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
import android.widget.Toast
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Transaction
import com.google.firebase.database.MutableData




class AuctionActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    var mDatabase: DatabaseReference? = null
    var mMember: DatabaseReference? = null
    var mWinner: DatabaseReference? = null
    var mHistory: DatabaseReference? = null
    var spinnerPrice: String? = null
    var price: Array<Int> = arrayOf(500, 1000, 1500, 2000, 2500)
    //val mRootRef = FirebaseDatabase.getInstance().reference
    var time: Long = 30000
    var value: String? = null
    var bidTime: Long? = null
    var firstTime: Long? = null
    var totalTime: Long? = 0
    var status: String? = null
    var leftTime: Long? = null
    var tempValue: String? = null
    var tempBidTime: Long? = null
    var state: Int? = 0
    var tempPrice: Long? = 0L
    var timer = object : CountDownTimer(30000, 1000) {

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

    var timers = object : CountDownTimer(totalTime!!, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            textView_time.text = (millisUntilFinished / 1000).toString()
        }

        override fun onFinish() {
            textView_time.text = "Done"
            btn_auction.isEnabled = false
            btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
        }
    }

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
        val image = "drawable/" + bundle.getString("image")
        val resource = image_auction!!.getResources().getIdentifier(image, null, image_auction!!.getContext().getPackageName())
        image_auction!!.setImageResource(resource)
        mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("firstTime").setValue(ServerValue.TIMESTAMP)
        callWebServiceForCheckUserRegisterAuction(licenseCarId, userId)
        mMember = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("member")
        mWinner = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("winner")
        mHistory = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("history")

        mWinner!!.addValueEventListener(object : ValueEventListener {
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

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val mapData = dataSnapshot.child(licenseCarId.toString()).child("history").value as Map<String?, Mapdata?>
                val mapDataSorted = TreeMap<String?, Mapdata?>(Collections.reverseOrder())//TreeMap<String, Mapdata?>(mapData)
                mapDataSorted.putAll(mapData)
                val mapDataEntry = mapDataSorted.entries
                val mapDataItelator = mapDataEntry.iterator()
                val auctionRealtimeList = ArrayList<AuctionRealtimeDatabase>()
                while (mapDataItelator.hasNext()) {
                    val mapDataNext = mapDataItelator.next()
                    val mapDataUser = mapDataNext.value as HashMap<String?, Any?>
                    val id = mapDataUser.get("bidder").toString()
                    val bidTime = mapDataUser.get("bidtime").toString()
                    val price = mapDataUser.get("price").toString()
                    val auctionRealtimeDatabase = AuctionRealtimeDatabase(id, bidTime, null, null, price)
                    auctionRealtimeList.add(auctionRealtimeDatabase)
                }
                val RecyclerViewForAuction = findViewById<View>(R.id.recyclerView_auction) as RecyclerView
                val linearLayoutManager = LinearLayoutManager(this@AuctionActivity)
                val AdapterAuctionRealtime = AdapterAuctionRealtime(auctionRealtimeList)
                RecyclerViewForAuction!!.adapter = AdapterAuctionRealtime
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                RecyclerViewForAuction.layoutManager = linearLayoutManager
                value = dataSnapshot.child(licenseCarId.toString()).child("winner").child("price").value as String?

                tempPrice = value!!.toLong()
                price[0] = parseInt(value) + 500
                price[1] = parseInt(value) + 1000
                price[2] = parseInt(value) + 1500
                price[3] = parseInt(value) + 2000
                price[4] = parseInt(value) + 2500
                spinnerAuction.setSelection(0, true)

                val handler = Handler()
                handler.postDelayed(Runnable {
                    finalPrice.setTextColor(Color.parseColor("#BEBEBE"))
                }, 2000)
                finalPrice.setTextColor(Color.parseColor("#1E90FF"))
                finalPrice.text = value
                //temp = finalPrice

//                bidFirstTime = dataSnapshot.child("bidfirsttime").value as Long?
//                bidSecondTime = dataSnapshot.child("bidsecondtime").value as Long?
                bidTime = dataSnapshot.child(licenseCarId.toString()).child("winner").child("bidTime").value as Long?
                Log.d("bidTime", bidTime.toString())

                if (tempBidTime == null) {

                } else if (tempBidTime == bidTime) {
                } else {

                    if (totalTime!! > 30000) {
                        //timer.cancel()
                    } else {
                        timer.start()

                        Toast.makeText(applicationContext, "tempPrice = " + tempPrice.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                tempBidTime = bidTime
                val mapUser = dataSnapshot.child(licenseCarId.toString()).child("member").value as HashMap<String, Member>?
                val mapUserEntry = mapUser!!.entries
                val mapUserIterator = mapUserEntry.iterator()
                val mapDataUser = HashMap<String, Member>()
                while (mapUserIterator.hasNext()) {
                    var mapUserMapEntry = mapUserIterator.next()
                    //Log.d("mapUser",mapUserMapEntry.value.toString())
                    //var mapSubUser = mapUserMapEntry.value as HashMap<String,Member>?
                    mapDataUser.put(mapUserMapEntry.key, mapUserMapEntry.value)

                }
                var mapUserId = mapDataUser.get(userId.toString()) as HashMap<String, Member>?
                firstTime = mapUserId!!["firstTime"].toString().toLong()
                //var priceMember = dataSnapshot.child(licenseCarId.toString()).child("member").child(userId.toString()).child("price").value as String?
//                if(priceMember!!.equals("0")){
//                    Log.d("firstTime",firstTime.toString())
//                    Log.d("bidFirstTime",bidFirstTime.toString())
//                    Log.d("time",leftTime.toString())
//                    timers = object : CountDownTimer(leftTime!!, 1000) {
//                            override fun onTick(millisUntilFinished: Long) {
//                                textView_time.text = (millisUntilFinished / 1000).toString()
//                            }
//
//                            override fun onFinish() {
//                                textView_time.text = "Done"
//                                btn_auction.isEnabled = false
//                                btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
//                            }
//                        }.start()
//                }else{
//
//                    //timers.cancel()
//                    if (tempTime.equals(null)) {
//                    } else if (value.equals(tempTime)) {
//                    } else {
//                        tempTime = value.toString()
//                        timers.cancel()
//                        timer.start()
//                    }
//                }
                if (state == 0) {
                    leftTime = firstTime!!.minus(tempBidTime!!)
                    leftTime = 30000.minus(leftTime!!)
                    //Toast.makeText(applicationContext,"bidTime = "+ tempBidTime.toString(),Toast.LENGTH_LONG).show()
                    timers = object : CountDownTimer(leftTime!!, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            textView_time.text = (millisUntilFinished / 1000).toString()
                        }

                        override fun onFinish() {
                            textView_time.text = "Done"
                            btn_auction.isEnabled = false
                            btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
                        }
                    }.start()
                    state = 1
                } else {
                    if (tempValue.equals(null)) {
                    } else if (value.equals(tempValue)) {
                    } else {
                        tempValue = value.toString()
                        timers.cancel()
                        timer.start()
                    }
                }




                tempValue = value.toString()
                //Log.d("firstTime",firstTime.toString())
                //Log.d("bidfirstTime",bidFirstTime.toString())
                //Log.d("totalTime",totalTime.toString())
                //timer.start()
//
//                var timers = object : CountDownTimer(totalTime, 1000) {
//
//                    override fun onTick(millisUntilFinished: Long) {
//                        textView_time.text = (millisUntilFinished / 1000).toString()
//                    }
//
//                    override fun onFinish() {
//                        textView_time.text = "Done"
//                        btn_auction.isEnabled = false
//                        btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
//                    }
//                }.start()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        btn_auction.setOnClickListener(View.OnClickListener {
//            timers.cancel()
//            timer.cancel()
//            //Log.d("time" ,time)
//            var auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toString(), "0", status, firstTime.toString(), spinnerPrice.toString())
//            //Log.d("firsttime",bidFirstTime.toString())
//            //Log.d("secondtime",bidSecondTime.toString())
//
//
//            if (parseInt(value) >= parseInt(spinnerPrice)) {
//                price[0] = parseInt(value) + 500
//                price[1] = parseInt(value) + 1000
//                price[2] = parseInt(value) + 1500
//                price[3] = parseInt(value) + 2000
//                price[4] = parseInt(value) + 2500
//                //spinnerAuction.setSelection(0, true)
//
//            } else {
//                //mDatabase!!.child(licenseCarId.toString()).child("winner").setValue(auctionRealtimeDatabase)
//                if (tempPrice == 0L) {
//                    tempPrice = value!!.toLong()
//                }
//                Toast.makeText(applicationContext, "equals temp = " + tempPrice + "=" + value!!.toLong() + tempPrice!!.equals(value!!.toLong()), Toast.LENGTH_LONG).show()
//                if (tempPrice == value!!.toLong()) {
                var mDatabase = mDatabase!!.child(licenseCarId.toString()).child("winner")
                    tranSacTionForAuction(mDatabase!!)
////                    Toast.makeText(applicationContext, "save complete", Toast.LENGTH_LONG).show()
////                    mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
////                    mDatabase!!.child(licenseCarId.toString()).child("history").child(Date().toString()).setValue(auctionRealtimeDatabase)
////                    mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidTime").setValue(ServerValue.TIMESTAMP)
////                    mDatabase!!.child(licenseCarId.toString()).child("winner").child("price").setValue(spinnerPrice)
////                    mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidder").setValue(userId)
////                    mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
////                    mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
////                    mDatabase!!.child(licenseCarId.toString()).child("history").child(Date().toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
//
//                } else {
////                        if(parseInt(value) > parseInt(spinnerPrice)){
////                            mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
////                            mDatabase!!.child(licenseCarId.toString()).child("history").child(Date().toString()).setValue(auctionRealtimeDatabase)
////                            mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidTime").setValue(ServerValue.TIMESTAMP)
////                            mDatabase!!.child(licenseCarId.toString()).child("winner").child("price").setValue(spinnerPrice)
////                            mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidder").setValue(userId)
////                            mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
////                            mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
////                            mDatabase!!.child(licenseCarId.toString()).child("history").child(Date().toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
////                        }else{
//                    // Toast.makeText(applicationContext,"can't save",Toast.LENGTH_LONG).show()
//                    //}
//                }
//                //mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidfirsttime").setValue(bidSecondTime)
//            }

//            mDatabase!!.runTransaction(object : Transaction.Handler {
//                override fun doTransaction(mutableData: MutableData): Transaction.Result {
//                    val p = mutableData.value ?: return Transaction.success(mutableData)
//                    mDatabase!!.child(licenseCarId.toString()).child("winner").child("bidTime").setValue(ServerValue.TIMESTAMP)
////                    if (p!!.stars.containsKey(getUid())) {
////                        // Unstar the post and remove self from stars
////                        p!!.starCount = p!!.starCount - 1
////                        p!!.stars.remove(getUid())
////                    } else {
////                        // Star the post and add self to stars
////                        p!!.starCount = p!!.starCount + 1
////                        p!!.stars.put(getUid(), true)
////                    }
//
//                    // Set value and report transaction success
//                    mutableData.value = p
//                    return Transaction.success(mutableData)
//                }
//
//                override fun onComplete(databaseError: DatabaseError?, b: Boolean,
//                                        dataSnapshot: DataSnapshot?) {
//                    // Transaction completed
//                    Log.d("tag", "postTransaction:onComplete:" + databaseError!!)
//                }
//            })
                //mutable.setValue(p);

            })

    }

    //
    fun callWebServiceForCheckUserRegisterAuction(licenseCarId: Long, userId: Int) {
        var dataList: ArrayList<Mapdata>? = ArrayList()
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
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>> {

            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?) {
                if (response!!.code() == 200) {
                    status = "Active"
                    btn_auction.isEnabled = true
                    spinnerAuction.isEnabled = true
                    //mDatabase!!.child(licenseCarId.toString()).child("member").child(userId.toString()).child("firstTime").setValue(ServerValue.TIMESTAMP)

                } else {
                    status = "deActive"
                    btn_auction.isEnabled = false
                    spinnerAuction.isEnabled = false
                }
                //val auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toString(), "0", status, "0", "0")
            }

            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
                Log.d("Failed", t.toString())
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
        val intent = Intent(baseContext, MainMenuActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
//        timer.cancel()
//        timers.cancel()
        finish()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        spinnerPrice = price[position].toString()
    }

    fun tranSacTionForAuction(mDatabase: DatabaseReference) {
        this.mDatabase!!.runTransaction(object : Transaction.Handler{
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                
            }

            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                var p = mutableData

                return Transaction.success(mutableData)
            }

            //mutable.setValue(p);

        })
    }
}
