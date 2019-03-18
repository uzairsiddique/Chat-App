package com.example.messnager

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login.*

class login : AppCompatActivity() {
    companion object {
        fun UserLoggedIn(): Boolean {
            val user = FirebaseAuth.getInstance().uid
            if (user!!.isNotEmpty()) {
                return true
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

//        val mail: TextView = findViewById(R.id.email_edit_login)
//        val password: TextView = findViewById(R.id.password_edit_login)

        login_btn.setOnClickListener {
            val mail = email_edit_login.text.toString()
            val password = password_edit_login.text.toString()
            Log.d("Login", "Attempt login with email $mail")
            SignInUser(mail, password)

        }
        back_to_reg.setOnClickListener {

            val intent = Intent(this@login, MainActivity::class.java)
            startActivity(intent)

        }
    }

    private fun SignInUser(mail: String, password: String) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener {

                if (!it.isSuccessful) {
                    Log.d("Login", "Logged in failure")
                    return@addOnCompleteListener
                    Toast.makeText(this@login, "Login Sucessfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@login, latest_message::class.java)
                    startActivity(intent)
                    this.finish()
                    Log.d("Login", "User ${mail} login in ")
                }

            }
            .addOnFailureListener {

                Toast.makeText(this@login, it.message, Toast.LENGTH_SHORT).show()
                return@addOnFailureListener


            }


    }


}