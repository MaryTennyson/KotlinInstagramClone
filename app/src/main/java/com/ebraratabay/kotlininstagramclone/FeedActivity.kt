package com.ebraratabay.kotlininstagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.ebraratabay.kotlininstagramclone.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerForContextMenu(binding.popUpMenu)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.insta_menu,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addpost-> Toast.makeText(this, "Add post",Toast.LENGTH_LONG)
            R.id.signout-> Toast.makeText(this, "Sign Out",Toast.LENGTH_LONG)
        }
        return super.onContextItemSelected(item)
    }
}