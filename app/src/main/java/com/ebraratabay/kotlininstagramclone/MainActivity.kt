package com.ebraratabay.kotlininstagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ebraratabay.kotlininstagramclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        val currentUser= auth.currentUser
        if(currentUser!=null){
            val intent= Intent(this@MainActivity,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
   fun  onSignInClicked(view:View){
       val email= binding.enterEmail.text.toString()
       val password= binding.enterPassword.text.toString()
       if(email.isNotEmpty() && password.isNotEmpty()){
           auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
               val intent= Intent(this@MainActivity,FeedActivity::class.java)
               startActivity(intent)
               finish()
           }.addOnFailureListener {
               Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()

           }


       }else{
           Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG)
       }


    }
    fun onSignUpClicked(view: View){
        val email=binding.enterEmail.text.toString()
        val password= binding.enterPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent= Intent(this@MainActivity,FeedActivity::class.java)
               startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }


        }else{
            Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG).show()
        }


    }
}