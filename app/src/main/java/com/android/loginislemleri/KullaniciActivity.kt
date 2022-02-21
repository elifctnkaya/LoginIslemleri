package com.android.loginislemleri

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_kullanici.*

class KullaniciActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanici)


        auth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("loginsp", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val mail = sharedPreferences.getString("email", "bos")
        val sifre = sharedPreferences.getString("sifre", "bos")

        /*textViewEmail.text = mail.toString()
        textViewSifre.text = sifre.toString()*/

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.cikis_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.exit){
           cikisYap()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun cikisYap(){
        editor.remove("email")
        editor.remove("sifre")
        editor.commit()

        auth.signOut()

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}