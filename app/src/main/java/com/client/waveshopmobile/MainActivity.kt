package com.client.waveshopmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import interfaces.UserAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.SignInBtn).setOnClickListener {
            action -> signIn()
        }


        findViewById<Button>(R.id.SignUpBtn).setOnClickListener {
                action -> SignUp()
        }
    }

    private fun SignUp(){
        intent = Intent(this@MainActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun signIn(){
        val email = findViewById<TextView>(R.id.TextEmail).text
        val pass = findViewById<TextView>(R.id.TextPassword).text
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(UserAPI::class.java).getUserByEmail("api/Users/mail/$email")
            call.enqueue(
                object : Callback<User?> {
                    override fun onResponse(call: Call<User?>?, response: Response<User?>?) {
                        val user = response?.body()
                        if(user?.password == pass.toString()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Bienvenido ${user?.userName}",
                                Toast.LENGTH_SHORT
                            ).show()
                            intent = Intent(this@MainActivity, HomeActivity::class.java)
                            intent.putExtra("ID", user?.id)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@MainActivity, "Verifica tu contraseña ${response?.body()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User?>?, t: Throwable?) {
                        Toast.makeText(this@MainActivity, "$t", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:5018/")
            .build()
}