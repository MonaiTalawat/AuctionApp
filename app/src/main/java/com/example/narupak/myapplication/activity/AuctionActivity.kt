package com.example.narupak.myapplication.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.AdapterAuctionRealtime
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_auction.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Integer.parseInt
import java.lang.reflect.Member
import java.security.KeyStore
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AuctionActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    var mDatabase : DatabaseReference? = null
    var mMember : DatabaseReference? = null
    var mWinner : DatabaseReference? = null
    var mHistory : DatabaseReference? = null
    var spinnerPrice : String? = null
    var price : Array<Int> = arrayOf(500,1000,1500,2000,2500)
    //val mRootRef = FirebaseDatabase.getInstance().reference
    var time : Long = 30000
    var value : String? = null
    var bidFirstTime : Long? = null
    var bidSecondTime : Long? = null
    var firstTime : Long? = null
    var totalTime : Long? = null
    var status : String? = null
    var leftTime : Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)
        mDatabase = FirebaseDatabase.getInstance().reference
        spinnerAuction.setOnItemSelectedListener(this)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, price)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAuction.setAdapter(aa)
        val bundle = intent.extras
        val userId = bundle.getInt("user_id")
        val licenseCarId = bundle.getLong("licenseCarId")
        var firstprice = bundle.getLong("firstprice")
        val image = "drawable/"+bundle.getString("image")
        val resource = image_auction!!.getResources().getIdentifier(image, null, image_auction!!.getContext().getPackageName())
        image_auction!!.setImageResource(resource)


        val timer = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                textView_time.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                textView_time.text = "Done"
                btn_auction.isEnabled = false
                btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
                //cancel()
            }
        }
        mMember = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("member")
        mWinner = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("winner")
        mHistory = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("history")

        mWinner!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                btn_auction.isEnabled = false
                btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
                val handler = Handler()
                handler.postDelayed(Runnable {
                    btn_auction.isEnabled = true
                    btn_auction.setBackgroundColor(Color.parseColor("#1E90FF"))
                }, 2000)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        mDatabase!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //timer.cancel()
                val mapData = dataSnapshot.child(licenseCarId.toString()).child("history").value as Map<String?,Mapdata?>
                val mapDataSorted =  TreeMap<String?, Mapdata?>(Collections.reverseOrder())//TreeMap<String, Mapdata?>(mapData)
                mapDataSorted.putAll(mapData)
                val mapDataEntry = mapDataSorted.entries
                val mapDataItelator = mapDataEntry.iterator()
                val auctionRealtimeList = ArrayList<AuctionRealtimeDatabase>()
                while(mapDataItelator.hasNext()) {
                    val mapDataNext = mapDataItelator.next()
                    val mapDataUser = mapDataNext.value as HashMap<String?,Any?>
                    val id = mapDataUser.get("bidder").toString()
                    val bidTime = mapDataUser.get("bidtime").toString()
                    val price = mapDataUser.get("price").toString()
                    val auctionRealtimeDatabase = AuctionRealtimeDatabase(id,bidTime,null,null,price)
                    auctionRealtimeList.add(auctionRealtimeDatabase)
                }
                val RecyclerViewForAuction = findViewById<View>(R.id.recyclerView_auction) as RecyclerView
                val linearLayoutManager = LinearLayoutManager(this@AuctionActivity)
                val AdapterAuctionRealtime = AdapterAuctionRealtime(auctionRealtimeList)
                RecyclerViewForAuction!!.adapter = AdapterAuctionRealtime
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                RecyclerViewForAuction.layoutManager = linearLayoutManager
                value = dataSnapshot.child(licenseCarId.toString()).child("winner").child("price").value as String?
                price[0] = parseInt(value)+500
                price[1] = parseInt(value)+1000
                price[2] = parseInt(value)+1500
                price[3] = parseInt(value)+2000
                price[4] = parseInt(value)+2500
                spinnerAuction.setSelection(0, true)

                val handler = Handler()
                handler.postDelayed(Runnable {
                    finalPrice.setTextColor(Color.parseColor("#BEBEBE"))
                }, 2000)
                finalPrice.setTextColor(Color.parseColor("#1E90FF"))
                finalPrice.text = value
                bidFirstTime = dataSnapshot.child("bidfirsttime").value as Long?
                bidSecondTime = dataSnapshot.child("bidsecondtime").value as Long?
                bidFirstTime = dataSnapshot.child(licenseCarId.toString()).child("winner").child("bidfirsttime").value as Long?
                bidSecondTime = dataSnapshot.child(licenseCarId.toString()).child("winner").child("bidsecondtime").value as Long?
                val mapUser = dataSnapshot.child(licenseCarId.toString()).child("member").value as HashMap<String, Member>?
                val mapUserEntry = mapUser!!.entries
                val mapUserIterator = mapUserEntry.iterator()
                val mapDataUser = HashMap<String,Member>()
                while(mapUserIterator.hasNext()){
                    var mapUserMapEntry = mapUserIterator.next()
                    //Log.d("mapUser",mapUserMapEntry.value.toString())
                    //var mapSubUser = mapUserMapEntry.value as HashMap<String,Member>?
                    mapDataUser.put(mapUserMapEntry.key,mapUserMapEntry.value)

                }
                var mapUserId = mapDataUser.get(userId.toString()) as HashMap<String,Member>?
                firstTime = mapUserId!!["firstTime"].toString().toLong()
                //Log.d("firstTime",firstTime)
                //var mapMember = mapUser!!.get("firstTime").toString().toLong()
                //Log.d("user",mapUser.toString())
                //firstTime = mapMember!!.get("firstTime").toString().toLong()
                var totalTime  = 30000.minus(bidSecondTime!!.minus(firstTime!!))
                if(totalTime > 30000){
                    totalTime = 0
                }else{
                    totalTime = totalTime
                }
                var timers = object : CountDownTimer(totalTime, 1000) {

                    override fun onTick(millisUntilFinished: Long) {
                        textView_time.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        textView_time.text = "Done"
                        btn_auction.isEnabled = false
                        btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
                    }
                }.start()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        btn_auction.setOnClickListener(View.OnClickListener {
            timer.cancel()
                    //Log.d("time" ,time)
                    var auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toString(), "0", status,"0",spinnerPrice.toString())
                    if (parseInt(value) >= parseInt(spinnerPrice)) {
                        price[0] = parseInt(value) + 500
                        price[1] = parseInt(value) + 1000
                        price[2] = parseInt(value) + 1500
                        price[3] = parseInt(value) + 2000
                        price[4] = parseInt(value) + 2500
                        //spinnerAuction.setSelection(0, true)

                    } else {
                        //mDatabase!!.child(licenseCarId.toString()).child("winner").setValue(auctionRealtimeDatabase)
                                mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
                                mDatabase!!.child(licenseCarId.toString()).child("history").child(Date().toString()).setValue(auctionRealtimeDatabase)
                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidsecondtime").setValue(ServerValue.TIMESTAMP)
                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("price").setValue(spinnerPrice)
                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidder").setValue(userId)
                                mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
                                mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
                                mDatabase!!.child(licenseCarId.toString()).child("history").child(Date().toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)

                                Log.d("firsttime",bidFirstTime.toString())
                                Log.d("secondtime",bidSecondTime.toString())
                                totalTime = (bidSecondTime!!.minus(firstTime!!))

                                var leftTime = 30000.minus(totalTime!!)
                                Log.d("leftTime",leftTime.toString())
                                if (leftTime > 30000) {
                                } else {
                                    timer.start()
                                }


                                mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidfirsttime").setValue(bidSecondTime)
                            }

                })
        callWebServiceForCheckUserRegisterAuction(licenseCarId,userId)
    }
//
    fun callWebServiceForCheckUserRegisterAuction(licenseCarId : Long,userId : Int){
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
                    status = "Active"
                    btn_auction.isEnabled = true
                    spinnerAuction.isEnabled = true

                }else{
                    status = "deActive"
                    btn_auction.isEnabled = false
                    spinnerAuction.isEnabled = false
                }
                val auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toString(), "0", status, "0", "0")
                mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
                mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("firstTime").setValue(ServerValue.TIMESTAMP)
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
