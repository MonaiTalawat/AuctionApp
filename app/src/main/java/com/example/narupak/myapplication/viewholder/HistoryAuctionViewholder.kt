package com.example.narupak.myapplication.viewholder

import android.os.Environment
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.drawable.hammer
import com.example.narupak.myapplication.R.id.*
import com.example.narupak.myapplication.model.SaveAuctionHistory
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Narupak on 9/10/2561.
 */
class HistoryAuctionViewholder : RecyclerView.ViewHolder {
    var imageHistory : ImageView
    var finalDateAuction : TextView
    var finalPrice : TextView
    var userWinner : TextView
    var cardView_history : ConstraintLayout

    constructor(itemView: View?) : super(itemView) {
        this.imageHistory = itemView!!.findViewById(R.id.imageHistory)
        this.finalDateAuction = itemView!!.findViewById(R.id.finalDateAuction)
        this.finalPrice = itemView!!.findViewById(R.id.finalPrice)
        this.userWinner = itemView!!.findViewById(R.id.userWinner)
        this.cardView_history = itemView!!.findViewById(R.id.cardview_history)
    }



    fun updateUI(saveAuction: SaveAuctionHistory, holder: HistoryAuctionViewholder?){
        //imageHistory = saveAuction.
        val numberLicenseCar = "เลขทะเบียน "+saveAuction.numberLicenseCar
        val finalPrices = saveAuction.finalprice.toString()
        val userWinners = "ผู้ชนะคือ "+saveAuction.firstName+" "+saveAuction.lastName
        val date = saveAuction.endAuctionDate
        val fmt = SimpleDateFormat("d MMMM พ.ศ. yyyy", Locale("th", "th"))

        if(!saveAuction.imageLicenseCar.equals(null)) {
            val image = saveAuction.imageLicenseCar
            val resource = imageHistory!!.getResources().getIdentifier("drawable/" + image, null, imageHistory!!.getContext().getPackageName())
            imageHistory!!.setImageResource(resource)
        }else{
            val image = "car_default"
            val resource = imageHistory!!.getResources().getIdentifier("drawable/" + image, null, imageHistory!!.getContext().getPackageName())
            imageHistory!!.setImageResource(resource)
        }
        //Picasso.with(holder!!.itemView.context).load(R.drawable).placeholder(R.drawable.hammer).into(imageHistory);
        //val date = Instant.ofEpochMilli(saveAuction.endAuctionDate!!).atZone(ZoneId.systemDefault()).toLocalDate()
        val finalDateAuctions = fmt.format(date)
        finalDateAuction.text = finalDateAuctions.toString()
        finalPrice.text = finalPrices
        userWinner.text = userWinners
    }
}