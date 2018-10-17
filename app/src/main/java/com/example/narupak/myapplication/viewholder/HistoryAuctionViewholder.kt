package com.example.narupak.myapplication.viewholder

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.auction
import com.example.narupak.myapplication.R.id.text
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.model.SaveAuctionHistory
import com.example.narupak.myapplication.model.WinnerAuction
import kotlinx.android.synthetic.main.cardview_history.view.*
import org.w3c.dom.Text
import java.time.ZoneId.systemDefault
import java.time.Instant
import java.time.Instant.ofEpochMilli
import java.time.ZoneId
import java.text.SimpleDateFormat
import java.time.LocalDateTime
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



    fun updateUI(saveAuction : SaveAuctionHistory){
        //imageHistory = saveAuction.
        val numberLicenseCar = "เลขทะเบียน "+saveAuction.numberLicenseCar
        val finalPrices = saveAuction.finalprice.toString()
        val userWinners = "ผู้ชนะคือ "+saveAuction.firstName+" "+saveAuction.lastName
        val date = saveAuction.endAuctionDate
        val fmt = SimpleDateFormat("d MMMM พ.ศ. yyyy", Locale("th", "th"))

        //val date = Instant.ofEpochMilli(saveAuction.endAuctionDate!!).atZone(ZoneId.systemDefault()).toLocalDate()
        val finalDateAuctions = fmt.format(date)
        finalDateAuction.text = finalDateAuctions.toString()
        finalPrice.text = finalPrices
        userWinner.text = userWinners
    }
}