package com.example.messnager

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.messnager.Adaptter.NewMessageAdapter
import com.example.messnager.GroupChatView.Companion.userClick
import com.example.messnager.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class new_message : AppCompatActivity() {
    companion object {
        var toUser: Users? = null
        var fromUser: Users? = null
    }

    //    newMessageAcativy= new_message
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        val userData = ArrayList<Users>()
//        userData.add(Users("", "Uzair", "uaair", ""))
        val recycler: RecyclerView = findViewById(R.id.recyler_new_message)
        recycler.layoutManager = LinearLayoutManager(this)

        val adapter = NewMessageAdapter(this@new_message, userData,
            { view, position ->
                val user = userData[position]
                new_message.toUser = user
//                Log.d("UserObj ", fromUser?.username)
//                Log.d("UerObj", toUser?.username)
                    userClick = true
                    startActivity(Intent(this@new_message, chatlog::class.java))
            })
        recycler.adapter = adapter
        adapter.notifyDataSetChanged()


        val dbRef = FirebaseDatabase.getInstance().reference

        Toast.makeText(applicationContext, "Contant", Toast.LENGTH_SHORT).show()

        dbRef.child("users").addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, p0.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
//                ue(Users::class.java)val user= dataSnapt.getVal
                val userId = FirebaseAuth.getInstance().uid
                val user = dataSnapshot.getValue(Users::class.java)
//                    when (user?.uid) {
//                        userId -> new_message.fromUser = user
//                        else -> {
//                            userData.add(user!!)
//                            Log.d("Users", user.toString())
//                            adapter.notifyDataSetChanged()
//                        }
//                    }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val userId = FirebaseAuth.getInstance().uid
                val user = dataSnapshot.getValue(Users::class.java)
                Toast.makeText(this@new_message, "Added", Toast.LENGTH_SHORT).show()
                when (user?.uid) {

                    userId -> new_message.fromUser = user
                    else -> {
                        userData.add(user!!)
                        Log.d("Users", user.toString())
                        adapter.notifyDataSetChanged()
                    }
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


    }
}
