package com.client.waveshopmobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import models.Product

class RecyclerAdapter(val products:List<Product>, val context: Context, val id:Int?, val quanti:Int?) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var productList = products
    var currentContext:Context = context
    var ID:Int? = id;
    var Quantity:Int? = quanti

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = productList[position].name
        holder.itemDetail.text = productList[position].description
        if(Quantity != null){
            holder.itemQuantity.text = Quantity.toString()
        }else{
            holder.itemQuantity.text = productList[position].stockQuantity.toString()
        }
        holder.itemPrice.text = productList[position].unitPrice.toString()
        Picasso.with(currentContext).load(productList[position].product_Images[0].url).into(holder.itemImage)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        var itemPrice: TextView
        var itemQuantity: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.nameProduct)
            itemDetail = itemView.findViewById(R.id.descripcion)
            itemPrice = itemView.findViewById(R.id.price)
            itemQuantity = itemView.findViewById(R.id.quantity)

            itemView.setOnClickListener {
                val position: Int = layoutPosition
                val intent = Intent(it.context, ProductActivity::class.java)
                intent.putExtra("productId", productList[position].id)
                intent.putExtra("ID", ID)
                it.context.startActivity(intent)
            }
        }
    }
}