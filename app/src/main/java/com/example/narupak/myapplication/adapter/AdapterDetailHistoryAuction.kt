package com.example.narupak.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.SaveAuctionHistory
import com.example.narupak.myapplication.viewholder.DetailHistoryAuctionViewholder

/**
 * Created by Narupak on 12/10/2561.
 */
class AdapterDetailHistoryAuction : RecyclerView.Adapter<DetailHistoryAuctionViewholder> {

    var detailHistoryAuctionList : ArrayList<SaveAuctionHistory>? = null
    var userId : Long? = null

    constructor(detailHistoryAuctionList: ArrayList<SaveAuctionHistory>?, userId: Long) : super() {
        this.detailHistoryAuctionList = detailHistoryAuctionList
        this.userId = userId
    }

    override fun onBindViewHolder(holder: DetailHistoryAuctionViewholder?, position: Int) {
        var detailHistoryAuction = detailHistoryAuctionList!![position]
        holder!!.updateUI(detailHistoryAuction,userId)
    }

    override fun getItemCount(): Int {
        return detailHistoryAuctionList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DetailHistoryAuctionViewholder {
        val detailHistoryAuction : View = LayoutInflater.from(parent!!.context).inflate(R.layout.cardview_detail_history,parent,false)
        return DetailHistoryAuctionViewholder(detailHistoryAuction)
    }
}