package com.example.messnager

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags
import android.util.Log;
import android.widget.Toast;
import com.example.messnager.Models.ChatMessage
import com.google.firebase.database.FirebaseDatabase

class swipeController(var ctx: Context, var chatlist: ArrayList<ChatMessage>, var chatId: String) : Callback() {
    override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {


        return ItemTouchHelper.Callback.makeMovementFlags(0, LEFT or RIGHT)


    }

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false

    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
        var postion = p0.adapterPosition
        var msgid = chatlist[postion].msgId

        val db = FirebaseDatabase.getInstance().getReference("UserMessages")
        db.child(chatId).child(msgid).removeValue()

//      object banya han
//        var log = chatlog()
//        log.chatData.removeAt(postion)
//        log.adapter.notifyDataSetChanged()

        Toast.makeText(ctx, "Deleted ", Toast.LENGTH_LONG).show()
    }


}