package com.android.loginislemleri

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var yoneticiDeger: String

    private lateinit var dataMap: HashMap<String,String>
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        yoneticiDeger = "false"

        auth = Firebase.auth


        sharedPreferences = getSharedPreferences("loginsp", Context.MODE_PRIVATE)


        radioButton.setOnClickListener {
            yoneticiDeger = "true"
        }

       
        //firebasede kullanımı
        // güncel kullanici varsa direkt kullanici activitye geçer yani burada uygulamaya tekrar girildiğinde giriş yapılmasına gerek kalmaz
        /* val guncelKullanici = auth.currentUser
        if(guncelKullanici != null){
            intentFonk()
        }*/

    }

    fun girisYap(view: View){
        auth.signInWithEmailAndPassword(textEmail.text.toString(),textPassword.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //val guncelKullanici = auth.currentUser?.email.toString() --> güncel kullanıcı
                    spKayit()
                }
        }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    fun kayitOl(view: View){

        auth.createUserWithEmailAndPassword(textEmail.text.toString(),textPassword.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    realtimeDatabaceEkle()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    private fun kullaniciIntent(){
        val intent = Intent(this, KullaniciActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun yoneticiIntent(){
        val intent = Intent(this, YoneticiActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun spKayit(){
        val email = textEmail.text.toString()
        val sifre = textPassword.text.toString()
        editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("sifre", sifre)
        editor.apply()
        kontrol()

    }

    private fun realtimeDatabaceEkle() {
        dataMap = HashMap()
        userID = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().reference.child("Users").child(userID)
        dataMap.put("mail", textEmail.text.toString())
        dataMap.put("yoneticilik", yoneticiDeger)
        database.setValue(dataMap).addOnCompleteListener {
            if (it.isSuccessful){
                spKayit()
            }
        }

    }


   private fun kontrol() {
        userID = auth.currentUser?.uid.toString()
        database = Firebase.database.reference.child("Users").child(userID)
            database.child("yoneticilik").get().addOnSuccessListener {
            if(it.value != null){
                if (it.value!!.equals("true")){
                    yoneticiIntent()
                }
                else{
                    kullaniciIntent()
                }
            }
        }

    }


}


