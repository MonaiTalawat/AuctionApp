package com.example.narupak.myapplication.viewholder

import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.Auction


/**
 * Created by Narupak on 22/8/2561.
 */
class AuctionViewholder : RecyclerView.ViewHolder,View.OnClickListener {
    override fun onClick(v: View?) {
       Log.d("click" , v.toString())
    }

    var imageview_auction: ImageView
    var cardview_auction : CardView
    var layoutAuction : LinearLayout


    constructor(itemView: View?) : super(itemView) {
        this.imageview_auction = itemView!!.findViewById(R.id.image_auction)
        this.cardview_auction = itemView.findViewById(R.id.cardview_auction)
        this.layoutAuction = itemView.findViewById<LinearLayout>(R.id.layoutAuction)
    }
    fun updateUI(auction : Auction){
        if(auction.status == "1") {
            layoutAuction.setBackgroundResource(R.drawable.gradient3)
        }else if(auction.status == "2"){
            layoutAuction.setBackgroundResource(R.drawable.gradient1)
        }else{
            layoutAuction.setBackgroundResource(R.drawable.gradient2)
        }
        var image = auction.getImgUrl()
        if(!image.equals(null)) {
            var url = image
            val resource = imageview_auction!!.getResources().getIdentifier(url, null, imageview_auction!!.getContext().getPackageName())
            imageview_auction!!.setImageResource(resource)
        }else{
            var url = "drawable/car_default"
            val resource = imageview_auction!!.getResources().getIdentifier(url, null, imageview_auction!!.getContext().getPackageName())
            imageview_auction!!.setImageResource(resource)
        }
    }
}