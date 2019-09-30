package mx.samsax.sellproduct.ui.home;

import android.content.Context;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mx.samsax.sellproduct.R

class ProductsRVAdapter : RecyclerView.Adapter<ProductsRVAdapter.ProductViewHolder>() {
    var products: MutableList<HomeViewModel.Product>  = ArrayList()
    lateinit var context:Context

    fun ProductsRVAdapter(products : MutableList<HomeViewModel.Product>, context: Context){
        this.products = products
        this.context = context
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = products.get(position)
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(
            layoutInflater.inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return products.size
    }


class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val superheroName = view.findViewById(R.id.tvSuperhero) as TextView
        val realName = view.findViewById(R.id.tvRealName) as TextView
        val publisher = view.findViewById(R.id.tvPublisher) as TextView
        val avatar = view.findViewById(R.id.ivAvatar) as ImageView

        fun bind(product:HomeViewModel.Product, context: Context){
        superheroName.text = product.name
        realName.text = product.max_size.toString()
        publisher.text = product.min_size.toString()
        itemView.setOnClickListener(View.OnClickListener { Toast.makeText(context, product.name, Toast.LENGTH_SHORT).show() })
        avatar.loadUrl(product.photo)
        }
        fun ImageView.loadUrl(url: String) {
        Picasso.get().load(url).into(this)
        }
        }
}
