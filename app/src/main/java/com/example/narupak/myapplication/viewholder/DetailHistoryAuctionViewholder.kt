package com.example.narupak.myapplication.viewholder

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.user
import com.example.narupak.myapplication.model.SaveAuctionHistory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_history_auction.view.*
import java.text.SimpleDateFormat
import java.util.*
/**
 * Created by Narupak on 12/10/2561.
 */
class DetailHistoryAuctionViewholder : RecyclerView.ViewHolder{
    var detailFinalDateAuction : TextView
    var detailFinalPrice : TextView
    var detailuser : TextView
    var detailConstraint : ConstraintLayout


    constructor(itemView: View?) : super(itemView) {
        this.detailFinalDateAuction = itemView!!.findViewById(R.id.textTimeDetailHistory)
        this.detailFinalPrice = itemView.findViewById(R.id.textPriceDetailHistory)
        this.detailuser = itemView.findViewById(R.id.textUserDetailHistory)
        this.detailConstraint = itemView.findViewById(R.id.constraintDetail)
    }


    @SuppressLint("ResourceAsColor")
    fun updateUI(detailHistoryAuction: SaveAuctionHistory, userId: Long?){
        val date = detailHistoryAuction.endAuctionDate!!.toLong()
        val fmt = SimpleDateFormat("d MMMM พ.ศ. yyyy", Locale("th", "th"))
        val detailDate = fmt.format(date)
        detailFinalDateAuction.text = detailDate.toString()
        detailFinalPrice.text = detailHistoryAuction.finalprice.toString()
        var detailHistoryAuctionUser = detailHistoryAuction.firstName+" "+detailHistoryAuction.lastName
        detailuser.text = detailHistoryAuctionUser
        if(detailHistoryAuction.id == userId){
            detailConstraint.setBackgroundColor(Color.parseColor("#86ff8aca"))

            //Picasso.with(mContext).load().into(detailFinalDateAuction.imageViewDetailHistory);
        }else{
            detailConstraint.setBackgroundColor(Color.parseColor("#8294ff99"))
        }
    }

}