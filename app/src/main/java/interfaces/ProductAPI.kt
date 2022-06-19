package interfaces

import models.Product
import models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ProductAPI {

    @GET
    fun getProductsByName(@Url name:String): Call<List<Product>>

    @GET
    fun getProductById(@Url id:String): Call<Product>

}