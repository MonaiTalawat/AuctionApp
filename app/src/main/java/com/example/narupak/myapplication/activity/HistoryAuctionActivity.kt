package com.example.narupak.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.AdapterAuctionRealtime
import com.example.narupak.myapplication.adapter.AdapterHistoryAuction
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response

class HistoryAuctionActivity : AppCompatActivity() {

    var saveAuctionHistoryList = ArrayList<SaveAuctionHistory>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_auction)
        var bundle = intent.extras
        var userId = bundle.getInt("user_id")

        callWebServiceForHistory(userId.toLong())
    }

    fun callWebServiceForHistory(userId: Long){
        val apiService = ApiInterface.create()
        val call = apiService.queryHistoryByUserId(userId)
        var saveList : List<SaveAuctionHistory>? = null
        call.enqueue(object : retrofit2.Callback<List<Long>> {
            override fun onResponse(call: Call<List<Long>>?, response: Response<List<Long>>?) {
                if(response!!.code() == 200) {
                    var history = response.body()
                    Log.d("userId",history.toString())
                    for(licenseCarId in history){
                        Log.d("userId",licenseCarId.toString())
                        saveList = callWebServiceForQuerySaveAuction(licenseCarId)

                    }
                    Log.d("queryHistoryByUserId","queryHistoryByUserId")
                    val RecyclerViewForHistory = findViewById<View>(R.id.recyclerViewHistoryAuction) as RecyclerView
                    val linearLayoutManager = LinearLayoutManager(this@HistoryAuctionActivity)
                    val AdapterHistoryAuction = AdapterHistoryAuction(saveAuctionHistoryList)
                    RecyclerViewForHistory!!.adapter = AdapterHistoryAuction
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    RecyclerViewForHistory.layoutManager = linearLayoutManager
                }else{
                    Log.d("failed","failed")
                }
            }
            override fun onFailure(call: Call<List<Long>>?, t: Throwable?) {

            }

        })
    }

    fun callWebServiceForQuerySaveAuction(licenseCarId : Long) : List<SaveAuctionHistory>{
        val apiService = ApiInterface.create()
        val call = apiService.querySaveAuctionByLicenseCarId(licenseCarId)
        call.enqueue(object : retrofit2.Callback<List<SaveAuction>> {
            override fun onResponse(call: Call<List<SaveAuction>>?, response: Response<List<SaveAuction>>?) {
                if(response!!.code() == 200) {
                    var saveAuction = response.body()
                    Log.d("saveAuction",saveAuction.toString())
                    for(save in saveAuction){
                        var endAuctionDate = save.endAuctionDate
                        var finalprice = save.finalprice
                        val licenseCar = save.licenseCarsVM
                            var imageLicenseCar = licenseCar!!.imageLicenseCar
                            var numberLicenseCar = licenseCar.number
                            var seq = licenseCar.seq
                        val user = save.userVM
                            var firstName = user!!.firstname
                            var lastName = user.lastname
                            var id = user.id
                        var saveAuctionHistory = SaveAuctionHistory()
                        saveAuctionHistory.endAuctionDate = endAuctionDate
                        saveAuctionHistory.finalprice = finalprice
                        saveAuctionHistory.imageLicenseCar = imageLicenseCar
                        saveAuctionHistory.firstName = firstName
                        saveAuctionHistory.lastName = lastName
                        saveAuctionHistory.numberLicenseCar = numberLicenseCar
                        saveAuctionHistory.id = id!!.toLong()
                        saveAuctionHistory.seq = seq
                        saveAuctionHistoryList.add(saveAuctionHistory)
                        Log.d("saveAuctionHistoryList",saveAuctionHistoryList.toString())
                    }
                }else{
                    Log.d("failed","failed")
                }
            }
            override fun onFailure(call: Call<List<SaveAuction>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }

        })
        return saveAuctionHistoryList
    }


}
