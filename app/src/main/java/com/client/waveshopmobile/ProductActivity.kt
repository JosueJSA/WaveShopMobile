package com.client.waveshopmobile

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import interfaces.OrderAPI
import interfaces.ProductAPI
import interfaces.ShoppingCartAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductActivity() : AppCompatActivity() {

    var ID:Int? = null
    var ProductSelected:Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ID = intent.getIntExtra("ID", -1)
        val idProduct = intent.getIntExtra("productId", -1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setData(idProduct)

        findViewById<Button>(R.id.sopBtn).setOnClickListener {
            c -> changeToNote()
        }

        findViewById<Button>(R.id.addToCartBtn).setOnClickListener {
            c -> addToShoppingCart()
        }

    }

    private fun addToShoppingCart(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ShoppingCartAPI::class.java).addToShoppingCart("api/ShoppingCart/$ID", prepareClass(ProductSelected!!))
            call.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>?) {
                    Toast.makeText(this@ProductActivity, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@ProductActivity, HomeActivity::class.java)
                    intent.putExtra("ID", ID)
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(call: Call<JsonObject?>?, t: Throwable?) {
                    Toast.makeText(this@ProductActivity, "Error de servidor", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun prepareClass(p:Product): Product = Product(
        p.id, p.name, p.description, p.videoAddress, Integer.parseInt(findViewById<EditText>(R.id.quantittyText).text.toString()), p.unitPrice, p.status, p.published, p.country, p.location,
        p.idCategory, p.idVendor, p.likesNumber, p.dislikesNumber, p.shoppedTimes, p.commentsNumber, p.lastUpdate, p.vendorUsername,
        p.comments, p.favorites, p.productSelectedCarts, p.productSelectedOrders, p.product_Images
    )

    private fun changeToNote(){
        intent = Intent(this@ProductActivity, ShopNoteActivity::class.java)
        intent.putExtra("ID", ID)
        intent.putExtra("productId", ProductSelected!!.id)
        intent.putExtra("quantity", Integer.parseInt(findViewById<EditText>(R.id.quantittyText).text.toString()))
        startActivity(intent)
        finish()
    }

    private fun setData(key:Int?){
        if(key != null)
            getProduct(key)
    }

    private fun getProduct(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ProductAPI::class.java).getProductById("api/Products/$id")
            call.enqueue(object : Callback<Product?> {
                override fun onResponse(call: Call<Product?>?, response: Response<Product?>?) {
                    if(response != null) {
                        ProductSelected = response.body()
                        showData(response.body())
                    }
                }

                override fun onFailure(call: Call<Product?>?, t: Throwable?) {
                    Toast.makeText(this@ProductActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            )
        }
    }

    override fun onBackPressed() {
        intent = Intent(this@ProductActivity, ProductsActivity::class.java)
        intent.putExtra("ID", ID)
        startActivity(intent)
        finish()
    }

    private fun showData(product:Product?){
        if(product != null) {
            ProductSelected = product
            findViewById<TextView>(R.id.productNameSelected).setText(product.name)
            findViewById<TextView>(R.id.qantity).setText(product.stockQuantity.toString())
            findViewById<TextView>(R.id.unitPrice).setText(product.unitPrice.toString())
            findViewById<TextView>(R.id.country).setText(product.country)
            findViewById<TextView>(R.id.location).setText(product.location)
            findViewById<TextView>(R.id.vendor).setText(product.vendorUsername)
            findViewById<TextView>(R.id.description).setText(product.description)
            Picasso.with(this@ProductActivity).load(product.product_Images[0].url)
                .into(findViewById<ImageView>(R.id.imageSelected))
        }
    }

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:5018/")
            .build()


}