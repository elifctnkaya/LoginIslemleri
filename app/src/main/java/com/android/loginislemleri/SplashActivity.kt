package com.android.loginislemleri

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        auth = Firebase.auth

        sharedPreferences = getSharedPreferences("loginsp", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val kontrolmail = sharedPreferences.getString("email", "bos")
        val kontrolsifre = sharedPreferences.getString("sifre", "bos")

        userID = auth.currentUser?.uid.toString()
        database = Firebase.database.reference.child("Users").child(userID)

        val bekleyis = object :Thread(){

            override fun run() {
                super.run()
                try {
                    Thread.sleep(4000)
                    if(!kontrolmail.equals("bos") && !kontrolsifre.equals("bos") && !kontrolmail.equals("") && !kontrolsifre.equals("")){
                        database.child("yoneticilik").get().addOnSuccessListener {
                            if (it.value != null) {
                                if (it.value!!.equals("true")) {
                                    yoneticiYonlendir()
                                } else {
                                    kullniciYonlendir()
                                }
                            }
                        }
                    }
                    mainYonlendir()
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        bekleyis.start()
    }

    private fun mainYonlendir(){
        val intent  = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun yoneticiYonlendir()
    {
        val intent = Intent(baseContext, YoneticiActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun kullniciYonlendir()
    {
        val intent = Intent(baseContext, KullaniciActivity::class.java)
        startActivity(intent)
        finish()
    }


}