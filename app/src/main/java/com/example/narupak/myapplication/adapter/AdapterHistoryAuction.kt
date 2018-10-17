package com.example.narupak.myapplication.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.activity.DetailHistoryAuctionActivity
import com.example.narupak.myapplication.model.SaveAuctionHistory
import com.example.narupak.myapplication.viewholder.HistoryAuctionViewholder

/**
 * Created by Narupak on 9/10/2561.
 */
class AdapterHistoryAuction : RecyclerView.Adapter<HistoryAuctionViewholder>{

    var historyAuction : ArrayList<SaveAuctionHistory>? = null
    var userId : Int? = null

    constructor(historyAuction: ArrayList<SaveAuctionHistory>?, userId: Int) : super() {
        this.historyAuction = historyAuction
        this.userId = userId
    }


    override fun onBindViewHolder(holder: HistoryAuctionViewholder?, position: Int) {
        var saveAuctionlist = historyAuction!!.get(position)
        holder!!.updateUI(saveAuctionlist)
        holder.cardView_history.setOnClickListener(View.OnClickListener {
            var intent = Intent(holder.itemView.context,DetailHistoryAuctionActivity::class.java)
            intent.putExtra("licenseCarId",saveAuctionlist.seq)
            intent.putExtra("imageLicenseCar",saveAuctionlist.imageLicenseCar)
            intent.putExtra("user_id",userId)
            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context,""+saveAuctionlist.seq,Toast.LENGTH_LONG).show()
        })
    }

    override fun getItemCount(): Int {
        return historyAuction!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryAuctionViewholder {
        val historyAuction : View = LayoutInflater.from(parent!!.context).inflate(R.layout.cardview_history,parent,false)
        return HistoryAuctionViewholder(historyAuction)
    }

}