package com.example.narupak.myapplication.adapter

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.narupak.myapplication.activity.DetailActivity
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.activity.AuctionActivity
import com.example.narupak.myapplication.activity.DetailHistoryAuctionActivity
import com.example.narupak.myapplication.activity.MainMenuActivity
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.service.ApiInterface
import com.example.narupak.myapplication.viewholder.AuctionViewholder
import retrofit2.Call
import retrofit2.Response
import android.app.Activity



/**
 * Created by Narupak on 22/8/2561.
 */
class AuctionAdapter : RecyclerView.Adapter<AuctionViewholder> {
    var auction : ArrayList<Auction>? = null
    var type_page : String? = null
    var userId : Int? = null
    var status : Long? = null

    constructor(auction: ArrayList<Auction>?, type_page: String?, userId: Int?, status: Long?) : super() {
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
                callWebServiceForcheckStatusLicenseCarByLicenseCarId(auctions,holder)
            }
            if(type_page == "register") {
                var intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("licenseCarId",auctions.seq)
                intent.putExtra("user_id",userId)
                intent.putExtra("typePage","registerAuction")
                holder.itemView.context.startActivity(intent)
                (holder.itemView.context as MainMenuActivity).finish()
            }
        })

    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AuctionViewholder {
        val auction : View = LayoutInflater.from(parent!!.context).inflate(R.layout.auction_cardview,parent,false)
        return AuctionViewholder(auction)
    }

    fun callWebServiceForcheckStatusLicenseCarByLicenseCarId(auctions : Auction, holder: AuctionViewholder?) {
        var apiService = ApiInterface.create()
        var call = apiService.checkStatusLicenseCarByLicenseCarId(auctions.seq!!)
        call.enqueue(object : retrofit2.Callback<Long>{
            override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                val status = response!!.body()
                if(status == 3L){
                    // Initialize a new instance of
                    val builder = AlertDialog.Builder(holder!!.itemView.context)
                    // Set the alert dialog title
                    builder.setTitle("แจ้งเตือน")
                    // Display a message on alert dialog
                    builder.setMessage("เลขทะเบียนนี้สิ้นสุดการประมูลแล้ว")
                    // Set a positive button and its click listener on alert dialog
                    builder.setPositiveButton("ปิด") { dialog, which ->
                        var intent = Intent(holder.itemView.context, MainMenuActivity::class.java)
                        intent.putExtra("user_id", userId)
                        holder.itemView.context.startActivity(intent)
                    }

                    // Finally, make the alert dialog using builder
                    val dialog: AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()
                }else {
                    // Initialize a new instance of
                    val builder = AlertDialog.Builder(holder!!.itemView.context)
                    // Set the alert dialog title
                    builder.setTitle("แจ้งเตือน")
                    // Display a message on alert dialog
                    builder.setMessage("คุณต้องการเข้าร่วมการประมูลใช่หรือไม่")
                    // Set a positive button and its click listener on alert dialog
                    builder.setPositiveButton("ใช่") { dialog, which ->
                        // Do something when user press the positive button
                        Toast.makeText(holder!!.itemView.context, "ขอให้ได้ขอให้โดน", Toast.LENGTH_SHORT).show()
                        var intent = Intent(holder.itemView.context, AuctionActivity::class.java)
                        intent.putExtra("licenseCarId", auctions.seq!!)
                        intent.putExtra("user_id", userId)
                        intent.putExtra("typePage", "auction")
                        intent.putExtra("firstprice", auctions.firstpriceLicenseCar)
                        intent.putExtra("image", auctions.imageLicenseCar)
                        holder.itemView.context.startActivity(intent)
                        (holder.itemView.context as MainMenuActivity).finish()

                    }
                    // Display a negative button on alert dialog
                    builder.setNegativeButton("ไม่") { dialog, which ->
                        Toast.makeText(holder.itemView.context, "เสียดายจัง", Toast.LENGTH_SHORT).show()
                    }

                    // Finally, make the alert dialog using builder
                    val dialog: AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()
                }
            }

            override fun onFailure(call: Call<Long>?, t: Throwable?) {
            }



        })
    }

}