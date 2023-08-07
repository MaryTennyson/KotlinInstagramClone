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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebraratabay.kotlininstagramclone.adapter.FeedRecyclerAdapter
import com.ebraratabay.kotlininstagramclone.databinding.ActivityFeedBinding
import com.ebraratabay.kotlininstagramclone.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var feedAdapter: FeedRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerForContextMenu(binding.popUpMenu)
        auth=Firebase.auth
        db= Firebase.firestore


        postArrayList= ArrayList<Post>()
        getData()

        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        feedAdapter= FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter=feedAdapter
    }
    private fun getData() {
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING ).addSnapshotListener { value, error ->
            if(error !=null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value!=null){
                    if(!value.isEmpty){

                        val documents= value.documents
                        postArrayList.clear()
                        for(document in documents){

                            //casting
                            val comment= document.get("comment") as String

                            val downloadUrl= document.get("downloadUrl") as String
                            val userEmail= document.get("userEmail") as String
                            val post= Post(userEmail,comment,downloadUrl )

                            postArrayList.add(post)
                        }

                        feedAdapter.notifyDataSetChanged()
                    }
                }

            }

        }
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