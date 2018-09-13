package com.example.narupak.myapplication.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.narupak.myapplication.activity.DetailActivity
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.activity.AuctionActivity
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
            }else if(typePage.equals("register")){
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("licenseCarId",more.seq)
                intent.putExtra("user_id",userId)
                holder.itemView.context.startActivity(intent)
            }else{
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("licenseCarId",more.seq)
                intent.putExtra("user_id",userId)
                holder.itemView.context.startActivity(intent)
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