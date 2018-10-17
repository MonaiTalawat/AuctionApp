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
        callWebserviceForMyAuctionRecyclerView(this,userId)
        nestedScrollForMoreMyAuction.setBackgroundColor(Color.parseColor("#3fff00a6"))
    }

    fun callWebserviceForMyAuctionRecyclerView(context : Context, user_id : Int){
        val apiService = ApiInterface.create()
        val user = User(user_id)
        val users = GenericRequest<User>()
        var status : String? = null
        var moreRegisterAuctionList = ArrayList<Auction>()
        var moreAuctionList = ArrayList<Auction>()
        var moreFinishAuction = ArrayList<Auction>()
        users.request = user
        val call = apiService.myAuction(users)
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>>{
            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?){
                val listlicenseCar = ArrayList<Auction>()
                if(response?.code() == 200){
                    val recyclerView_moreRegisterAuction = findViewById<View>(R.id.RecyclerViewForMoreRegisterAuction) as RecyclerView
                    val recyclerView_moreAuction = findViewById<View>(R.id.recyclerViewForMoreAuction) as RecyclerView
                    val recyclerView_moreFinishAuction = findViewById<View>(R.id.recyclerViewForMoreFinishAuction) as RecyclerView

                    for (list in response.body().iterator()){
                        //Log.d("JSON",)
                        var objects = list.licenseCar
                        val image = objects!!.imageLicenseCar
                        //Log.d("image",image)
                        val number = objects.number
                        val seq = objects.seq
                        status = objects.status
                        val licensecar_auction = Auction(seq,image,number,null,status)
                        listlicenseCar.add(licensecar_auction)
                    }
                    for(moreList in listlicenseCar){
                        if(moreList.status.equals("1")){
                            moreRegisterAuctionList.add(moreList)
                        }else if(moreList.status.equals("2")){
                            moreAuctionList.add(moreList)
                        }else{
                            moreFinishAuction.add(moreList)
                        }
                    }
                    var adapterRegisterAuction : MoreAdapter? = null
                    var adapterAuction : MoreAdapter? = null
                    var adapterFinishAuction : MoreAdapter? = null
//                    header_more.text = "ทะเบียนรถของฉันเพิ่มเติม"
                    var type_page = "myauction"

                    adapterRegisterAuction = MoreAdapter(moreRegisterAuctionList,type_page,user_id)
                    recyclerView_moreRegisterAuction.adapter = adapterRegisterAuction
                    val linearLayoutManagerMoreRegisterAuction = LinearLayoutManager(baseContext)
                    linearLayoutManagerMoreRegisterAuction.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_moreRegisterAuction.layoutManager = linearLayoutManagerMoreRegisterAuction

                    adapterAuction = MoreAdapter(moreAuctionList,type_page,user_id)
                    recyclerView_moreAuction.adapter = adapterAuction
                    val linearLayoutManagerMoreAucion = LinearLayoutManager(baseContext)
                    linearLayoutManagerMoreAucion.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_moreAuction.layoutManager = linearLayoutManagerMoreAucion

                    adapterFinishAuction = MoreAdapter(moreFinishAuction,type_page,user_id)
                    recyclerView_moreFinishAuction.adapter = adapterFinishAuction
                    val linearLayoutManagerMoreFinishAuction = LinearLayoutManager(baseContext)
                    linearLayoutManagerMoreFinishAuction.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_moreFinishAuction.layoutManager = linearLayoutManagerMoreFinishAuction

                }else{
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
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
