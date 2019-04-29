package com.example.messnager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.Toast
import com.example.messnager.Adaptter.groupListAdapter
import com.example.messnager.Models.GroupChatModal
import com.example.messnager.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class GroupChatView : AppCompatActivity() {


    companion object {
        var userClick: Boolean = false
        var toGroup: GroupChatModal? = null
    }

    val groupData = ArrayList<GroupChatModal>()
    lateinit var adaptatar: groupListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_view)

        addgroupuser()

        supportActionBar?.title = "Group List"


        val recyeler: RecyclerView = findViewById(R.id.grouplist_recycler)
        recyeler.layoutManager = LinearLayoutManager(this)

        adaptatar = groupListAdapter(this, groupData, { view, postion ->
            val group = groupData[postion]
            toGroup = group
//            chatlog.to
            userClick = false
            startActivity(Intent(this, chatlog::class.java))
        })
        recyeler.adapter = adaptatar
        adaptatar.notifyDataSetChanged()


    }

    private fun addgroupuser() {

        val cuurent_user = FirebaseAuth.getInstance().currentUser
        val DataRef = FirebaseDatabase.getInstance().reference
        DataRef.child("GroupChat").addChildEventListener(object : ChildEventListener {
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
                if (dataSnapshot.exists()) {
                    try {
                        val group = dataSnapshot.getValue(GroupChatModal::class.java)

                        if (group!!.memberList!!.contains(cuurent_user!!.uid)) {
                            groupData.add(group)
                            println("test")

                        }
                        adaptatar.notifyDataSetChanged()

                    } catch (e:Exception){

                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })


    }

}
