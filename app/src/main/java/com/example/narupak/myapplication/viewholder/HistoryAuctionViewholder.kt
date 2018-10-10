package com.example.narupak.myapplication.viewholder

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

/**
 * Created by Narupak on 9/10/2561.
 */
class HistoryAuctionViewholder : RecyclerView.ViewHolder {
    var imageHistory : ImageView
    var textLicenseCar : TextView
    var finalDateAuction : TextView
    var finalPrice : TextView
    var userWinner : TextView

    constructor(itemView: View?) : super(itemView) {
        this.imageHistory = itemView!!.findViewById(R.id.imageHistory)
        this.textLicenseCar = itemView!!.findViewById(R.id.textLicenseCar)
        this.finalDateAuction = itemView!!.findViewById(R.id.finalDateAuction)
        this.finalPrice = itemView!!.findViewById(R.id.finalPrice)
        this.userWinner = itemView!!.findViewById(R.id.userWinner)
    }


    fun updateUI(saveAuction : SaveAuctionHistory){
        //imageHistory = saveAuction.
        textLicenseCar.text = saveAuction.numberLicenseCar
        finalDateAuction.text = saveAuction.endAuctionDate.toString()
        finalPrice.text = saveAuction.finalprice.toString()
        userWinner.text = saveAuction.firstName
    }
}