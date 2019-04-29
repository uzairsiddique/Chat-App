package com.example.messnager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.messnager.Adaptter.groupListAdapter
import com.example.messnager.Models.GroupChatMessage
import com.example.messnager.Models.GroupChatModal
import com.google.firebase.auth.FirebaseAuth

class GroupChatLog : AppCompatActivity() {

    lateinit var groupChat: Array<GroupChatMessage>
    lateinit var adapter: groupListAdapter
    lateinit var groupUser: GroupChatModal

    var fromId = FirebaseAuth.getInstance().uid
    var toId: String = GroupChat_recyclerView.toUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_log)




    }
}
