package com.example.narupak.myapplication.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.activity.DetailActivity
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.user
import com.example.narupak.myapplication.activity.AuctionActivity
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.model.LicenseCar
import com.example.narupak.myapplication.model.User
import com.example.narupak.myapplication.service.ApiInterface
import com.example.narupak.myapplication.viewholder.AuctionViewholder
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/**
 * Created by Narupak on 22/8/2561.
 */
class AuctionAdapter : RecyclerView.Adapter<AuctionViewholder> {
    var auction : ArrayList<Auction>? = null
    var type_page : String? = null
    var userId : Int? = null
    var status : String? = null

    constructor(auction: ArrayList<Auction>?, type_page: String?, userId: Int?, status: String?) : super() {
        this.auction = auction
        this.type_page = type_page
        this.userId = userId
        this.status = status
    }


    override fun getItemCount(): Int {
        return auction!!.size
    }

    override fun onBindViewHolder(holder: AuctionViewholder?, position: Int) {
        var auctions = auction!!.get(position)
        holder!!.updateUI(auctions)
        holder.imageview_auction.setOnClickListener(View.OnClickListener {
            if(type_page == "auction"){
                var intent = Intent(holder.itemView.context, AuctionActivity::class.java)
                intent.putExtra("licenseCarId",auctions.seq)
                intent.putExtra("user_id",userId)
                intent.putExtra("typePage","auction")
                intent.putExtra("firstprice",auctions.firstpriceLicenseCar)
                intent.putExtra("image",auctions.imageLicenseCar)
                holder.itemView.context.startActivity(intent)
            }
            if(type_page == "register") {
                var intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("licenseCarId",auctions.seq)
                intent.putExtra("user_id",userId)
                intent.putExtra("typePage","registerAuction")
                holder.itemView.context.startActivity(intent)
            }
            if(type_page == "myauction"){
                if(auctions.status!! == "1") {
                    var intent = Intent(holder.itemView.context, DetailActivity::class.java)
                    intent.putExtra("licenseCarId", auctions.seq)
                    intent.putExtra("user_id", userId)
                    intent.putExtra("typePage", "myauction")
                    intent.putExtra("status", status)
                    holder.itemView.context.startActivity(intent)
                }else{
                    var intent = Intent(holder.itemView.context, AuctionActivity::class.java)
                    intent.putExtra("licenseCarId", auctions.seq)
                    intent.putExtra("user_id", userId)
                    intent.putExtra("typePage", "myauction")
                    intent.putExtra("image",auctions.imageLicenseCar)
                    intent.putExtra("status", status)
                    holder.itemView.context.startActivity(intent)
                }

            }
        })

    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AuctionViewholder {
        val auction : View = LayoutInflater.from(parent!!.context).inflate(R.layout.auction_cardview,parent,false)
        return AuctionViewholder(auction)
    }
//    fun onclick(itemview : MenuView.ItemView){
//        Log.d("menu" , itemview.toString())
//    }

}