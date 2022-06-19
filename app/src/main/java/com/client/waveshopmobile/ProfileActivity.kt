package com.client.waveshopmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import interfaces.OrderAPI
import interfaces.ProductAPI
import interfaces.UserAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.Address
import models.Product
import models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class ProfileActivity() : AppCompatActivity() {

    var MODE:String? = null
    var ID:Int? = null
    var USER:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MODE = intent.getStringExtra("mode")
        ID = intent.getIntExtra("ID", -1)
        setContentView(R.layout.activity_profile)

        if(MODE == "Update"){
            getUser(ID!!)
        }

        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            if(MODE == "Update"){
                updateUser()
            }else{
                addUser()
            }
        }

        findViewById<Button>(R.id.cancelSaveBtn).setOnClickListener {
            intent = Intent(this@ProfileActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addUser(){
        if(findViewById<CheckBox>(R.id.checkBox).isChecked && findViewById<EditText>(R.id.adminCodeTxt).text.toString() != "113"){
            Toast.makeText(this, "Verifica que el código de aministrador sea correcto", Toast.LENGTH_SHORT).show()
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(UserAPI::class.java).addUser("api/Users", getDataFromForm())
                call.enqueue(object : Callback<User?> {
                    override fun onResponse(call: Call<User?>?, response: Response<User?>?) {
                        val user:User? = response?.body()
                        if(user != null) {
                            Toast.makeText(this@ProfileActivity, "Usuario agregado", Toast.LENGTH_SHORT)
                                .show()
                            intent = Intent(this@ProfileActivity, HomeActivity::class.java)
                            intent.putExtra("ID", user.id)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<User?>?, t: Throwable?) {
                        Toast.makeText(this@ProfileActivity, "Error al registrar un usuario", Toast.LENGTH_SHORT).show()
                        intent = Intent(this@ProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    private fun updateUser(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(UserAPI::class.java).updateUser("api/Users/$ID", getDataFromFormUpdate())
            call.enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>?, response: Response<User?>?) {
                    val user:User? = response?.body()
                    if(user != null) {
                        Toast.makeText(this@ProfileActivity, "Usuario agregado", Toast.LENGTH_SHORT)
                            .show()
                        intent = Intent(this@ProfileActivity, HomeActivity::class.java)
                        intent.putExtra("ID", user.id)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<User?>?, t: Throwable?) {
                    Toast.makeText(this@ProfileActivity, "Error al registrar un usuario", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@ProfileActivity, HomeActivity::class.java)
                    intent.putExtra("ID", ID)
                    startActivity(intent)
                }
            })
        }
    }

    override fun onBackPressed() {
        intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("ID", ID)
        startActivity(intent)
        finish()
    }

    private fun setDataInForm(user:User){
        findViewById<EditText>(R.id.userNameTxt).setText(user.userName)
        findViewById<EditText>(R.id.emailTxt).setText(user.email)
        findViewById<EditText>(R.id.passwordTxt).setText(user.password)
        findViewById<EditText>(R.id.phoneTxt).setText(user.phone)
        findViewById<EditText>(R.id.bithdayTxt).setText(user.birthDay)
        findViewById<EditText>(R.id.descriptionTxtr).setText(user.description)
        findViewById<CheckBox>(R.id.checkBox).isVisible = false
        findViewById<EditText>(R.id.adminCodeTxt).isVisible = false
        findViewById<Button>(R.id.cancelSaveBtn).isVisible = false
    }

    private fun getDataFromForm():User{
        val f = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(findViewById<EditText>(R.id.bithdayTxt).text.toString(), f)
        val p = Period.between(date, LocalDate.now())
        var typeUser:String = "Cliente"
        if(findViewById<CheckBox>(R.id.checkBox).isChecked){
            typeUser = "Administrador"
        }
        return User(
            -1,
            findViewById<EditText>(R.id.emailTxt).text.toString(),
            findViewById<EditText>(R.id.userNameTxt).text.toString(),
            findViewById<EditText>(R.id.passwordTxt).text.toString(),
            findViewById<EditText>(R.id.phoneTxt).text.toString(),
            findViewById<EditText>(R.id.descriptionTxtr).text.toString(),
            "Activo",
            findViewById<EditText>(R.id.bithdayTxt).text.toString(),
            p.years,
            typeUser,
            "Good",
            LocalDate.now().toString(),
            LocalDate.now().toString(),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    private fun getDataFromFormUpdate():User{
        val f = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(findViewById<EditText>(R.id.bithdayTxt).text.toString(), f)
        val p = Period.between(date, LocalDate.now())
        return User(
            USER!!.id,
            findViewById<EditText>(R.id.emailTxt).text.toString(),
            findViewById<EditText>(R.id.userNameTxt).text.toString(),
            findViewById<EditText>(R.id.passwordTxt).text.toString(),
            findViewById<EditText>(R.id.phoneTxt).text.toString(),
            findViewById<EditText>(R.id.descriptionTxtr).text.toString(),
            USER!!.status,
            findViewById<EditText>(R.id.bithdayTxt).text.toString(),
            p.years,
            USER!!.userType,
            USER!!.reputation,
            LocalDate.now().toString(),
            LocalDate.now().toString(),
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    private fun getUser(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(UserAPI::class.java).getUserById("api/Users/$id")
            call.enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>?, response: Response<User?>?) {
                    val user:User? = response?.body()
                    if(user != null){
                        USER = user
                        setDataInForm(USER!!)
                    }
                }

                override fun onFailure(call: Call<User?>?, t: Throwable?) {
                    Toast.makeText(this@ProfileActivity, "No se encontró al usuario en el sistema", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@ProfileActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }
    }

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://10.0.2.2:5018/")
            .build()


}