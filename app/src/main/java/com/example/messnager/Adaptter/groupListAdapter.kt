package com.example.messnager.Adaptter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.messnager.Models.GroupChatModal
import com.example.messnager.R
import kotlinx.android.synthetic.main.row_from_msg.view.*

class groupListAdapter(
    var ctx: Context, var userData: ArrayList<GroupChatModal>, var viewClick: (view: View, postion: Int) -> Unit
) : RecyclerView.Adapter<groupListAdapter.customerViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): customerViewHolder {

        val view = LayoutInflater.from(ctx).inflate(R.layout.groupchat_recycler_layout, null)
        return customerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userData.size

    }

    override fun onBindViewHolder(holder: customerViewHolder, position: Int) {

        holder.groupname?.text = userData[position].grp_name
        holder.cardView?.setOnClickListener {

            viewClick(it, position)

        }

    }


    inner class customerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val groupname: TextView? = view.findViewById(R.id.Username_recyclerView_GroupMsgActivty)
        val cardView: CardView? = view.findViewById(R.id.group_card_view)

    }
}