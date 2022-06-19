package com.client.waveshopmobile

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import interfaces.ProductAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsActivity : AppCompatActivity() {

    var ID:Int? = null

    private var layourManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        ID = intent.getIntExtra("ID", -1)
        findViewById<Button>(R.id.searchBtn).setOnClickListener {
            c -> getProducts()
        }

    }

    private fun getProducts(){
        val search = findViewById<TextView>(R.id.productName).text
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ProductAPI::class.java).getProductsByName("api/Products/0/$search")
            call.enqueue(
                object : Callback<List<Product>?> {
                    override fun onResponse(call: Call<List<Product>?>?, response: Response<List<Product>?>?) {
                        setProducts(response?.body())
                    }

                    override fun onFailure(call: Call<List<Product>?>?, t: Throwable?) {
                        Toast.makeText(this@ProductsActivity, "El servidor no está respondiendo correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    override fun onBackPressed() {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("ID", ID)
        startActivity(intent)
        finish()
    }

    private fun setProducts(products:List<Product>?){
        if(products != null){
            layourManager = LinearLayoutManager(this)
            val recycle = findViewById<RecyclerView>(R.id.recyclerView)
            recycle.layoutManager = layourManager
            adapter = RecyclerAdapter(products, this, ID, null)
            recycle.adapter = adapter
        }
    }

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:5018/")
            .build()
}