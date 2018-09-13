package com.example.narupak.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.notification
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.model.AuctionRealtimeDatabase
import com.example.narupak.myapplication.viewholder.AuctionRealtimeViewholder
import com.example.narupak.myapplication.viewholder.NotiViewholder

/**
 * Created by Narupak on 4/9/2561.
 */
class AdapterAuctionRealtime : RecyclerView.Adapter<AuctionRealtimeViewholder> {

    var auctionRealtime : ArrayList<AuctionRealtimeDatabase>? = null

    constructor(auctionRealtime: ArrayList<AuctionRealtimeDatabase>) : super() {
        this.auctionRealtime = auctionRealtime
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AuctionRealtimeViewholder {
        val auctionRealtime : View = LayoutInflater.from(parent!!.context).inflate(R.layout.cardview_auction,parent,false)
        return AuctionRealtimeViewholder(auctionRealtime)
    }

    override fun getItemCount(): Int {
        return auctionRealtime!!.size
    }

    override fun onBindViewHolder(holder: AuctionRealtimeViewholder?, position: Int) {
        var auctionRealtimeList = auctionRealtime!!.get(position)
        holder!!.updateUI(auctionRealtimeList)
    }

}