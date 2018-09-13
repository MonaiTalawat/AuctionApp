package com.example.narupak.myapplication.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.finalPrice
import com.example.narupak.myapplication.R.id.user
import com.example.narupak.myapplication.adapter.AdapterAuctionRealtime
import com.example.narupak.myapplication.adapter.AuctionAdapter
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_auction.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import com.google.firebase.database.collection.LLRBNode
import kotlinx.android.synthetic.main.content_main_menu.*
import java.lang.Integer.parseInt
import java.lang.String.format
import java.lang.String.valueOf
import java.lang.Thread.sleep
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AuctionActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    var mDatabase : DatabaseReference? = null
    var spinnerPrice : String? = null
    var price : Array<Int> = arrayOf(500,1000,1500,2000,2500)
    val mRootRef = FirebaseDatabase.getInstance().reference
    var time : Long = 30000
    //var mPrice = mDatabase!!.child("price").setValue(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)
        //var spinner = spinnerAuction
        mDatabase = FirebaseDatabase.getInstance().reference
        spinnerAuction.setOnItemSelectedListener(this)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, price)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAuction.setAdapter(aa)
        val bundle = intent.extras
        var userId = bundle.getInt("user_id")
        var licenseCarId = bundle.getLong("licenseCarId")
        var firstprice = bundle.getLong("firstprice")
        var image = "drawable/"+bundle.getString("image")
        val resource = image_auction!!.getResources().getIdentifier(image, null, image_auction!!.getContext().getPackageName())
        image_auction!!.setImageResource(resource)
        var auctionRealtimes = AuctionRealtimeDatabase(null,"0",firstprice.toString())
        callWebServiceForlastPrice(this,licenseCarId,userId,image)
        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value : String? = dataSnapshot.child(licenseCarId.toString()).child("winner").child("price").value as String?
                var mapData = dataSnapshot.child(licenseCarId.toString()).child("history").value as Map<String?,Mapdata?>
                val mapDataSorted =  TreeMap<String?, Mapdata?>(Collections.reverseOrder())//TreeMap<String, Mapdata?>(mapData)
                mapDataSorted.putAll(mapData)
                finalPrice.text = value
                //var mapDataSorted = TreeSet<String>(mapData.keys)
                var mapDataEntry = mapDataSorted.entries
                var mapDataItelator = mapDataEntry.iterator()
                var auctionRealtimeList = ArrayList<AuctionRealtimeDatabase>()
                while(mapDataItelator.hasNext()) {
                    val mapDataNext = mapDataItelator.next()
                    var mapDataUser = mapDataNext.value as HashMap<String?,Any?>
                    //Log.d("data",mapDataUser.get("id").toString())
                    var mapDataUserEntry = mapDataUser.entries
                    var mapDataUserEntryIte = mapDataUserEntry.iterator()
                    var id = mapDataUser.get("bidder").toString()
                    var bidTime = mapDataUser.get("bidsecondiime").toString()
                    var price = mapDataUser.get("price").toString()
                    //var mapDatas = Mapdata(id,bidTime,price.toString())
                    var auctionRealtimeDatabase = AuctionRealtimeDatabase(id,bidTime,price)
                    auctionRealtimeList.add(auctionRealtimeDatabase)
                    //dataList!!.add(mapDatas)
                }
                var RecyclerViewForAuction = findViewById<View>(R.id.recyclerView_auction) as RecyclerView
//                for (data in dataList!!.iterator()){
//                    var auctionRealtimeDatabase = AuctionRealtimeDatabase(data.id,data.bidTime,data.price.toString())
//                    auctionRealtimeList.add(auctionRealtimeDatabase)
//                }
                val linearLayoutManager = LinearLayoutManager(this@AuctionActivity)
                var AdapterAuctionRealtime = AdapterAuctionRealtime(auctionRealtimeList)
                RecyclerViewForAuction!!.adapter = AdapterAuctionRealtime
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                RecyclerViewForAuction.layoutManager = linearLayoutManager
                price[0] = parseInt(value)+500
                price[1] = parseInt(value)+1000
                price[2] = parseInt(value)+1500
                price[3] = parseInt(value)+2000
                price[4] = parseInt(value)+2500
                spinnerAuction.setSelection(0, true)
                val firstTime = dataSnapshot.child(licenseCarId.toString()).child("winner").child("bidfirsttime").value as Long?
                val secondTime = dataSnapshot.child(licenseCarId.toString()).child("winner").child("bidsecondtime").value as Long?
                var totalTime = secondTime!!.minus(firstTime!!)
                Log.d("firsttime",firstTime.toString())
                Log.d("secondtime",secondTime.toString())
                Log.d("totaltime",totalTime.toString())
                if(firstTime.equals(0)) {
                    totalTime = 0
                }else {
                    if (totalTime > 30000) {
                        totalTime = 0
                    }else{
                        totalTime = totalTime
                    }
                }

                var timerBegin = object : CountDownTimer(totalTime, 1000) {

                    override fun onTick(millisUntilFinished: Long) {
                        textView_time.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        textView_time.text = "Begin"
                    }
                }.start()
                mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidfirsttime").setValue(secondTime)
            }

            override fun onCancelled(error: DatabaseError) {
                //mTextView.setText("Failed: " + databaseError.getMessage())
            }
        })
        var count = 0
        btn_auction.setOnClickListener(View.OnClickListener {
            mDatabase!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value: String? = dataSnapshot.child(licenseCarId.toString()).child("winner").child("price").value as String?
                    //var key = mRootRef.child(licenseCarId.toString()).child("user").push().key

                    //Log.d("time" ,time)
                    var auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toString(), null, spinnerPrice.toString())
                    if (parseInt(value!!) >= parseInt(spinnerPrice)) {
                        price[0] = parseInt(value) + 500
                        price[1] = parseInt(value) + 1000
                        price[2] = parseInt(value) + 1500
                        price[3] = parseInt(value) + 2000
                        price[4] = parseInt(value) + 2500
                        spinnerAuction.setSelection(0, true)

                    } else {
                        //mDatabase!!.child(licenseCarId.toString()).child("winner").setValue(auctionRealtimeDatabase)
                        var timestamp: Long? = 0L
                        val apiService = ApiInterface.create()
                        val call = apiService.queryCurrentTimeStamp()
                        call.enqueue(object : retrofit2.Callback<Long> {
                            override fun onFailure(call: Call<Long>?, t: Throwable?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                                mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
                                mDatabase!!.child(licenseCarId.toString()).child("history").child(Date().toString()).setValue(auctionRealtimeDatabase)
                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidsecondtime").setValue(response!!.body())
                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("price").setValue(spinnerPrice)
                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidder").setValue(userId)
                                mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("bidtime").setValue(response.body())
                                val firstTime = dataSnapshot.child(licenseCarId.toString()).child("winner").child("bidfirsttime").value as Long?
                                val secondTime = dataSnapshot.child(licenseCarId.toString()).child("winner").child("bidsecondtime").value as Long?
                                var totalTime = secondTime!!.minus(firstTime!!)
                                if (totalTime > 30000) {
                                    totalTime = 0
                                } else {
                                    totalTime = 30000
                                }
                                var timer = object : CountDownTimer(totalTime, 1000) {

                                    override fun onTick(millisUntilFinished: Long) {
                                        textView_time.text = (millisUntilFinished / 1000).toString()
                                    }

                                    override fun onFinish() {
                                        textView_time.text = "Done"
                                        cancel()
                                    }
                                }.start()

                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidfirsttime").setValue(secondTime)
                            }
                        })

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //mTextView.setText("Failed: " + databaseError.getMessage())
                }
            })
        })
    }

    fun callWebServiceForlastPrice(context : Context,licenseCarId : Long,userId : Int,image : String?){
        var dataList : ArrayList<Mapdata>? = ArrayList()
        val apiService = ApiInterface.create()
        val registerAuctionList = GenericRequest<RegisterAuctionLicenseCar>()
        val registerAuction = RegisterAuctionLicenseCar()
        var uservm = User()
        uservm.id = userId
        var licenseCar = LicenseCar()
        licenseCar.seq = licenseCarId
        registerAuction.user = uservm
        registerAuction.licenseCar = licenseCar
        registerAuctionList.request = registerAuction
        val call = apiService.checkRegisterAuction(registerAuctionList)
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>>{

            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?) {
                if(response!!.code() == 200){
                    btn_auction.isEnabled = true
                    spinnerAuction.isEnabled = true
                }else{
                    btn_auction.isEnabled = false
                    spinnerAuction.isEnabled = false
                    Log.d("error","error")
                }
            }
            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
                Log.d("Failed",t.toString())
            }
        })
    }
//    fun callWebServiceForAuction(licenseCarId : Long?){
//        val apiService = ApiInterface.create()
//        val call = apiService.queryMemberRegisterAuctionLicenseCar(parseInt(licenseCarId.toString()))
//        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>> {
//            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?) {
//                if(response!!.code() == 200){
//                    for(regisList in response.body().iterator()){
//                        val licenseCar = regisList.licenseCar
//                        val seq = licenseCar!!.seq
//                        val user = regisList.user
//                        val userId = user!!.id
//                        var auctionRealtime = AuctionRealtimeDatabase(null,null,null)
//                        var mUserRef = mDatabase!!.child("licenseCarId").child(seq.toString()).child("user").child(userId.toString()).push().setValue(1)
//                        //Log.d("result",userId.toString())
//                    }
//                }else{
//                    Log.d("error","error")
//                }
//            }
//            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
//                Log.d("failed",t.toString())
//            }
//
//        })
//    }
    override fun onBackPressed() {
        val bundle = intent.extras
        val userId = bundle.getInt("user_id")
        val intent = Intent(baseContext,MainMenuActivity::class.java)
        intent.putExtra("user_id",userId)
        startActivity(intent)
        finish()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        spinnerPrice = price[position].toString()
    }
}
