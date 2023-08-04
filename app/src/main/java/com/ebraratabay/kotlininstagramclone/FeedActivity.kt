package com.ebraratabay.kotlininstagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.ebraratabay.kotlininstagramclone.databinding.ActivityFeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerForContextMenu(binding.popUpMenu)
        auth=Firebase.auth
    }


    fun showPopUponExtra(view: View){
        val popup= PopupMenu(this,view)
        val inflater: MenuInflater= popup.menuInflater
        inflater.inflate(R.menu.insta_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.signout->{
                    auth.signOut()
                    val intent= Intent(this@FeedActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                R.id.addpost->{
                    val intent= Intent(this@FeedActivity,UploadActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }
            true
        }
        popup.show()

    }


}