package interfaces

import com.google.gson.JsonObject
import models.Product
import models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    @GET
    fun getUserByEmail(@Url email:String):Call<User>

    @GET("Users")
    fun getUsers(): Call<List<User>>

    @GET
    fun getUserById(@Url id:String): Call<User>

    @Headers("Content-Type: application/json")
    @POST()
    fun addUser(@Url url:String, @Body user:User): Call<User>

    @Headers("Content-Type: application/json")
    @PUT()
    fun updateUser(@Url url:String, @Body user:User): Call<User>

}