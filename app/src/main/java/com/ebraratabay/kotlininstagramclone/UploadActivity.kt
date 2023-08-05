package com.ebraratabay.kotlininstagramclone

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ebraratabay.kotlininstagramclone.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    fun onClickAddImage(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(view, "Permission needed for Gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give Permission") {
                        //request permission
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                    }.show()
            } else {
                //request permission
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentGallery)
        }


    }

    fun onClickUploadPost(view: View) {
        val uuid = UUID.randomUUID() //universal uniq id
        val imagename = "$uuid.jpg"
        val reference = storage.reference
        val imagereference = reference.child("images").child(imagename)
        if (selectedPicture != null) {
            imagereference.putFile(selectedPicture!!).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("images").child(imagename)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()

                    val postMap= hashMapOf<String,Any>()
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("userEmail",auth.currentUser!!.email!!)
                    postMap.put("comment",binding.enterdescriptionOfPost.text.toString())
                 postMap.put("date",Timestamp.now())
                    firestore.collection("Posts").add(postMap).addOnSuccessListener {
                        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@UploadActivity, FeedActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                        .addOnFailureListener {Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()  }
                }
            }.addOnFailureListener { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show() }
        }


    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        selectedPicture?.let { binding.addimage.setImageURI(it) }
                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //permission granted
                    val intentGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentGallery)
                } else {
                    Toast.makeText(this@UploadActivity, "Permission needed", Toast.LENGTH_LONG)
                        .show()
                }
            }

    }


}