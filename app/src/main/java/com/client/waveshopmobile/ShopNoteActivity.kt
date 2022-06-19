package com.client.waveshopmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
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
import java.io.ObjectInput
import java.util.*

class ShopNoteActivity : AppCompatActivity() {

    var ID:Int? = null
    private var layourManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    var IdProduct:Int? = null
    var IdCart:Int? = null
    var Quantity:Int? = null
    var ProductSelectd:Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ID = intent.getIntExtra("ID", -1)
        IdProduct = intent.getIntExtra("productId", -1)
        Quantity = intent.getIntExtra("quantity", -1)
        IdCart = intent.getIntExtra("4", -1)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_note)
        findViewById<TextView>(R.id.dateLbl).setText(Calendar.getInstance().time.toString())
        if(IdProduct != -1){
            getProduct(IdProduct!!)
        }else{
            getProducts(IdCart!!)
        }

        findViewById<Button>(R.id.shopBtn).setOnClickListener {
            if(IdCart != -1){
                shopCart()
            }else{
                shopUnitProduct()
            }
        }
    }

    private fun shopUnitProduct(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(OrderAPI::class.java).shopUnitProduct("api/Orders/buy/$ID", ProductSelectd!!)
            call.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>?) {
                    Toast.makeText(this@ShopNoteActivity, "Producto comprado exitosamente", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@ShopNoteActivity, HomeActivity::class.java)
                    intent.putExtra("ID", ID)
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(call: Call<JsonObject?>?, t: Throwable?) {
                    Toast.makeText(this@ShopNoteActivity, "Error de servidor", Toast.LENGTH_SHORT).show()
                }
            }
            )
        }
    }

    private fun shopCart(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(OrderAPI::class.java).shopCart("api/Orders/$ID")
            call.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>?) {
                    Toast.makeText(this@ShopNoteActivity, "Carrito comprado exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ShopNoteActivity, HomeActivity::class.java)
                    intent.putExtra("ID", ID)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<JsonObject?>?, t: Throwable?) {
                    Toast.makeText(this@ShopNoteActivity, "Error de servidor", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getProduct(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ProductAPI::class.java).getProductById("api/Products/$id")
            call.enqueue(object : Callback<Product?> {
                override fun onResponse(call: Call<Product?>?, response: Response<Product?>?) {
                    val product:Product? = response?.body()
                    if(product != null) {
                        ProductSelectd = prepareClass(product)
                        setProducts(listOf(ProductSelectd!!))
                        findViewById<TextView>(R.id.totalLbl).setText((Quantity!! * product.unitPrice).toString())
                    }
                }

                override fun onFailure(call: Call<Product?>?, t: Throwable?) {
                    Toast.makeText(this@ShopNoteActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            )
        }
    }

    private fun prepareClass(p:Product): Product = Product(
            p.id, p.name, p.description, p.videoAddress, Quantity!!, p.unitPrice, p.status, p.published, p.country, p.location,
            p.idCategory, p.idVendor, p.likesNumber, p.dislikesNumber, p.shoppedTimes, p.commentsNumber, p.lastUpdate, p.vendorUsername,
            p.comments, p.favorites, p.productSelectedCarts, p.productSelectedOrders, p.product_Images
        )


    private fun getProducts(id:Int?){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ShoppingCartAPI::class.java).getProductsIntoCart("api/ShoppingCart/$id")
            call.enqueue(
                object : Callback<List<Product>?> {
                    override fun onResponse(call: Call<List<Product>?>?, response: Response<List<Product>?>?) {
                        val products:List<Product>? = response?.body()
                        if(products != null) {
                            setProducts(products)
                            findViewById<TextView>(R.id.totalLbl).setText(products.map { it.unitPrice * it.stockQuantity }.sum().toString())
                        }
                    }

                    override fun onFailure(call: Call<List<Product>?>?, t: Throwable?) {
                        Toast.makeText(this@ShopNoteActivity, "El servidor no está respondiendo correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun setProducts(products: List<Product>){
        if(products != null){
            layourManager = LinearLayoutManager(this)
            val recycle = findViewById<RecyclerView>(R.id.recyclerView)
            recycle.layoutManager = layourManager
            if(Quantity != -1) {
                adapter = RecyclerAdapter(products, this, ID, Quantity)
            }else {
                adapter = RecyclerAdapter(products, this, ID, null)
            }
            recycle.adapter = adapter
        }
    }

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:5018/")
            .build()


}