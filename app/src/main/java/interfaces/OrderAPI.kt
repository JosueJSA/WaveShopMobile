package interfaces

import com.google.gson.JsonObject
import models.Product
import retrofit2.Call
import retrofit2.http.*
import java.io.ObjectInput

interface OrderAPI {

    @GET
    fun getProductsShopped(@Url iduser:String): Call<List<Product>>

    @GET
    fun getProductsSold(@Url idUser:String): Call<List<Product>>

    @Headers("Content-Type: application/json")
    @POST()
    fun shopUnitProduct(@Url url:String, @Body product: Product): Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST()
    fun shopCart(@Url url:String): Call<JsonObject>

}