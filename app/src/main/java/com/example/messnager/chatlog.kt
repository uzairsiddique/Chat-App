package com.example.messnager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.messnager.Adaptter.chat_adapter_RecyclerView
import com.example.messnager.Models.ChatMessage
import com.example.messnager.Models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chatlog.*

class chatlog : AppCompatActivity() {

    lateinit var chatData: ArrayList<ChatMessage>
    lateinit var adapter: chat_adapter_RecyclerView
    lateinit var user: Users
    var fromId = FirebaseAuth.getInstance().uid
    var toId: String = new_message.toUser!!.uid



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)

        displayMessagesFromDb()
        chatData = ArrayList()
        user = Users()

//        chatData.add(ChatMessage("","kesi ho","",""))

        val recyCleview: RecyclerView = findViewById(R.id.recyView_chaLog)
        adapter = chat_adapter_RecyclerView(this@chatlog, chatData)
        recyCleview.layoutManager = LinearLayoutManager(this@chatlog)
        recyCleview.adapter = adapter
        adapter.notifyDataSetChanged()
        send_btn_chatLog.setOnClickListener {
            if (textMsg.text.toString().isNotEmpty()) {
                sendMessagetoDb(textMsg.text.toString())
                textMsg.text.clear()
            }
        }
        supportActionBar?.title = new_message.toUser!!.username
    }


    private fun displayMessagesFromDb() {
        val dbRef = FirebaseDatabase.getInstance().getReference("/UserMessages/" + fromId + toId)

        Log.d("Displaymessage","Usermesages ")
        dbRef.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                val messageData = dataSnapshot.getValue(ChatMessage::class.java)
                if (messageData != null) {
                    chatData.add(messageData)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@chatlog, "MessageData  is null", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val messageData = dataSnapshot.getValue(ChatMessage::class.java)
                if (messageData != null) {
                    chatData.add(messageData)
                    adapter.notifyDataSetChanged()
                    recyView_chaLog.scrollToPosition(adapter.itemCount - 1)
                } else {
                    Toast.makeText(this@chatlog, "MessageData is null", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })

    }

    private fun sendMessagetoDb(msgText: String) {
        val FromMsgRef = FirebaseDatabase.getInstance().getReference("/UserMessages/" + fromId + toId).push()
        val FromessageData = ChatMessage(FromMsgRef.key!!, msgText, fromId!!, toId)
        FromMsgRef.setValue(FromessageData)
            .addOnSuccessListener {
                Log.d("ChatLog", "Message added successfully")
            }
            .addOnFailureListener {
                Toast.makeText(this@chatlog, it.message, Toast.LENGTH_SHORT).show()
            }
        val toMsgRef = FirebaseDatabase.getInstance().getReference("/UserMessages/" + toId + fromId).push()
        val toMessageData = ChatMessage(toMsgRef.key!!, msgText, fromId!!, toId)
        toMsgRef.setValue(toMessageData)
            .addOnSuccessListener {
                Log.d("ChatLog", "Message added successfully")
            }
            .addOnFailureListener {
                Toast.makeText(this@chatlog, it.message, Toast.LENGTH_SHORT).show()
            }

        val fromLatestMsgsRef = FirebaseDatabase.getInstance().getReference("/latest_msgs/$fromId/$toId")
        val toLatestMsgsRef = FirebaseDatabase.getInstance().getReference("/latest_msgs/$toId/$fromId")
        fromLatestMsgsRef.setValue(FromessageData)
        toLatestMsgsRef.setValue(toMessageData)
    }

}
