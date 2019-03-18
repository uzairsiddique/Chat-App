package com.example.messnager

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.messnager.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var CAMERA_IMAGE_REQ_CODE = 55
    var GALLERY_IMAGE_REQ_CODE = 44
    var CAMERA_PERMISSION_CODE = 11
    var GALLERY_PERMISSION_CODE = 22
    var SELECTED_PHOTO_URI: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_btn_reg.setOnClickListener {
            forRegister()
        }
        already_have_account_textview.setOnClickListener {


            Log.d("main activty ", "try to now login")

            val intent = Intent(this, login::class.java)
            startActivity(intent)

        }
        imageBtn_register.setOnClickListener {
            selectImageFromGallery()


        }
    }

    private fun selectImageFromGallery() {

        Log.d("Main", "Try to show photo")

//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, 0)


        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, GALLERY_IMAGE_REQ_CODE)
            return
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            GALLERY_PERMISSION_CODE
        )

    }

    private fun selectImageFromCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_IMAGE_REQ_CODE)
            return
        }
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromCamera()
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromGallery()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//            super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GALLERY_IMAGE_REQ_CODE -> {


                if (resultCode == Activity.RESULT_OK) {
                    val imageuri = data!!.getData()
                    imageBtn_register.setImageURI(imageuri)
                    SELECTED_PHOTO_URI = imageuri

                }
            }

        }
//            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//                Log.d("REsgiter ", "Photo Select")
//
//
//            val uri = data?.data
//            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            imageBtn_register.setBackgroundDrawable(bitmapDrawable)
//
//            }

    }

    private fun forRegister() {
//        val username=username_edit_reg.text.toString()
        val email = email_edit_reg.text.toString()
        val password = password_edit_reg.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter the email nd  password ", Toast.LENGTH_LONG).show()
            return
        }
//        Log.d("Main Activity", "username is " + username)
        Log.d("Main Activity", "Email is " + email)
        Log.d("Main Activity", "Password is $password")


//            Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    UploadImageToFirebase()
                    Log.d("Main ", "Succesfully Create user with Auth ")
                }


            }

            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

    }


    private fun UploadImageToFirebase() {

        if (SELECTED_PHOTO_URI == null) {
            Log.d("PHOTO URI", "Photo uri is null")
            return
        }
        val userId = FirebaseAuth.getInstance().uid
        val ref = FirebaseStorage.getInstance().getReference("/myimg/$userId")
        ref.putFile(SELECTED_PHOTO_URI!!)
            .addOnSuccessListener {
                Log.d("Register ", "Succefuly ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Log.d("Main", "File Location : ${it.path}")
                    saveUsertoDB(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("imagemain ", it.message)
            }

    }

//    private fun saveUsertoFirebase(profileImageurl: String) {
//        val uid = FirebaseAuth.getInstance().uid ?: ""
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//        val user = Users(uid, username_edit_reg.text.toString(), profileImageurl)
//
//        ref.setValue(user)
//
//            .addOnSuccessListener {
//                Log.d("Main", "Finllay we save the user in database")
//
//            }
//    }

    private fun saveUsertoDB(userImageUrl: String) {
//        val userId = FirebaseAuth.getInstance().uid ?: ""
//        val userName = username_edit_reg.text.toString()
//        val userEmail: String = email_edit_reg.text.toString()
//        val userId = FirebaseAuth.getInstance().uid ?: ""
//        val userName = username_edit_reg.text.toString()
//        val userEmail: String = email_edit_reg.text.toString()
//
////        val user = Users(userId,userName,userEmail,userImageUrl)
//
//        if (userId.isEmpty()) return
//        val fireDbRef = FirebaseDatabase.getInstance().reference
//        val user = Users(userId, userName, userEmail, userImageUrl)
////        val user = Users(userId, username_edit_reg.text.toString(),userEmail, profileImageurl)
//        fireDbRef.child("Users").child(userId).setValue(user)
//            .addOnSuccessListener {
//                Log.d("RegisterActivity", "User Data added to Db successfully!")
//                Toast.makeText(this@MainActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@MainActivity, login::class.java))
//            }
//            .addOnFailureListener {
//
//                Log.d("RegisterActivity", "Failed to add data to DB, Exception:" + it.message)
//                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
//            }
//    }
//

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = Users(uid, username_edit_reg.text.toString(), email_edit_reg.text.toString(), userImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {

                Log.d("main", "Succefuly crate")


                val intent = Intent(this, latest_message::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
//                Toast.makeText(this, "Database : ${it.}", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Database : ${it.message}", Toast.LENGTH_LONG).show()


            }


    }
}
