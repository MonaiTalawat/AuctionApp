package com.example.narupak.myapplication.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.Auction

/**
 * Created by Narupak on 23/8/2561.
 */
class MoreViewholder : RecyclerView.ViewHolder{
    var image_more : ImageView

    constructor(itemView: View?) : super(itemView) {
        this.image_more = itemView!!.findViewById(R.id.image_more)
    }

    fun updateUI(auction : Auction){
        var url = auction.getImgUrl()
        val resource = image_more!!.getResources().getIdentifier(url, null, image_more!!.getContext().getPackageName())
        image_more!!.setImageResource(resource)
    }
}