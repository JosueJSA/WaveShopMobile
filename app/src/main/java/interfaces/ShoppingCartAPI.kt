package interfaces

import com.google.gson.JsonObject
import models.Product
import retrofit2.Call
import retrofit2.http.*

interface ShoppingCartAPI {

    @GET
    fun getProductsIntoCart(@Url idUser:String): Call<List<Product>>

    @Headers("Content-Type: application/json")
    @POST()
    fun addToShoppingCart(@Url url:String, @Body product: Product): Call<JsonObject>
}