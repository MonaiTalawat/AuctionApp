package com.example.narupak.myapplication.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.image_more
import com.example.narupak.myapplication.model.Auction

/**
 * Created by Narupak on 24/8/2561.
 */
class NotiViewholder : RecyclerView.ViewHolder{
    var image_noti : ImageView

    constructor(itemView: View?) : super(itemView) {
        this.image_noti = itemView!!.findViewById(R.id.image_noti)
    }

    fun updateUI_noti(auction : Auction){
        var url = auction.getImgUrl()
        val resource = image_noti.getResources().getIdentifier(url, null, image_noti.getContext().getPackageName())
        image_noti.setImageResource(resource)
    }

}