package com.ebraratabay.kotlininstagramclone.adapter

import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebraratabay.kotlininstagramclone.databinding.FeedPostBinding
import com.ebraratabay.kotlininstagramclone.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter (val postList: ArrayList<Post>): RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {
    class PostHolder(val binding: FeedPostBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding= FeedPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        val username= postList.get(position).email.substring(0,  postList.get(position).email.indexOf("@"))
       holder.binding.ownernameofpost.text=username
        holder.binding.descriptionofpost.text=postList.get(position).comment
      Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.feedImageView)
    }
}