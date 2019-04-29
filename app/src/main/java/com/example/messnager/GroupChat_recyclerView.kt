package com.example.messnager

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import com.example.messnager.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.messnager.Adaptter.GroupChat_adapter
import com.example.messnager.Models.GroupChatModal
import com.example.messnager.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.create_daillog_box.*

class GroupChat_recyclerView : AppCompatActivity() {

    companion object {
        var toUser: Users? = null
        var fromUser: Users? = null
        var group_member_id = ArrayList<String>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_recycler_view)
        val AddGroup: FloatingActionButton = findViewById(R.id.add_group)


        AddGroup.setOnClickListener {

            DailBox()


        }


        supportActionBar?.title = "Select Group Users"

        val userData = ArrayList<Users>()
        val recycler: RecyclerView = findViewById(R.id.recyler_group_chat)

        recycler.layoutManager = LinearLayoutManager(this)

        val adapter = GroupChat_adapter(this, userData, { view, positon ->
            val users = userData[positon]
            toUser = users
            startActivity(Intent(this, GroupChatLog::class.java))
        })
        recycler.adapter = adapter

        adapter.notifyDataSetChanged()

        val dbRef = FirebaseDatabase.getInstance().reference
//        Toast.makeText(this, "Users", Toast.LENGTH_LONG).show()

        dbRef.child("users").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, p0.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val userId = FirebaseAuth.getInstance().uid
                val user = dataSnapshot.getValue(Users::class.java)
//  Toast.makeText(this,"")
                when (user?.uid) {
                    userId -> GroupChat_recyclerView.fromUser = user
                    else -> {

                        userData.add(user!!)
                        adapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }

    private fun DailBox() {


        val dail = Dialog(this)
        dail.setContentView(R.layout.create_daillog_box)

        val groupname = dail.findViewById<TextView>(R.id.group_name)
        val CancelBtn = dail.findViewById<Button>(R.id.btn_cancel)
        val okBtn = dail.findViewById<Button>(R.id.btn_ok)

        CancelBtn.setOnClickListener {
            dail.dismiss()

        }
        okBtn.setOnClickListener {
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/GroupChat").push()
            val GroupChat = GroupChatModal(ref.key!!, groupname.text.toString(), group_member_id)

            ref.setValue(GroupChat)
                .addOnSuccessListener {


                    val intent = Intent(this, GroupChatView::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Created Group", Toast.LENGTH_LONG).show()
                    finish()

                }
                .addOnFailureListener {

                    Toast.makeText(this, "fail", Toast.LENGTH_LONG).show()
                }

            Toast.makeText(this, "wokring", Toast.LENGTH_LONG).show()
        }
//        dail.window?.setLayout(1050, 900)
        dail.show()


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.viewgrouplist_menu -> {
                val intent = Intent(this, GroupChatView::class.java)
                startActivity(intent)
            }

        }


        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.groupview, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
