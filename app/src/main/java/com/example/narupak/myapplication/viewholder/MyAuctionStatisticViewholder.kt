package com.example.narupak.myapplication.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.Statistic

/**
 * Created by Narupak on 18/10/2561.
 */
class MyAuctionStatisticViewholder : RecyclerView.ViewHolder{
    var countAution : TextView? = null
    var typeAuction : TextView? = null

    constructor(itemView: View?) : super(itemView) {
        this.countAution = itemView!!.findViewById(R.id.countLicenseCar)
        this.typeAuction = itemView.findViewById(R.id.typeAuction)
    }

    fun setUI(statistic : Statistic){
        countAution!!.text = statistic.countAuction.toString()
        typeAuction!!.text = statistic.typeAuction
    }
}