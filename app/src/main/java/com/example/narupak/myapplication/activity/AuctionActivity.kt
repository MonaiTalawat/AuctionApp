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
    var status: String? = null
    var leftTime: Long? = null
    var tempValue: String? = null
    var tempBidTime: Long? = null
    var statrPriceColor: Int? = 0
    var stateTime : Long? = 0L
    var tempPrice: Long? = 0L
    var tempVersion : Long? = null
    var task  =null
    var version : Long? = null
    var mapMember : Map<String,Member>? = HashMap<String,Member>()
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

    var statusBidTime : Long? = 0L
    var winner: ValueEventListener? = null

    var history: ValueEventListener? = null

    var addValueWinner: ValueEventListener? = null
    var id : Long? = 0

    var firstTime : Long? = null

    var historySave = ArrayList<AuctionRealtimeDatabase>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction)
        spinnerAuction.setOnItemSelectedListener(this)
        mDatabase = FirebaseDatabase.getInstance().reference
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, price)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAuction.setAdapter(aa)
        val bundle = intent.extras
        val userId = bundle.getInt("user_id")
        val licenseCarId = bundle.getLong("licenseCarId")
        val statusLicenseCar = bundle.getString("status")
        var typePage = bundle.getString("typePage")
        if(statusLicenseCar == "3"){
            btn_auction.isEnabled = false
            btn_auction.text = "สิ้้นสุดการประมูล"
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


                Log.d("datasnapshot1", dataSnapshot.toString())
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
                //firstTime = dataSnapshot.child("member").child(userId.toString()).child("firstTime").value as Long?
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
                    aa.notifyDataSetChanged()
                    spinnerPrice = price[0].toString()
                    finalPrice.text = value.toString()
                    //bidTime = dataSnapshot.child("bidTime").value as Long?
                    //statusBidTime = 0
                    if (userId.toLong() == id) {
                        finalPrice.setTextColor(Color.GREEN)
                        winText.text = "WIN"
                        winText.setTextColor(Color.YELLOW)
                        //Log.d("colorLis", "GREEN And winner is : " + id.toString() + " And Device is : " + userId.toString())
                    } else {
                        finalPrice.setTextColor(Color.RED)
                        winText.text = "LOSE"
                        winText.setTextColor(Color.RED)
                        //Log.d("colorLis", "RED And winner is : " + id.toString() + " And Device is : " + userId.toString())
                    }
                } else {
                }
                //tempPrice = value

                    //if(status == "Active") {
                        btn_auction.isEnabled = false
                        btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))

                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        btn_auction.isEnabled = true
                        btn_auction.setBackgroundColor(Color.parseColor("#1E90FF"))
                    }, 2000)
//                }else{
//                        btn_auction.isEnabled = false
//                        btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
//                    }

                //////////////////////////// state firstTime ////////////////////////////////////////
                if (stateTime == 0L) {
                    Log.d("first", "first")
                    Log.d("firsttime", firstTime.toString())
                    Log.d("bidTime", bidTime.toString())
                    //tempPrice = value!!.toLong()
                    //////////////////////////////////////// tempbidTime  == null firstcome /////////////////////////////////////
                    if (tempBidTime == null) {
                        //Log.d("bidTime1",bidTime.toString())
                        leftTime = firstTime!!.minus(bidTime!!)
                        leftTime = 30000.minus(leftTime!!)
                        //Toast.makeText(applicationContext,"bidTime = "+ tempBidTime.toString(),Toast.LENGTH_LONG).show()
                        timerFirst = object : CountDownTimer(leftTime!!, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                textView_time.text = (millisUntilFinished / 1000).toString()
                            }

                            override fun onFinish() {
                                textView_time.text = "Done"
                                btn_auction.isEnabled = false
                                btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
                                //callWebServiceForUpdateStatusLicenseCar(licenseCarId)
                            }
                        }.start()
                    }
                    stateTime = 1
                    //Log.d("stateTime",stateTime.toString())
                    //////////////////////////////////////// tempbidTime  == null firstcome /////////////////////////////////////


                    //////////////////////////// bidTime != null , stateTime = 1 ////////////////////////////////////////
                } else {
                    //////////////////////////// bidTime not change ////////////////////////////////////////
                        if (tempBidTime == bidTime) {

                        }

                        //////////////////////////// bidTime not change ////////////////////////////////////////

                        //////////////////////////// bidTime change ////////////////////////////////////////
                        else {
                            Log.d("click", "click")
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
                                    btn_auction.text = "เสร็จสิ้นการประมูล"
                                    var winner = WinnerAuction()
                                    var winnerUserId = dataSnapshot.child("bidder").value as Long?
                                    var winnerbidTime = dataSnapshot.child("bidTime").value as Long?
                                    var winnerPrice = dataSnapshot.child("price").value as Long?
                                    winner.bidder = winnerUserId
                                    winner.bidTime = winnerbidTime
                                    winner.price = winnerPrice
                                    winner.licenseCarId = licenseCarId
                                    Log.d("historySave",historySave.toString())
                                    var saveHistoryList = ArrayList<SaveHistory>()
                                    for (history in historySave) {
                                        val saveHistory = SaveHistory()
                                        saveHistory.id = history.bidder!!.toLong()
                                        saveHistory.date = history.bidtime!!.toLong()
                                        saveHistory.price = history.price
                                        saveHistory.licenseCarId = licenseCarId
                                        saveHistoryList.add(saveHistory)
                                    }
                                    //callWebServiceForSaveHistory(saveHistoryList)
                                    //callWebServiceForUpdateStatusLicenseCar(licenseCarId)
                                    if(userId.toLong() == id) {
                                        callWebServiceForSaveAuction(winner)
                                        callWebServiceForSaveHistory(saveHistoryList)
                                        mHistory!!.removeValue()
                                        //callWebServiceForUpdateStatusLicenseCar(licenseCarId)
                                    }
                                }
                            }.start()
                            //Toast.makeText(applicationContext, "version = " + version.toString(), Toast.LENGTH_SHORT).show()
                            //stateTime = 0
                            //stateTime = 1L
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
        //stateTime = 1L
        Log.d("stateTimeout",stateTime.toString())

        history = mHistory!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var mapNull = HashMap<String?,Mapdata?>()
                var mapDataNullNext = HashMap<String?,Any?>()
                var mapdataNull = Mapdata("1","2","20000")
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
                    var gson = Gson()
                    var history = gson.fromJson(mapDataUser.toString(),AuctionRealtimeDatabase::class.java)
                    histories.add(history)
                    historySave = histories
                }
                val RecyclerViewForAuction = findViewById<View>(R.id.recyclerView_auction) as RecyclerView
                val linearLayoutManager = LinearLayoutManager(this@AuctionActivity)
                val AdapterAuctionRealtime = AdapterAuctionRealtime(histories)
                RecyclerViewForAuction!!.adapter = AdapterAuctionRealtime
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                RecyclerViewForAuction.layoutManager = linearLayoutManager
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        btn_auction.setOnClickListener(View.OnClickListener {
            var auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toLong(), 0, status, firstTime, spinnerPrice!!.toLong())
            var mData = mDatabase!!.child("winner")
            //tranSacTionForAuction(mData,userId,licenseCarId,auctionRealtimeDatabase,spinnerPrice,tempVersion)
            val apiService = ApiInterface.create()
            val call = apiService.currentTimeStamp()
            call.enqueue(object : retrofit2.Callback<Long> {
                override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                    if(response!!.code() == 200) {
                        var timeStamp = response.body().toLong()

                        /////////////////////////////////// insert data member to firebase////////////////////////////////
                        mPerson!!.child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
                        mPerson!!.child("member").child(userId.toString()).child("bidtime").setValue(timeStamp)
                        mPerson!!.child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
                        /////////////////////////////////// insert data member to firebase////////////////////////////////
                        ///////////////////////////////////map history to fitrbase/////////////////////////////////////////

                        var map = HashMap<String?, Any?>()
                        map.put("bidtime", timeStamp)
                        map.put("bidder", auctionRealtimeDatabase.bidder!!)
                        map.put("firstTime", auctionRealtimeDatabase.firstTime!!)
                        map.put("price", auctionRealtimeDatabase.price)
                        mHistory!!.push().setValue(map)

                        ///////////////////////////////////map history to fitrbase////////////////////////////////////////

                        ///////////////////////////////////map Winner to fitrbase////////////////////////////////////////
                        var mapWinner =  HashMap<String, Any?>()
                        mapWinner.put("bidTime",timeStamp)
                        mapWinner.put("bidder", auctionRealtimeDatabase.bidder!!.toLong())
                        mapWinner.put("price", auctionRealtimeDatabase.price!!)
                        //Log.d("map",mapWinner.toString())
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
                                    aa.notifyDataSetChanged()
                                    spinnerPrice = price[0].toString()
                                    finalPrice.text = value.toString()
                                    //bidTime = dataSnapshot.child("bidTime").value as Long?
                                    //statusBidTime = 0
                                    //Log.d("task","taskComplete")
                                    finalPrice.setTextColor(Color.GREEN)
                                    winText.setTextColor(Color.YELLOW)
                                    winText.text = "WIN"
                                    //Log.d("colortask", "GREEN And winner is : " + id.toString() + " And Device is : " + userId.toString())
                                    statrPriceColor = 0
                                }else{
                                    price[0] = parseInt(value.toString()) + 500
                                    price[1] = parseInt(value.toString()) + 1000
                                    price[2] = parseInt(value.toString()) + 1500
                                    price[3] = parseInt(value.toString()) + 2000
                                    price[4] = parseInt(value.toString()) + 2500
                                    spinnerAuction.setSelection(0, true)
                                    aa.notifyDataSetChanged()
                                    spinnerPrice = price[0].toString()
                                    finalPrice.text = value.toString()
                                    //bidTime = dataSnapshot.child("bidTime").value as Long?
                                    //statusBidTime = 0
                                    finalPrice.setTextColor(Color.RED)
                                    winText.setTextColor(Color.RED)
                                    winText.text = "LOSE"
                                    //Log.d("colortask", "RED And winner is : " + id.toString() + " And Device is : " + userId.toString())
                                    //Log.d("task","taskFailed")
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

    fun tranSacTionForAuction(mData: DatabaseReference, userId: Int, licenseCarId: Long, auctionRealtimeDatabase: AuctionRealtimeDatabase, spinner: String?, tempVersion: Long?) {
        mData.runTransaction(object : Transaction.Handler{
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val apiService = ApiInterface.create()
                val call = apiService.currentTimeStamp()
                call.enqueue(object : retrofit2.Callback<Long> {
                    override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                        if(response!!.code() == 200) {
                            var timeStamp = response.body().toLong()

                            ///////////////////////////////////map Winner to fitrbase////////////////////////////////////////
                            var mapWinner =  HashMap<String, Any?>()
                            mapWinner.put("bidTime",timeStamp)
                            mapWinner.put("bidder", auctionRealtimeDatabase.bidder!!.toLong())
                            mapWinner.put("price", auctionRealtimeDatabase.price!!)
                            //Log.d("map",mapWinner.toString())
                            mWinner!!.setValue(mapWinner)
                            ///////////////////////////////////map Winner to fitrbase/////////////////////////////////////////
                            /////////////////////////////////// insert data member to firebase////////////////////////////////
//                            mPerson!!.child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
//                            mPerson!!.child("member").child(userId.toString()).child("bidtime").setValue(timeStamp)
//                            mPerson!!.child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
                            /////////////////////////////////// insert data member to firebase////////////////////////////////
                            ///////////////////////////////////map history to fitrbase/////////////////////////////////////////
//                            var map = HashMap<String?, Any?>()
//                            map.put("bidtime", timeStamp)
//                            map.put("bidder", auctionRealtimeDatabase.bidder!!)
//                            map.put("firstTime", auctionRealtimeDatabase.firstTime!!)
//                            map.put("price", 250000)
//                            mHistory!!.push().setValue(map)

                            ///////////////////////////////////map history to fitrbase////////////////////////////////////////
                            //mutableData!!.setValue(mapWinner)
                            //state = 1
                        }else{

                        }
                    }
                    override fun onFailure(call: Call<Long>?, t: Throwable?) {

                    }


                })
                    return Transaction.success(mutableData)


            }

            override fun onComplete(p0: DatabaseError?, p1: Boolean, dataSnapshot : DataSnapshot?) {
                var memberId = dataSnapshot!!.child("bidder").value as Long?
                if (userId.toLong() == id) {
                    finalPrice.setTextColor(Color.GREEN)
                    //Log.d("colorComplete", "GREEN And winner is : " + id.toString() + " And Device is : " + userId.toString())
                } else {
                    finalPrice.setTextColor(Color.RED)
                    //Log.d("colorComplete", "RED And winner is : " + id.toString() + " And Device is : " + userId.toString())
                }
                //state = 0
            }

        })
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
                    //Log.d("Time",timeStamp.toString())
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
        var genericRequest = GenericRequest<WinnerAuction>()
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
