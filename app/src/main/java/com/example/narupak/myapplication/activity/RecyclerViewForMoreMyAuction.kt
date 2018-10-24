package com.example.narupak.myapplication.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.MoreAdapter
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.model.RegisterAuctionLicenseCar
import com.example.narupak.myapplication.model.User
import com.example.narupak.myapplication.service.ApiInterface
import kotlinx.android.synthetic.main.activity_recycler_view_for_more.*
import kotlinx.android.synthetic.main.activity_recycler_view_for_my_auction.*
import retrofit2.Call
import retrofit2.Response

class RecyclerViewForMoreMyAuction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_for_my_auction)
        var bundle = intent.extras
        //Toast.makeText(applicationContext,bundle.getString("name"),Toast.LENGTH_LONG).show()
        var userId = bundle.getInt("user_id")
        //callWebserviceForMyAuctionRecyclerView(this,userId)
        nestedScrollForMoreMyAuction.setBackgroundColor(Color.parseColor("#3fff00a6"))
    }



    override fun onBackPressed() {
        val bundle = intent.extras
        var userId = bundle.getInt("user_id")
        var intent = Intent(baseContext,MainMenuActivity::class.java)
        intent.putExtra("user_id",userId)
        startActivity(intent)
        finish()
    }
}
