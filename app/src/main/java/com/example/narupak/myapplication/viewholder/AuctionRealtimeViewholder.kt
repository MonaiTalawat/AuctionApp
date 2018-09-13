package com.example.narupak.myapplication.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.finalPrice
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.model.AuctionRealtimeDatabase

/**
 * Created by Narupak on 5/9/2561.
 */
class AuctionRealtimeViewholder : RecyclerView.ViewHolder{

    var cardview_auction : CardView
    var bidder : TextView
    var bidTime : TextView
    var Price : TextView

        constructor(itemView: View?) : super(itemView) {
            this.bidder = itemView!!.findViewById(R.id.name_auction)
            this.bidTime = itemView.findViewById(R.id.date_auction)
            this.Price = itemView.findViewById(R.id.amount_auction)
            this.cardview_auction = itemView.findViewById(R.id.cardview_auction)

        }
        fun updateUI(auctionRealtimeList : AuctionRealtimeDatabase){
            bidder.text = auctionRealtimeList.bidder
            bidTime.text = auctionRealtimeList.bidtime.toString()
            Price.text = auctionRealtimeList.price.toString()
        }
}