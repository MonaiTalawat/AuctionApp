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
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.AdapterAuctionRealtime
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
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
import com.google.gson.Gson


class AuctionActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    var mDatabase: DatabaseReference? = null
    var mPerson: DatabaseReference? = null
    var mWinner: DatabaseReference? = null
    var mHistory: DatabaseReference? = null
    var spinnerPrice: String? = null
    var price: Array<Int> = arrayOf(500, 1000, 1500, 2000, 2500)
    //val mRootRef = FirebaseDatabase.getInstance().reference
    var time: Long = 30000
    var value: Long? = null
    var bidTime: Long? = null
    //var firstTime: Long? = null
    var totalTime: Long? = 0
    var statusUser: String? = null
    var leftTime: Long? = null
    var tempBidTime: Long? = null
    var statrPriceColor: Int? = 0
    var stateTime : Long? = 0L
    var typeColor : String? = "#BEBEBE"
    var version : Long? = null
    var timerClick = object : CountDownTimer(30000, 1000) {

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

    var timerFirst = object : CountDownTimer(totalTime!!, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            textView_time.text = (millisUntilFinished / 1000).toString()
        }

        override fun onFinish() {
            textView_time.text = "Done"
            btn_auction.isEnabled = false
            btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
        }
    }

    var timerFirstDeActive = object : CountDownTimer(totalTime!!, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            textView_time.text = (millisUntilFinished / 1000).toString()
        }

        override fun onFinish() {
            textView_time.text = "Done"
            btn_auction.isEnabled = false
            btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
        }
    }

    var winner: ValueEventListener? = null

    var history: ValueEventListener? = null

    var id : Long? = 0

    var firstTime : Long? = null

    var historySave = ArrayList<AuctionRealtimeDatabase>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)
        spinnerAuction.setOnItemSelectedListener(this)
        mDatabase = FirebaseDatabase.getInstance().reference
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, price)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAuction.setAdapter(spinnerAdapter)
        val bundle = intent.extras
        val userId = bundle.getInt("user_id")
        val licenseCarId = bundle.getLong("licenseCarId")
        val statusLicenseCar = bundle.getString("status")
        var typePage = bundle.getString("typePage")
        if(statusLicenseCar == "3"){
            spinnerAuction.visibility = GONE
            btn_auction.visibility = GONE
            textAuction.text = "สิ้้นสุดการประมูล"
            btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
        }
        mDatabase = mDatabase!!.child(licenseCarId.toString())
        var firstprice = bundle.getLong("firstprice")
        val image = "drawable/" + bundle.getString("image")
        val resource = image_auction!!.getResources().getIdentifier(image, null, image_auction!!.getContext().getPackageName())
        image_auction!!.setImageResource(resource)
        callWebServiceForCheckUserRegisterAuction(licenseCarId, userId)
        mPerson = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("person")
        mHistory = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("history")
        mWinner = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("winner")
        callWebServiceForFirstTimeStamp(userId.toLong())


        winner = mWinner!!.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                value = dataSnapshot.child("price").value.toString().toLong()
                if (value == null) {
                    value = 0
                }
                id = dataSnapshot.child("bidder").value as Long?
                if (value == null) {
                    id = 0
                }
                bidTime = dataSnapshot.child("bidTime").value as Long?
                if (bidTime == null) {
                    bidTime = 0
                }
                if (firstTime == null) {
                    firstTime = 0
                }
                if (statrPriceColor == 0) {

                    price[0] = parseInt(value.toString()) + 500
                    price[1] = parseInt(value.toString()) + 1000
                    price[2] = parseInt(value.toString()) + 1500
                    price[3] = parseInt(value.toString()) + 2000
                    price[4] = parseInt(value.toString()) + 2500
                    spinnerAuction.setSelection(0, true)
                    spinnerAdapter.notifyDataSetChanged()
                    spinnerPrice = price[0].toString()
                    finalPrice.text = value.toString()
                    if(statusUser == "Active") {
                        if (userId.toLong() == id) {
                            finalPrice.setTextColor(Color.GREEN)
                            winText.text = "WIN"
                            winText.setTextColor(Color.YELLOW)

                        } else {
                            finalPrice.setTextColor(Color.RED)
                            winText.text = "LOSE"
                            winText.setTextColor(Color.RED)

                        }
                    }else{
                        if(typeColor == "#BEBEBE"){
                            finalPrice.setTextColor(Color.parseColor(typeColor))
                            typeColor = "#1E90FF"
                        }else{
                            finalPrice.setTextColor(Color.parseColor(typeColor))
                            typeColor = "#BEBEBE"
                        }
                    }
                } else {
                }
                        btn_auction.isEnabled = false
                        btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))

                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        btn_auction.isEnabled = true
                        btn_auction.setBackgroundColor(Color.parseColor("#1E90FF"))
                    }, 2000)

                //////////////////////////// state firstTime ////////////////////////////////////////
                if (stateTime == 0L) {
                    //////////////////////////////////////// tempbidTime  == null firstcome /////////////////////////////////////
                    if (tempBidTime == null) {
                        timerFirstDeActive.cancel()
                        leftTime = firstTime!!.minus(bidTime!!)
                        leftTime = 30000.minus(leftTime!!)
                        timerFirst = object : CountDownTimer(leftTime!!, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                textView_time.text = (millisUntilFinished / 1000).toString()
                            }

                            override fun onFinish() {
                                textView_time.text = "Done"
                                btn_auction.isEnabled = false
                                btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
                                textAuction.text = "สิ้นสุดการประมูล"
                            }
                        }.start()
                    }
                    stateTime = 1
                    //////////////////////////////////////// tempbidTime  == null firstcome /////////////////////////////////////


                    //////////////////////////// bidTime != null , stateTime = 1 ////////////////////////////////////////
                } else {
                    //////////////////////////// bidTime not change ////////////////////////////////////////
                        if (tempBidTime == bidTime) {

                        }

                        //////////////////////////// bidTime not change ////////////////////////////////////////

                        //////////////////////////// bidTime change ////////////////////////////////////////
                        else {
                            timerClick.cancel()
                            timerFirst.cancel()
                            timerClick = object : CountDownTimer(30000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                    textView_time.text = (millisUntilFinished / 1000).toString()
                                }

                                override fun onFinish() {
                                    textView_time.text = "Done"
                                    btn_auction.isEnabled = false
                                    btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
                                    btn_auction.visibility = GONE
                                    spinnerAuction.visibility = GONE
                                    textAuction.text = "สิ้นสุดการประมูล"
                                    textAuction.visibility = VISIBLE
                                    val winner = WinnerAuction()
                                    val winnerUserId = dataSnapshot.child("bidder").value as Long?
                                    val winnerbidTime = dataSnapshot.child("bidTime").value as Long?
                                    val winnerPrice = dataSnapshot.child("price").value as Long?
                                    winner.bidder = winnerUserId
                                    winner.bidTime = winnerbidTime
                                    winner.price = winnerPrice
                                    winner.licenseCarId = licenseCarId
                                    val saveHistoryList = ArrayList<SaveHistory>()
                                    for (history in historySave) {
                                        val saveHistory = SaveHistory()
                                        saveHistory.id = history.bidder!!.toLong()
                                        saveHistory.date = history.bidtime!!.toLong()
                                        saveHistory.price = history.price
                                        saveHistory.licenseCarId = licenseCarId
                                        saveHistoryList.add(saveHistory)
                                    }
                                    if(userId.toLong() == id) {
                                        callWebServiceForSaveAuction(winner)
                                        callWebServiceForSaveHistory(saveHistoryList)
                                        mHistory!!.removeValue()
                                    }
                                }
                            }.start()
                        }
                    //////////////////////////// bidTime change ////////////////////////////////////////
                    }
                //////////////////////////// bidTime != null , stateTime = 1 ////////////////////////////////////////

                ////////////////////////////set tempBidTime ///////////////////////////////////////////////////////
                    tempBidTime = bidTime
                ////////////////////////////set tempBidTime ///////////////////////////////////////////////////////
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        history = mHistory!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mapNull = HashMap<String?,Mapdata?>()
                val mapDataNullNext = HashMap<String?,Any?>()
                val mapdataNull = Mapdata("1","2","20000")
                mapDataNullNext.put("1",mapdataNull)
                mapNull.put("1",mapdataNull)
                var mapData = dataSnapshot.value as? HashMap<String?,Mapdata?>
                if(mapData == null){
                    mapData = mapNull
                }
                val mapDataSorted = TreeMap<String? ,Mapdata?>(Collections.reverseOrder())//TreeMap<String, Mapdata?>(mapData)
                mapDataSorted.putAll(mapData)
                val mapDataEntry = mapDataSorted.entries
                val mapDataItelator = mapDataEntry.iterator()
                val histories = ArrayList<AuctionRealtimeDatabase>()
                while (mapDataItelator.hasNext()) {
                    val mapDataNext = mapDataItelator.next()
                    var mapDataUser = mapDataNext.value as? HashMap<String?, Any?>
                    if(mapDataUser == null){
                        mapDataUser = mapDataNullNext
                    }
                    val gson = Gson()
                    val history = gson.fromJson(mapDataUser.toString(),AuctionRealtimeDatabase::class.java)
                    histories.add(history)
                    historySave = histories
                }
                val RecyclerViewForAuction = findViewById<View>(R.id.recyclerView_auction) as RecyclerView
                val linearLayoutManager = LinearLayoutManager(this@AuctionActivity)
                val AdapterAuctionRealtime = AdapterAuctionRealtime(histories)
                RecyclerViewForAuction.adapter = AdapterAuctionRealtime
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                RecyclerViewForAuction.layoutManager = linearLayoutManager
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        btn_auction.setOnClickListener(View.OnClickListener {
            val auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toLong(), 0, firstTime, spinnerPrice!!.toLong())
            val apiService = ApiInterface.create()
            val call = apiService.currentTimeStamp()
            call.enqueue(object : retrofit2.Callback<Long> {
                override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                    if(response!!.code() == 200) {
                        val timeStamp = response.body().toLong()
                        /////////////////////////////////// insert data member to firebase////////////////////////////////
                        var mapPerson = HashMap<String?,Any?>()
                        mapPerson.put("bidder", timeStamp)
                        mapPerson.put("bidTime", auctionRealtimeDatabase.bidder!!)
                        mapPerson.put("firstTime", auctionRealtimeDatabase.firstTime!!)
                        mapPerson.put("price", auctionRealtimeDatabase.price)
                        mPerson!!.child("member").child(userId.toString()).setValue(mapPerson)
                        /////////////////////////////////// insert data member to firebase////////////////////////////////
                        ///////////////////////////////////map history to fitrbase/////////////////////////////////////////
                        val map = HashMap<String?, Any?>()
                        map.put("bidtime", timeStamp)
                        map.put("bidder", auctionRealtimeDatabase.bidder!!)
                        map.put("firstTime", auctionRealtimeDatabase.firstTime!!)
                        map.put("price", auctionRealtimeDatabase.price)
                        mHistory!!.push().setValue(map)
                        ///////////////////////////////////map history to fitrbase////////////////////////////////////////
                        ///////////////////////////////////map Winner to fitrbase////////////////////////////////////////
                        val mapWinner =  HashMap<String, Any?>()
                        mapWinner.put("bidTime",timeStamp)
                        mapWinner.put("bidder", auctionRealtimeDatabase.bidder!!.toLong())
                        mapWinner.put("price", auctionRealtimeDatabase.price!!)
                        statrPriceColor = 1
                        val task = mWinner!!.setValue(mapWinner)
                        task.addOnCompleteListener(object : OnCompleteListener<Void> {
                            override fun onComplete(task: Task<Void>) {
                                if(task.isSuccessful){
                                    price[0] = parseInt(value.toString()) + 500
                                    price[1] = parseInt(value.toString()) + 1000
                                    price[2] = parseInt(value.toString()) + 1500
                                    price[3] = parseInt(value.toString()) + 2000
                                    price[4] = parseInt(value.toString()) + 2500
                                    spinnerAuction.setSelection(0, true)
                                    spinnerAdapter.notifyDataSetChanged()
                                    spinnerPrice = price[0].toString()
                                    finalPrice.text = value.toString()
                                    finalPrice.setTextColor(Color.GREEN)
                                    winText.setTextColor(Color.YELLOW)
                                    winText.text = "WIN"
                                    statrPriceColor = 0
                                }else{
                                    price[0] = parseInt(value.toString()) + 500
                                    price[1] = parseInt(value.toString()) + 1000
                                    price[2] = parseInt(value.toString()) + 1500
                                    price[3] = parseInt(value.toString()) + 2000
                                    price[4] = parseInt(value.toString()) + 2500
                                    spinnerAuction.setSelection(0, true)
                                    spinnerAdapter.notifyDataSetChanged()
                                    spinnerPrice = price[0].toString()
                                    finalPrice.text = value.toString()
                                    finalPrice.setTextColor(Color.RED)
                                    winText.setTextColor(Color.RED)
                                    winText.text = "LOSE"
                                    statrPriceColor = 0
                                }
                            }

                        })
                        //mutableData!!.setValue(mapWinner)
                        //state = 1
                    }else{

                    }
                }
                override fun onFailure(call: Call<Long>?, t: Throwable?) {

                }


            })
        })

    }

    //
    fun callWebServiceForCheckUserRegisterAuction(licenseCarId: Long, userId: Int) {
        val apiService = ApiInterface.create()
        val registerAuctionList = GenericRequest<RegisterAuctionLicenseCar>()
        val registerAuction = RegisterAuctionLicenseCar()
        val uservm = User()
        uservm.id = userId
        val licenseCar = LicenseCar()
        licenseCar.seq = licenseCarId
        registerAuction.user = uservm
        registerAuction.licenseCar = licenseCar
        registerAuctionList.request = registerAuction
        val call = apiService.checkRegisterAuction(registerAuctionList)
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>> {
            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?) {
                if (response!!.code() == 200) {
                    statusUser = "Active"
                    btn_auction.isEnabled = true
                    spinnerAuction.isEnabled = true
                } else {
                    statusUser = "deActive"
                    btn_auction.visibility = GONE
                    spinnerAuction.visibility = GONE
                    textAuction.visibility = VISIBLE
                }
            }

            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
                Log.d("Failed", t.toString())
            }
        })
    }

    override fun onBackPressed() {
        stateTime = 0L
        val bundle = intent.extras
        val userId = bundle.getInt("user_id")
        val intent = Intent(baseContext, MainMenuActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        timerFirst.cancel()
        timerClick.cancel()
        mWinner!!.removeEventListener(winner!!)
        mHistory!!.removeEventListener(history!!)
        finish()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        spinnerPrice = price[position].toString()
    }

    fun callWebServiceForUpdateStatusLicenseCar(licenseCarId: Long) {
        val apiService = ApiInterface.create()
        val call = apiService.updateStatusLicenseCar(licenseCarId)
        call.enqueue(object : retrofit2.Callback<LicenseCar> {
            override fun onResponse(call: Call<LicenseCar>?, response: Response<LicenseCar>?) {
                if(response!!.code() == 200) {
                    //Log.d("update","updateComplete")
                }else{

                }
            }
            override fun onFailure(call: Call<LicenseCar>?, t: Throwable?) {

            }

        })

    }
    fun callWebServiceForSaveHistory(historySave: ArrayList<SaveHistory>) {
        val apiService = ApiInterface.create()
        var genericRequestHistory = GenericRequest<SaveHistory>()
        genericRequestHistory.requests = historySave
        val call = apiService.saveHistory(genericRequestHistory)
        call.enqueue(object : retrofit2.Callback<SaveHistory>{
            override fun onResponse(call: Call<SaveHistory>?, response: Response<SaveHistory>?) {
                if(response!!.code() == 200){
                    Log.d("save","saveComplete")
                }else{

                }
            }

            override fun onFailure(call: Call<SaveHistory>?, t: Throwable?) {

            }
        })

    }

    fun callWebServiceForFirstTimeStamp(userId : Long) : Long{
        val apiService = ApiInterface.create()
        val call = apiService.currentTimeStamp()
        var timeStamp = 0L
        call.enqueue(object : retrofit2.Callback<Long> {
            override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                if(response!!.code() == 200) {
                    timeStamp = response.body().toLong()
                    mWinner!!.child("member").child(userId.toString()).child("firstTime").setValue(timeStamp)
                    firstTime = timeStamp
                }else{

                }
            }
            override fun onFailure(call: Call<Long>?, t: Throwable?) {

            }
        })
        return timeStamp
    }

    fun callWebServiceForSaveAuction(winnerAuction : WinnerAuction){
        val apiService = ApiInterface.create()
        val genericRequest = GenericRequest<WinnerAuction>()
        genericRequest.request = winnerAuction
        val call = apiService.saveAuction(genericRequest)
        call.enqueue(object : retrofit2.Callback<WinnerAuction> {
            override fun onResponse(call: Call<WinnerAuction>?, response: Response<WinnerAuction>?) {
                if(response!!.code() == 200) {
                    //Log.d("update","updateComplete")
                }else{

                }
            }
            override fun onFailure(call: Call<WinnerAuction>?, t: Throwable?) {

            }

        })

    }



    override fun onStop() {
        super.onStop()
        stateTime = 0L
        statrPriceColor = 0
        mWinner!!.removeEventListener(winner!!)
        mHistory!!.removeEventListener(history!!)
    }
}
