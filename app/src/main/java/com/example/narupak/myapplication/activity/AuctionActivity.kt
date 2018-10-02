package com.example.narupak.myapplication.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.*
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
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.io.StringReader
import java.util.Spliterators.iterator


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
    var firstTime: Long? = null
    var totalTime: Long? = 0
    var status: String? = null
    var leftTime: Long? = null
    var tempValue: String? = null
    var tempBidTime: Long? = null
    var state: Int? = 0
    var tempPrice: Long? = 0L
    var tempVersion : Long? = null
    var task  =null
    var version : Long? = null
    var mapMember : Map<String,Member>? = HashMap<String,Member>()
    var timer = object : CountDownTimer(300000, 1000) {

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

    var statusBidTime : Long? = 0L
    var winner: ValueEventListener? = null

    var history: ValueEventListener? = null

    var addValueWinner: ValueEventListener? = null
    var id : Long? = 0
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
        mDatabase = mDatabase!!.child(licenseCarId.toString())
        var firstprice = bundle.getLong("firstprice")
        val image = "drawable/" + bundle.getString("image")
        val resource = image_auction!!.getResources().getIdentifier(image, null, image_auction!!.getContext().getPackageName())
        image_auction!!.setImageResource(resource)
        callWebServiceForCheckUserRegisterAuction(licenseCarId, userId)

      //  mDatabase!!.child("winner").child("member").child(userId.toString()).child("firstTime").setValue(ServerValue.TIMESTAMP)
        mPerson = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("person")
        mHistory = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("history")
        mWinner = FirebaseDatabase.getInstance().reference.child(licenseCarId.toString()).child("winner")

//        if(state == 0){
//
//            Log.d("state",state.toString())
//            state = 1
//            mWinner!!.addChildEventListener(object : ChildEventListener{
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//
//                override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
//                    Log.d("datasnapshotMove",dataSnapshot.toString())
//                }
//
//                override fun onChildChanged(dataSnapshot : DataSnapshot, p1: String?) {
//                    Log.d("datasnapshotChange",dataSnapshot.toString())
////                    if(state == 0) {
////                        var data = dataSnapshot.value as Map<String, Any>?
////                        var user = data!!.get(userId.toString()) as Map<String, Any>?
////                        firstTime = user!!.get("firstTime") as Long?
////                        state = 1
////                    }else{
//                        value = dataSnapshot.value as Long?
//                        price[0] = parseInt(value.toString()) + 500
//                        price[1] = parseInt(value.toString()) + 1000
//                        price[2] = parseInt(value.toString()) + 1500
//                        price[3] = parseInt(value.toString()) + 2000
//                        price[4] = parseInt(value.toString()) + 2500
//                        spinnerAuction.setSelection(0, true)
//                        aa.notifyDataSetChanged()
//                        spinnerPrice = price[0].toString()
//                        finalPrice.text = value.toString()
//                    //}
//
//
////
//                }
//
//                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
//                    Log.d("datasnapshotAdd",dataSnapshot.toString())
//                    //firstTime = dataSnapshot.value as Long?
//                }
//
//                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
//                    Log.d("datasnapshotRemove",dataSnapshot.toString())
//                }
//
//            })
        //}
//        if(state == 1){
//            Log.d("state",state.toString())
//
//            mWinner!!.addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    Log.d("dataSnapshot",dataSnapshot.toString())
//                    firstTime = dataSnapshot.child("member").child(userId.toString()).child("firstTime").value as Long?
//                    value = dataSnapshot.child("price").value.toString().toLong()
//
//                    price[0] = parseInt(value.toString()) + 500
//                    price[1] = parseInt(value.toString()) + 1000
//                    price[2] = parseInt(value.toString()) + 1500
//                    price[3] = parseInt(value.toString()) + 2000
//                    price[4] = parseInt(value.toString()) + 2500
//                    spinnerAuction.setSelection(0, true)
//                    aa.notifyDataSetChanged()
//                    spinnerPrice = price[0].toString()
//                    finalPrice.text = value.toString()
//                }
//
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//            })
//        }


        statusBidTime = 0L

        winner = mWinner!!.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                    Log.d("datasnapshot1", dataSnapshot.toString())
                    value = dataSnapshot.child("price").value.toString().toLong()

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
                    statusBidTime = 0
                    id = dataSnapshot.child("bidder").value as Long?
                    if(state == 0) {
                        if (userId.toLong() == id) {
                            finalPrice.setTextColor(Color.GREEN)
                            Log.d("color", "GREEN And winner is : " + id.toString() + " And Device is : " + userId.toString())
                        } else {
                            finalPrice.setTextColor(Color.RED)
                            Log.d("color", "RED And winner is : " + id.toString() + " And Device is : " + userId.toString())
                        }
                        state = 1
                    }else{

                    }
//                }
                //Log.d("id", id.toString())

//                btn_auction.isEnabled = false
//                btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
//                val handler = Handler()
//                handler.postDelayed(Runnable {
//                    btn_auction.isEnabled = true
//                    btn_auction.setBackgroundColor(Color.parseColor("#1E90FF"))
//                }, 2000)
//                value = dataSnapshot.child("price").value.toString().toLong()
//
//                price[0] = parseInt(value.toString()) + 500
//                price[1] = parseInt(value.toString()) + 1000
//                price[2] = parseInt(value.toString()) + 1500
//                price[3] = parseInt(value.toString()) + 2000
//                price[4] = parseInt(value.toString()) + 2500
//                spinnerAuction.setSelection(0, true)
//                aa.notifyDataSetChanged()
//                spinnerPrice = price[0].toString()
//                finalPrice.text = value.toString()
//                bidTime = dataSnapshot.child("bidTime").value as Long?
//
//                Log.d("bidTime", bidTime.toString())
//                tempPrice = value!!.toLong()
//                if (tempBidTime == null) {
//                tempPrice = value!!.toLong()
//                } else if (tempBidTime == bidTime) {
//                tempPrice = value!!.toLong()
//
//                } else {
//                if (totalTime!! > 30000) {
//                timer.cancel()
//                } else {
//                timer.start()
//                Toast.makeText(applicationContext, "version = " + version.toString(), Toast.LENGTH_SHORT).show()
//                }
//                }
//
//                tempBidTime = bidTime



//                if (state == 0) {
//                    mapMember = dataSnapshot.child("member").value as HashMap<String, Member>?
//                    val mapMemberEntry = mapMember!!.entries
//                    val mapMemberIterator = mapMemberEntry.iterator()
//                    val mapMemeberData = HashMap<String, Member>()
//                    while (mapMemberIterator.hasNext()) {
//                        var mapMemberMapEntry = mapMemberIterator.next()
//                Log.d("mapUser",mapUserMapEntry.value.toString())
//                var mapSubUser = mapUserMapEntry.value as HashMap<String,Member>?
//                        mapMemeberData.put(mapMemberMapEntry.key, mapMemberMapEntry.value)
//
//                    }
//                    var mapUserId = mapMemeberData.get(userId.toString()) as HashMap<String, Member>?
//                    firstTime = mapUserId!!["firstTime"].toString().toLong()
//                    leftTime = firstTime!!.minus(tempBidTime!!)
//                    leftTime = 30000.minus(leftTime!!)
//                    //Toast.makeText(applicationContext,"bidTime = "+ tempBidTime.toString(),Toast.LENGTH_LONG).show()
//                    timers = object : CountDownTimer(leftTime!!, 1000) {
//                        override fun onTick(millisUntilFinished: Long) {
//                            textView_time.text = (millisUntilFinished / 1000).toString()
//                        }
//
//                        override fun onFinish() {
//                            textView_time.text = "Done"
////                            btn_auction.isEnabled = false
////                            btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
//                        }
//                    }.start()
//                    state = 1
//                } else {
//                    if (tempValue.equals(null)) {
//                    } else if ((value.toString()) == tempValue) {
//                    } else {
//                        tempValue = value.toString()
//                        timers.cancel()
//                        timer.start()
//                    }
//                }




               // tempValue = value.toString()
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

//        mPerson!!.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.d("", "mPerson!!.addValueEventListener")
//                btn_auction.isEnabled = false
//                btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
//                val handler = Handler()
//                handler.postDelayed(Runnable {
//                    btn_auction.isEnabled = true
//                    btn_auction.setBackgroundColor(Color.parseColor("#1E90FF"))
//                }, 2000)
//                value = dataSnapshot.child("winner").child("price").value.toString().toLong()
//                version = dataSnapshot.child("winner").child("version").value as Long?
//
//                price[0] = parseInt(value.toString()) + 500
//                price[1] = parseInt(value.toString()) + 1000
//                price[2] = parseInt(value.toString()) + 1500
//                price[3] = parseInt(value.toString()) + 2000
//                price[4] = parseInt(value.toString()) + 2500
//                spinnerAuction.setSelection(0, true)
//                aa.notifyDataSetChanged()
//                spinnerPrice = price[0].toString()
//                finalPrice.text = value.toString()
//
////                var id  = dataSnapshot.child("winner").child("bidder").value as Long?
////                if(userId.toLong() == id){
////                    finalPrice.setTextColor(Color.GREEN)
////                }else{
////                    finalPrice.setTextColor(Color.RED)
////                }
//
//                bidTime = dataSnapshot.child("winner").child("bidTime").value as Long?
//
//                Log.d("bidTime", bidTime.toString())
//                //tempPrice = value!!.toLong()
//                if (tempBidTime == null) {
//                    //tempPrice = value!!.toLong()
//                } else if (tempBidTime == bidTime) {
//                    //tempPrice = value!!.toLong()
//
//                } else {
//                    if (totalTime!! > 30000) {
//                        //timer.cancel()
//                    } else {
//                        timer.start()
//                        //Toast.makeText(applicationContext, "version = " + version.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//                tempBidTime = bidTime
//                val mapUser = dataSnapshot.child("member").value as HashMap<String, Member>?
//                val mapUserEntry = mapUser!!.entries
//                val mapUserIterator = mapUserEntry.iterator()
//                val mapDataUser = HashMap<String, Member>()
//                while (mapUserIterator.hasNext()) {
//                    var mapUserMapEntry = mapUserIterator.next()
//                    //Log.d("mapUser",mapUserMapEntry.value.toString())
//                    //var mapSubUser = mapUserMapEntry.value as HashMap<String,Member>?
//                    mapDataUser.put(mapUserMapEntry.key, mapUserMapEntry.value)
//
//                }
//                var mapUserId = mapDataUser.get(userId.toString()) as HashMap<String, Member>?
//                firstTime = mapUserId!!["firstTime"].toString().toLong()
//
//
//
//                if (state == 0) {
//                    leftTime = firstTime!!.minus(tempBidTime!!)
//                    leftTime = 30000.minus(leftTime!!)
//                    //Toast.makeText(applicationContext,"bidTime = "+ tempBidTime.toString(),Toast.LENGTH_LONG).show()
//                    timers = object : CountDownTimer(leftTime!!, 1000) {
//                        override fun onTick(millisUntilFinished: Long) {
//                            textView_time.text = (millisUntilFinished / 1000).toString()
//                        }
//
//                        override fun onFinish() {
//                            textView_time.text = "Done"
////                            btn_auction.isEnabled = false
////                            btn_auction.setBackgroundColor(Color.parseColor("#BEBEBE"))
//                        }
//                    }.start()
//                    state = 1
//                } else {
//                    if (tempValue.equals(null)) {
//                    } else if ((value.toString()) == tempValue) {
//                    } else {
//                        tempValue = value.toString()
//                        timers.cancel()
//                        timer.start()
//                    }
//                }
//
//
//
//
//                tempValue = value.toString()

//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })

        history = mHistory!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val mapData = dataSnapshot.value as HashMap<String?,Mapdata?>
                val mapDataSorted = TreeMap<String? ,Mapdata?>(Collections.reverseOrder())//TreeMap<String, Mapdata?>(mapData)
                mapDataSorted.putAll(mapData)
                val mapDataEntry = mapDataSorted.entries
                val mapDataItelator = mapDataEntry.iterator()
                val histories = ArrayList<AuctionRealtimeDatabase>()
                while (mapDataItelator.hasNext()) {
                    val mapDataNext = mapDataItelator.next()
                    val mapDataUser = mapDataNext.value as HashMap<String?, Any?>
                    var gson = Gson()
                    var history = gson.fromJson(mapDataUser.toString(),AuctionRealtimeDatabase::class.java)
                    histories.add(history)
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
           // tempVersion = version
            //tempPrice = value!!.toLong()
            //mPerson!!.child("winner").child("price").setValue(spinnerPrice.toString())
            var auctionRealtimeDatabase = AuctionRealtimeDatabase(userId.toString(), "0", status, firstTime.toString(), spinnerPrice!!.toLong())
//            var mData = mDatabase!!.child("person").child("winner")
//            tranSacTionForAuction(mData,userId,licenseCarId,auctionRealtimeDatabase,spinnerPrice,tempVersion)
//            var versionWinners = parseInt(version.toString())+1
//            ///////////////////////////////////map Winner to fitrbase/////////////////////////////////////////
//             var mapWinner =  HashMap<String, Any?>()

            var time = ServerValue.TIMESTAMP
//                Log.d("time",time.toString())


            var mapWinner =  HashMap<String, Any?>()
            //mapWinner.put("bidTime",ServerValue.TIMESTAMP)
            //mapWinner.put("bidTime",1538106931421)
            mapWinner.put("bidder", auctionRealtimeDatabase.bidder!!.toLong())
            mapWinner.put("price", auctionRealtimeDatabase.price)
            //mPerson!!.child("winner").setValue(mapWinner)
            //var mapMemberWinner = HashMap<String,Map<String,Member>?>()
            //mapWinner.put("member",mapMember)
//            var complete = DatabaseReference.CompletionListener(object : DatabaseReference.CompletionListener, (DatabaseError?, DatabaseReference) -> Unit {
//                override fun invoke(p1: DatabaseError?, p2: DatabaseReference) {
//
//                }
//
//                override fun onComplete(databaseError: DatabaseError?, p1: DatabaseReference) {
//                    if(databaseError != null){
//                        Log.d("error",databaseError.message)
//                    }else{
//
//                    }
//                }
//
//            })
            mWinner!!.setValue(mapWinner)
//            var task = mWinner!!.setValue(mapWinner)
//            task.addOnCompleteListener(this,object : OnCompleteListener<Void>  {
//                override fun onComplete(tasks: Task<Void>) {
//
//                    if(!tasks.isSuccessful) {
//                        Log.i("**********", "BUT IT FAILED", tasks.getException())
//                        finalPrice.setTextColor(Color.RED)
//                        Log.d("color", "RED And winner is : " + id.toString() + " And Device is : " + userId.toString())
//                    }else{
//                        Log.d("**********", "success")
//                        var result = tasks.result
//                    }
//                }

            //})


//            ///////////////////////////////////map Winner to fitrbase/////////////////////////////////////////
//
//            ///////////////////////////////////map history to fitrbase/////////////////////////////////////////
//            var map = HashMap<String?, Any?>()
//            map.put("bidtime", ServerValue.TIMESTAMP)
//            map.put("bidder", auctionRealtimeDatabase.bidder!!)
//            map.put("firstTime", auctionRealtimeDatabase.firstTime!!)
//            map.put("price", auctionRealtimeDatabase.price!!)
//            mHistory!!.push().setValue(map)
//            ///////////////////////////////////map history to fitrbase/////////////////////////////////////////
//
//            /////////////////////////////////// insert data member to firebase////////////////////////////////
//            mPerson!!.child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
//            mPerson!!.child("member").child(userId.toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
//            mPerson!!.child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
//            /////////////////////////////////// insert data member to firebase////////////////////////////////
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

    fun tranSacTionForAuction(mData: DatabaseReference, userId: Int, licenseCarId: Long, auctionRealtimeDatabase: AuctionRealtimeDatabase, spinner: String?, tempVersion: Long?) {
        mData.runTransaction(object : Transaction.Handler{
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    //var p1 = mutableData.value as Long
                    var dataList : ArrayList<Map<String,String>> = ArrayList()
                    var mapData = HashMap<String,String>()
                    var winnerPrice = mutableData.child("price").value
                    Log.d("price",value.toString()+"="+spinnerPrice.toString())
                    //if(tempVersion == version){
                    //mutableData.child("version").value = version

                /////////////////////////////////// insert data member to firebase////////////////////////////////
//                mPerson!!.child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
//                mPerson!!.child("member").child(userId.toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
//                mPerson!!.child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
                /////////////////////////////////// insert data member to firebase////////////////////////////////
                    ///////////////////////////////////map Winner to fitrbase/////////////////////////////////////////

                    var mapWinner =  HashMap<String, Any?>()
                    mapWinner.put("bidTime",ServerValue.TIMESTAMP)
                    mapWinner.put("bidder", auctionRealtimeDatabase.bidder!!.toLong())
                    mapWinner.put("price", auctionRealtimeDatabase.price!!)
                    //mPerson!!.child("winner").setValue(mapWinner)
                    //var mapMemberWinner = HashMap<String,Map<String,Member>?>()
                    //mapWinner.put("member",mapMember)
                    mWinner!!.setValue(mapWinner)
                    ///////////////////////////////////map Winner to fitrbase/////////////////////////////////////////
                    ///////////////////////////////////map history to fitrbase/////////////////////////////////////////
                    var map = HashMap<String?, Any?>()
                    map.put("bidtime", ServerValue.TIMESTAMP)
                    map.put("bidder", auctionRealtimeDatabase.bidder!!)
                    map.put("firstTime", auctionRealtimeDatabase.firstTime!!)
                    map.put("price", 250000)
                    mHistory!!.push().setValue(map)

                    ///////////////////////////////////map history to fitrbase/////////////////////////////////////////
                        return Transaction.success(mutableData)
                    //}else{
                        //mutableData.value = version
                        //return Transaction.abort()
                    //}
                    //var versionWinner = mutableData.child("version").value
                    //var priceWinner = mutableData.child("price").value
                    //Log.d("version",versionWinner.toString())
                    //if(tempVersion == version || value.toString().toLong() < spinner.toString().toLong()){
                        //var versionWinners = parseInt(version.toString())+1
                        //mHistory!!.child(Date().toString()).setValue(auctionRealtimeDatabase)
//                        mPerson!!.child("winner").child("bidTime").setValue(ServerValue.TIMESTAMP)
//                        mPerson!!.child("winner").child("price").setValue(spinner)
//                        mPerson!!.child("winner").child("bidder").setValue(userId)
//                        mPerson!!.child("winner").child("version").setValue(versionWinners)
//                        mPerson!!.child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
//                        mPerson!!.child("member").child(userId.toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
//                        mPerson!!.child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
//                        var mapWinner =  HashMap<String, Any>()
//                        mapWinner.put("bidTime",ServerValue.TIMESTAMP)
//                        mapWinner.put("bidder", auctionRealtimeDatabase.bidder!!.toLong())
//                        mapWinner.put("price", auctionRealtimeDatabase.price!!)
//                        mapWinner.put("version",versionWinners)
//                        mPerson!!.child("winner").setValue(mapWinner)
//
//                        var map = HashMap<String?, Any?>()
//                        map.put("bidtime", ServerValue.TIMESTAMP)
//                        map.put("bidder", auctionRealtimeDatabase.bidder!!)
//                        map.put("firstTime", auctionRealtimeDatabase.firstTime!!)
//                        map.put("price", auctionRealtimeDatabase.price!!)

                       // mHistory!!.push().setValue(map)

//                        mHistory!!.child("bidtime").setValue(ServerValue.TIMESTAMP)
                        //mHistory!!.addChildEventListener()
//
//                        return Transaction.success(mutableData)
//                    }else{
//                        return Transaction.abort()
//                    }

//                if(p == null){
//                    mutableData.value = 1
//                }else{
//                    mutableData.value = p+1
//                }
//                    if(p1 == 0L){
//                    p1 = 1
//                    mutableData.value = p1
//                    return Transaction.success(mutableData)
//                }else{
//                   p1 = 0
//                    mutableData.value = p1
//                   return Transaction.success(mutableData)
//                }
//                if(spinnerPrice.equals(null)){
//                    p = p1
//                }else{
//                    p = spinnerPrice
//
//                }
//                mapData.put(userId.toString(),p.toString())
//                mapData.put("number",p1.toString())
//                dataList.add(mapData)

//                if(p!!.toLong() == tempPrice){
//                    mDatabase!!.child("member").child(userId.toString()).setValue(auctionRealtimeDatabase)
//                    mDatabase!!.child("history").child(Date().toString()).setValue(auctionRealtimeDatabase)
//                    mData.child("bidTime").setValue(ServerValue.TIMESTAMP)
//                    mData.child("price").setValue(spinnerPrice)
//                    mData.child("bidder").setValue(userId)
//                    mDatabase!!.child("member").child(userId.toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
//                    mDatabase!!.child("member").child(userId.toString()).child("firstTime").setValue(firstTime)
//                    mDatabase!!.child("history").child(Date().toString()).child("bidtime").setValue(ServerValue.TIMESTAMP)
//                    tempPrice = p.toLong()
//                }else{
//                    Toast.makeText(baseContext,"can't save",Toast.LENGTH_LONG).show()
//                    tempPrice = p.toLong()
//
//                }


            }

            override fun onComplete(p0: DatabaseError?, p1: Boolean, dataSnapshot : DataSnapshot?) {
//                if(tempVersion == )
                //Log.d("version = " , tempVersion.toString() +"=" + dataSnapshot!!.value.toString())
                //if(tempVersion == dataSnapshot.child("version").value){


//                    val handler = Handler()
//                    handler.postDelayed(Runnable {
//                        mPerson!!.child("member").child("number").setValue(0)
//                    }, 1000)
                //}else{
                    //Toast.makeText(baseContext,"Transaction Failed",Toast.LENGTH_LONG).show()

                //}

            }

            //mutable.setValue(p);

        })
    }

    override fun onStop() {
        super.onStop()
        //mDatabase.removeEventListener()
        mWinner!!.removeEventListener(winner!!)
        mHistory!!.removeEventListener(history!!)
    }
}
