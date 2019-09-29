package mx.samsax.sellproduct.ui.map


import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import mx.samsax.sellproduct.R
import java.lang.Exception
import java.util.*


class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnCameraMoveListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    private val locale: Locale = Locale("ES", "MX")
    private lateinit var geocoder: Geocoder
    private lateinit var txtAddress: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        txtAddress = findViewById(R.id.txt_address)
        geocoder = Geocoder(this, locale)
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val city = LatLng(17.9868908, -92.9302826)
        marker = mMap.addMarker(MarkerOptions().position(city).title("Marker in Sydney"))
        mMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    city,
                    16.0f
                )
            )
        )
        mMap.setOnCameraIdleListener(this)
        mMap.setOnCameraMoveListener(this)
        if (setUpMap()) {
            activateLocation()
        }


    }

    private fun activateLocation() {
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
//                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16.0f))
            }
        }
    }

    override fun onCameraIdle() {

        marker.position = mMap.cameraPosition.target
        try {
            val fromLocation = geocoder.run {
                getFromLocation(
                    mMap.cameraPosition.target.latitude,
                    mMap.cameraPosition.target.longitude, 1
                )
            }
            txtAddress.setText(fromLocation.get(0).getAddressLine(0))
        } catch (e: Exception) {
            println(e.localizedMessage)
        }



        println(mMap.cameraPosition.target.latitude)

        println(mMap.cameraPosition.target.longitude)
    }

    override fun onCameraMove() {
        marker.position = mMap.cameraPosition.target
    }

    private fun setUpMap(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activateLocation()
                } else {
                    val make = Snackbar.make(
                        txtAddress,
                        "Sin permiso para obtener localizacion",
                        Snackbar.LENGTH_LONG
                    )
                    make.show()
                }
            }
        }
    }
}
