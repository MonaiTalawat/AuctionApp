package com.example.narupak.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.AdapterDetailHistoryAuction
import com.example.narupak.myapplication.model.DetailHistoryAuction
import com.example.narupak.myapplication.model.SaveAuctionHistory
import com.example.narupak.myapplication.service.ApiInterface
import retrofit2.Call
import retrofit2.Response

class DetailHistoryAuctionActivity : AppCompatActivity() {

    var AdapterDetailHistoryAuction : AdapterDetailHistoryAuction? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history_auction)

        var bundle = intent.extras
        var licenseCarId = bundle.getLong("licenseCarId")
        var imageLicenseCar = bundle.getString("imageLicenseCar")
        var userId = bundle.getInt("user_id")
        callWebServiceForDetailHistoryAuction(licenseCarId,userId)

    }

    fun callWebServiceForDetailHistoryAuction(licenseCarId: Long, userId: Int){
        val apiService = ApiInterface.create()
        val call = apiService.queryHistoryAuctionByLicenseCarId(licenseCarId)
        val auctionHistoryList = ArrayList<SaveAuctionHistory>()
        call.enqueue(object : retrofit2.Callback<List<DetailHistoryAuction>> {
            override fun onResponse(call: Call<List<DetailHistoryAuction>>?, response: Response<List<DetailHistoryAuction>>?) {
                if(response!!.code() == 200) {
                    var history = response.body()
                    //Log.d("Detailhistory",history.toString())
                    for(histories in history ){
                        Log.d("Detailhistory",histories.toString())
                        var auctionHistory = SaveAuctionHistory()
                        val userVM = histories.userVM
                        auctionHistory.firstName = userVM!!.firstname
                        auctionHistory.lastName = userVM.lastname
                        auctionHistory.finalprice = histories.auctionPrice
                        auctionHistory.endAuctionDate = histories.auctionTime
                        auctionHistory.id = userVM.id!!.toLong()
                        auctionHistoryList.add(auctionHistory)
                    }
                    val RecyclerViewForDetailHistory = findViewById<View>(R.id.recyclerViewDetailHistory) as RecyclerView
                    val linearLayoutManager = LinearLayoutManager(this@DetailHistoryAuctionActivity)
                    AdapterDetailHistoryAuction = AdapterDetailHistoryAuction(auctionHistoryList,userId.toLong())
                    RecyclerViewForDetailHistory!!.adapter = AdapterDetailHistoryAuction
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    RecyclerViewForDetailHistory.layoutManager = linearLayoutManager
                }else{
                    Log.d("failed","failed")
                }
            }
            override fun onFailure(call: Call<List<DetailHistoryAuction>>?, t: Throwable?) {

            }

        })
    }
}
