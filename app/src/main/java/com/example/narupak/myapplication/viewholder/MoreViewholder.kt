package com.example.narupak.myapplication.viewholder

import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.auction
import com.example.narupak.myapplication.R.id.layoutAuction
import com.example.narupak.myapplication.model.Auction

/**
 * Created by Narupak on 23/8/2561.
 */
class MoreViewholder : RecyclerView.ViewHolder{
    var image_more : ImageView
    var constraint_more : ConstraintLayout

    constructor(itemView: View?) : super(itemView) {
        this.image_more = itemView!!.findViewById(R.id.image_more)
        this.constraint_more = itemView.findViewById(R.id.constraint_more)
    }

    fun updateUI(auction : Auction){
        if(auction.status!!.equals("1")){
            constraint_more.setBackgroundResource(R.drawable.gradient3)
        }else if(auction.status!!.equals("2")){
            constraint_more.setBackgroundResource(R.drawable.gradient1)
        }else{
            constraint_more.setBackgroundResource(R.drawable.gradient2)
        }
        var url = auction.getImgUrl()
        if(!url.equals(null)) {
            url = auction.getImgUrl()
            val resource = image_more!!.getResources().getIdentifier(url, null, image_more!!.getContext().getPackageName())
            image_more!!.setImageResource(resource)
        }else{
            url = "drawable/car_default"
            val resource = image_more!!.getResources().getIdentifier(url, null, image_more!!.getContext().getPackageName())
            image_more!!.setImageResource(resource)
        }
    }
}