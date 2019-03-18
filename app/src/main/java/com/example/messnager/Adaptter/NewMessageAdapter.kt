package com.example.messnager.Adaptter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.messnager.Models.Users
import com.example.messnager.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class NewMessageAdapter(
    var ctx: Context,
    var userData: ArrayList<Users>,
    var viewClick: (view: View, position: Int) -> Unit
) :
    RecyclerView.Adapter<NewMessageAdapter.customViewHoler>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): customViewHoler {
        val view = LayoutInflater.from(ctx).inflate(R.layout.latest_msg_row, null)
        return customViewHoler(view)
    }

    override fun getItemCount(): Int {
        return userData.size
    }

    override fun onBindViewHolder(holder: customViewHoler, position: Int) {
        holder.userName?.text = userData[position].username
        try {
            Picasso.get().load(userData[position].profileImageurl).into(holder.userImage)
        } catch (e: Exception) {
            e.message
        }


        holder.cardView?.setOnClickListener {
            viewClick(it, position)
        }

    }


    inner class customViewHoler(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView? = view.findViewById(R.id.userName_tv_latestMessage)
        val userImage: CircleImageView? = view.findViewById(R.id.image_latestMsg)
        val cardView: CardView? = view.findViewById(R.id.latestMsg_view)
    }
}