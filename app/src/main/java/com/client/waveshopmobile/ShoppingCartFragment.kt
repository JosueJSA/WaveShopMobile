package com.client.waveshopmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import interfaces.OrderAPI
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingCartFragment(val user:Int?, val conn:Context) : Fragment() {

    private var ID:Int? = user
    private var layourManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private var cont: Context = conn
    private var recycle: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycle = view.findViewById<RecyclerView>(R.id.recyclerView)
        getProducts(ID)

        view.findViewById<Button>(R.id.shopCart).setOnClickListener {
            c -> shopCart()
        }
    }

    private fun shopCart(){
        val intent = Intent(activity, ShopNoteActivity::class.java)
        intent.putExtra("ID", ID)
        intent.putExtra("cart", ID)
        startActivity(intent)
    }

    private fun getProducts(id:Int?){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ShoppingCartAPI::class.java).getProductsIntoCart("api/ShoppingCart/$id")
            call.enqueue(
                object : Callback<List<Product>?> {
                    override fun onResponse(call: Call<List<Product>?>?, response: Response<List<Product>?>?) {
                        setProducts(response?.body())
                    }

                    override fun onFailure(call: Call<List<Product>?>?, t: Throwable?) {
                        Toast.makeText(cont, "El servidor no está respondiendo correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }


    private fun setProducts(products:List<Product>?){
        if(products != null){
            layourManager = LinearLayoutManager(cont)
            recycle?.layoutManager = layourManager
            adapter = activity?.let { RecyclerAdapter(products, it, ID, null) }
            recycle?.adapter = adapter
        }
    }

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:5018/")
            .build()


}