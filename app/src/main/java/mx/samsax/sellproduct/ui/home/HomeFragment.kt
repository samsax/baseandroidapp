package mx.samsax.sellproduct.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import mx.samsax.sellproduct.R
import mx.samsax.sellproduct.utilities.BackendVolley
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var rvProducts: RecyclerView
    private val productsRVAdapter: ProductsRVAdapter = ProductsRVAdapter()
    private lateinit var backendVolley: BackendVolley

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        rvProducts = root.findViewById(R.id.rv_products)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backendVolley = BackendVolley(context as Context)
        setUpRecyclerView()
    }


    fun setUpRecyclerView(){
        rvProducts.setHasFixedSize(true)
        rvProducts.layoutManager = LinearLayoutManager(context as Context)
        getProducts()
    }

    fun getProducts(){

//        products.add(HomeViewModel.Product("Spiderman", "Marvel", "Peter Parker", "https://cursokotlin.com/wp-content/uploads/2017/07/spiderman.jpg"))
//        products.add(HomeViewModel.Product("Daredevil", "Marvel", "Matthew Michael Murdock", "https://cursokotlin.com/wp-content/uploads/2017/07/daredevil.jpg"))
//        products.add(HomeViewModel.Product("Wolverine", "Marvel", "James Howlett", "https://cursokotlin.com/wp-content/uploads/2017/07/logan.jpeg"))
//        products.add(HomeViewModel.Product("Batman", "DC", "Bruce Wayne", "https://cursokotlin.com/wp-content/uploads/2017/07/batman.jpg"))
//        products.add(HomeViewModel.Product("Thor", "Marvel", "Thor Odinson", "https://cursokotlin.com/wp-content/uploads/2017/07/thor.jpg"))
//        products.add(HomeViewModel.Product("Flash", "DC", "Jay Garrick", "https://cursokotlin.com/wp-content/uploads/2017/07/flash.png"))
//        products.add(HomeViewModel.Product("Green Lantern", "DC", "Alan Scott", "https://cursokotlin.com/wp-content/uploads/2017/07/green-lantern.jpg"))
//        products.add(HomeViewModel.Product("Wonder Woman", "DC", "Princess Diana", "https://cursokotlin.com/wp-content/uploads/2017/07/wonder_woman.jpg"))
//        return products

        val path = "pokemon/pikachu"
        val params = JSONObject()
//        params.put("email", "foo@email.com")
//        params.put("password", "barpass")
        val TAG = "request"

        val basePath = "https://pokeapi.co/api/v2/"

        val jsonObjReq = object : JsonObjectRequest(
            Method.GET, basePath + path, params,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "/get request OK! Response: $response")
                showProducts(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }

        backendVolley.addToRequestQueue(jsonObjReq, TAG)
    }

    private fun showProducts(response: JSONObject?) {
        val products:MutableList<HomeViewModel.Product> = ArrayList()
        val stats = response!!.getJSONArray("stats")
        val sprits = response!!.getJSONObject("sprites")
        var stat : JSONObject
        var maxItem: Number
        var minItem:Number
        var name:String
        var photo:String
        for (x in 0..(stats.length()-1)){
            stat = stats.getJSONObject(x)
            maxItem = stat.getInt("base_stat")
            minItem = stat.getInt("effort")
            name = stat.getJSONObject("stat").getString("name")
            photo = sprits.getString("front_default")
            products.add(HomeViewModel.Product(name, maxItem, minItem, photo))
        }
        productsRVAdapter.ProductsRVAdapter(products,context as Context)
        rvProducts.adapter = productsRVAdapter

    }
}