package com.example.narupak.myapplication.adapter

import android.app.Notification
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.auction
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.viewholder.AuctionViewholder
import com.example.narupak.myapplication.viewholder.NotiViewholder

/**
 * Created by Narupak on 24/8/2561.
 */
class AdapterNoti : RecyclerView.Adapter<NotiViewholder> {
    var notification : ArrayList<Auction>? = null

    constructor(notification: ArrayList<Auction>?) : super() {
        this.notification = notification
    }

    override fun onBindViewHolder(holder: NotiViewholder?, position: Int) {
        var notification = notification!!.get(position)
        holder!!.updateUI_noti(notification)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NotiViewholder {
        val notification : View = LayoutInflater.from(parent!!.context).inflate(R.layout.cardview_noti,parent,false)
        return NotiViewholder(notification)
    }

    override fun getItemCount(): Int {
        return notification!!.size
    }

}