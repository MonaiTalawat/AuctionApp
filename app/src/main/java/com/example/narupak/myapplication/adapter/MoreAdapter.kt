package com.example.narupak.myapplication.adapter

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.activity.*
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.viewholder.MoreViewholder

/**
 * Created by Narupak on 23/8/2561.
 */
class MoreAdapter : RecyclerView.Adapter<MoreViewholder> {


    var moreList : ArrayList<Auction>? = null
    var typePage : String? = null
    var userId : Int? = null

    constructor(moreList: ArrayList<Auction>?, typePage: String?, userId: Int?) : super() {
        this.moreList = moreList
        this.typePage = typePage
        this.userId = userId
    }


    override fun onBindViewHolder(holder: MoreViewholder?, position: Int) {
        var more = moreList!!.get(position)
        holder!!.updateUI(more)
        holder.image_more.setOnClickListener(View.OnClickListener {
            if(typePage.equals("auction")){
                val intent = Intent(holder.itemView.context, AuctionActivity::class.java)
                intent.putExtra("licenseCarId",more.seq)
                intent.putExtra("user_id",userId)
                intent.putExtra("image",more.imageLicenseCar)
                holder.itemView.context.startActivity(intent)
                (holder.itemView.context as RecyclerViewForMoreActivity).finish()
            }else if(typePage.equals("register")){
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("licenseCarId",more.seq)
                intent.putExtra("user_id",userId)
                holder.itemView.context.startActivity(intent)
                (holder.itemView.context as RecyclerViewForMoreActivity).finish()
            }else{
                if(more.status.equals("1")) {
                    val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                    intent.putExtra("licenseCarId", more.seq)
                    intent.putExtra("user_id", userId)
                    intent.putExtra("typePage", "myauction")
                    intent.putExtra("imageLicenseCar",more.imageLicenseCar)
                    intent.putExtra("status", more.status)
                    holder.itemView.context.startActivity(intent)
                    (holder.itemView.context as ActionBarTabActivity).finish()
                }else if(more.status.equals("2")){
                    // Initialize a new instance of
                    val builder = AlertDialog.Builder(holder!!.itemView.context)
                    // Set the alert dialog title
                    builder.setTitle("Warnning Auction")
                    // Display a message on alert dialog
                    builder.setMessage("Are you want to Auction LicenseCar?")
                    // Set a positive button and its click listener on alert dialog
                    builder.setPositiveButton("YES") { dialog, which ->
                        // Do something when user press the positive button
                        Toast.makeText(holder!!.itemView.context, "Ok, we change the app background.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(holder.itemView.context, AuctionActivity::class.java)
                        intent.putExtra("licenseCarId", more.seq)
                        intent.putExtra("user_id", userId)
                        intent.putExtra("typePage", "myauction")
                        intent.putExtra("image",more.imageLicenseCar)
                        intent.putExtra("status", more.status)
                        holder.itemView.context.startActivity(intent)
                        (holder.itemView.context as ActionBarTabActivity).finish()
                    }
                    // Display a negative button on alert dialog
                    builder.setNegativeButton("No") { dialog, which ->
                        Toast.makeText(holder.itemView.context, "You are not agree.", Toast.LENGTH_SHORT).show()
                    }

                    // Finally, make the alert dialog using builder
                    val dialog: AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()
                }else{
                    val intent = Intent(holder.itemView.context, DetailHistoryAuctionActivity::class.java)
                    intent.putExtra("licenseCarId", more.seq)
                    intent.putExtra("user_id", userId)
                    intent.putExtra("typePage", "myauction")
                    intent.putExtra("imageLicenseCar",more.imageLicenseCar)
                    intent.putExtra("status", more.status)
                    holder.itemView.context.startActivity(intent)
                    (holder.itemView.context as ActionBarTabActivity).finish()
                }
            }
        })
    }



    override fun getItemCount(): Int {
        return moreList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MoreViewholder {
        val more : View = LayoutInflater.from(parent!!.context).inflate(R.layout.cardview_more,parent,false)
        return MoreViewholder(more)
    }

}