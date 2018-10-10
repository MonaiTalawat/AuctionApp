package com.example.narupak.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.model.SaveAuctionHistory
import com.example.narupak.myapplication.model.WinnerAuction
import com.example.narupak.myapplication.viewholder.AuctionRealtimeViewholder
import com.example.narupak.myapplication.viewholder.HistoryAuctionViewholder
import com.example.narupak.myapplication.viewholder.NotiViewholder

/**
 * Created by Narupak on 9/10/2561.
 */
class AdapterHistoryAuction : RecyclerView.Adapter<HistoryAuctionViewholder>{

    var historyAuction : ArrayList<SaveAuctionHistory>? = null

    constructor(historyAuction: ArrayList<SaveAuctionHistory>?) : super() {
        this.historyAuction = historyAuction
    }


    override fun onBindViewHolder(holder: HistoryAuctionViewholder?, position: Int) {
        var saveAuctionlist = historyAuction!!.get(position)
        holder!!.updateUI(saveAuctionlist)
    }

    override fun getItemCount(): Int {
        return historyAuction!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryAuctionViewholder {
        val historyAuction : View = LayoutInflater.from(parent!!.context).inflate(R.layout.cardview_history,parent,false)
        return HistoryAuctionViewholder(historyAuction)
    }

}