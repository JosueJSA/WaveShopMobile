package com.client.waveshopmobile

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import models.User

class HomeActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle:ActionBarDrawerToggle
    private var ID:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ID = intent.getIntExtra("ID", -1)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_profile -> changeToProfile()
            R.id.nav_item_shopping_cart -> changeToShoppingCart()
            R.id.nav_item_shoppings -> changeToShoppings()
            R.id.nav_item_products -> changeToProducts()
            R.id.nav_item_sold -> changeToProductsSold()
            R.id.nav_item_users -> print("")
            R.id.nav_item_quit -> quit()
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun changeToProfile(){
        intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("mode", "Update")
        intent.putExtra("ID", ID)
        startActivity(intent)
        finish()
    }

    fun changeToShoppings(){
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, ShoppingsFragment(ID, this))
                .commitAllowingStateLoss()
    }

    fun changeToProductsSold(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, ProductsSoldFragment(ID, this))
            .commitAllowingStateLoss()
    }

    fun changeToProducts(){
        intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra("ID", ID)
        startActivity(intent)
        finish()
    }

    fun changeToShoppingCart(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, ShoppingCartFragment(ID, this))
            .commitAllowingStateLoss()
    }

    fun quit(){
        startActivity(Intent(this@HomeActivity, MainActivity::class.java))
        finish()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}