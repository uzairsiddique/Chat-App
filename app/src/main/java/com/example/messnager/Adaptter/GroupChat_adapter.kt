package com.example.messnager.Adaptter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.messnager.GroupChat_recyclerView
import com.example.messnager.Models.Users
import com.example.messnager.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class GroupChat_adapter(
    var ctx: Context,
    var userData: ArrayList<Users>,
    var viewclick: (view: View, positon: Int) -> Unit
) : RecyclerView.Adapter<GroupChat_adapter.cutomViewHolder>() {
    var count = 0
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GroupChat_adapter.cutomViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.groupchat_recycler_layout, null)

        return cutomViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userData.size
    }

    override fun onBindViewHolder(holder: cutomViewHolder, positon: Int) {
        GroupChat_recyclerView.group_member_id.clear()

        holder.userName?.text = userData[positon].username
        try {
            Picasso.get().load(userData[positon].profileImageurl).into(holder.userImage)
        } catch (e: Exception) {
            e.message
        }

        holder.cardView?.setOnLongClickListener {
            val uid = FirebaseAuth.getInstance().uid ?: ""
            if (GroupChat_recyclerView.group_member_id.size == 0)
                GroupChat_recyclerView.group_member_id.add(GroupChat_recyclerView.fromUser!!.uid)
//                GroupChat_recyclerView.group_member_id.put(count.toString(), uid)


            holder.icon.visibility = View.VISIBLE

            count++
//            GroupChat_recyclerView.group_member_id.put(count.toString(), userData[positon].uid)
            GroupChat_recyclerView.group_member_id.add(userData[positon].uid)

            Toast.makeText(ctx, "click on this", Toast.LENGTH_LONG).show()



            return@setOnLongClickListener true

        }
    }


    inner class cutomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView? = view.findViewById(R.id.Username_recyclerView_GroupMsgActivty)
        val userImage: CircleImageView? = view.findViewById(R.id.user_image__GroupMsgActivity)
        val cardView: CardView? = view.findViewById(R.id.group_card_view)
        val icon: ImageView = view.findViewById(R.id.check_icon)


    }


}
