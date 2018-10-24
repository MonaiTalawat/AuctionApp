package com.example.narupak.myapplication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.Statistic
import com.example.narupak.myapplication.viewholder.AuctionRealtimeViewholder
import com.example.narupak.myapplication.viewholder.HistoryAuctionViewholder
import com.example.narupak.myapplication.viewholder.MyAuctionStatisticViewholder

/**
 * Created by Narupak on 18/10/2561.
 */
class AdapterMyAuctionStatistic  : RecyclerView.Adapter<MyAuctionStatisticViewholder>{

    var statisticList = ArrayList<Statistic>()

    constructor(statisticList: ArrayList<Statistic>) : super() {
        this.statisticList = statisticList
    }

    override fun getItemCount(): Int {
        return statisticList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyAuctionStatisticViewholder {
        val statisticAuction : View = LayoutInflater.from(parent!!.context).inflate(R.layout.cardview_myauctionmain,parent,false)
        return MyAuctionStatisticViewholder(statisticAuction)
    }

    override fun onBindViewHolder(holder: MyAuctionStatisticViewholder?, position: Int) {
        var statistics = statisticList.get(position)
        holder!!.setUI(statistics)
    }


}