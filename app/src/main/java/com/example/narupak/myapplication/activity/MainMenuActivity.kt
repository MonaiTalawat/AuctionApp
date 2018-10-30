package com.example.narupak.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.narupak.myapplication.dataservice.DataService
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.app_bar_main_menu.*
import kotlinx.android.synthetic.main.content_main_menu.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.narupak.myapplication.adapter.AuctionAdapter
import android.os.Looper
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.AdapterMyAuctionStatistic
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.nav_header_main_menu.*
import kotlinx.android.synthetic.main.nav_header_main_menu.view.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainMenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var dataservice : DataService? = null
    var adapterAuction : AuctionAdapter? = null
    var adapterRegister : AuctionAdapter? = null
    var adapter_myauction : AuctionAdapter? = null
    var adapterstatisticAuction : AdapterMyAuctionStatistic? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

       // val recyclerview_auction_main = findViewById<View?>(R.id.recyclerView_auction) as RecyclerView?

        val bundle = intent.extras
        val userid = bundle.getInt("user_id")
        val firstName = bundle.getString("firstname")
        val lastName = bundle.getString("lastname")
        val name = firstName+" "+lastName
        val mail = bundle.getString("mail")

        //Picasso.with(this).load("www.journaldev.com").placeholder(R.drawable.hammer).into(imageViewHeader);


        callWebServiceForSetting(userid)
        callWebserviceForAuction(this,userid)
        callWebserviceForRegisterAuction(this,userid)
        callWebserviceForMyAuction(this,userid)

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setItemIconTintList(null);
        //nav_view.textViewHeader.text = "Onepiecez"
        more_auction.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
            intent.putExtra("name","auction")
            intent.putExtra("user_id",userid)
            startActivity(intent)
            finish()
        })
        more_register_auction.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
            intent.putExtra("name","register")
            intent.putExtra("user_id",userid)
            startActivity(intent)
            finish()
        })
        constraintLayoutMyAuction.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, ActionBarTabActivity::class.java)
            intent.putExtra("name","myauction")
            intent.putExtra("user_id",userid)
            intent.putExtra("position_auction",1)
            startActivity(intent)
            finish()
        })

        constraintMyRegisterAuction.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, ActionBarTabActivity::class.java)
            intent.putExtra("name","myauction")
            intent.putExtra("user_id",userid)
            intent.putExtra("position_auction",0)
            startActivity(intent)
            finish()
        })
        constraintMyAuction.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, ActionBarTabActivity::class.java)
            intent.putExtra("name","myauction")
            intent.putExtra("user_id",userid)
            intent.putExtra("position_auction",1)
            startActivity(intent)
            finish()
        })
        constraintMyHistory.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, ActionBarTabActivity::class.java)
            intent.putExtra("name","myauction")
            intent.putExtra("user_id",userid)
            intent.putExtra("position_auction",2)
            startActivity(intent)
            finish()
        })
    }

    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("Warnning Auction")
        // Display a message on alert dialog
        builder.setMessage("คุณต้องการออกจากแอปพลิเคชันใช่หรือไม่")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("ใช่") { dialog, which ->
            // Do something when user press the positive button
            finish()
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton("ไม่") { dialog, which ->
            Toast.makeText(this, "You are not agree.", Toast.LENGTH_SHORT).show()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.user -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                if(userId == null) {
                    val intent = Intent(baseContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(baseContext, SettingActivity::class.java)
                    intent.putExtra("user_id",userId)
                    startActivity(intent)
                    finish()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.auction -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
                intent.putExtra("name","auction")
                intent.putExtra("user_id",userId)
                startActivity(intent)
                finish()
            }
            R.id.register_auction -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
                intent.putExtra("name","register")
                intent.putExtra("user_id",userId)
                startActivity(intent)
                finish()
            }
            R.id.myRegisterAuction -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext, ActionBarTabActivity::class.java)
                intent.putExtra("name","myauction")
                intent.putExtra("user_id",userId)
                intent.putExtra("position_auction",0)
                startActivity(intent)
                finish()
            }
            R.id.myAuction -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext, ActionBarTabActivity::class.java)
                intent.putExtra("name","myauction")
                intent.putExtra("user_id",userId)
                intent.putExtra("position_auction",1)
                startActivity(intent)
                finish()
            }
            R.id.myhistoryAuction->{
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext,ActionBarTabActivity::class.java)
                intent.putExtra("name","historyAuction")
                intent.putExtra("user_id",userId)
                intent.putExtra("position_auction",2)
                startActivity(intent)
                finish()
            }
            R.id.notification -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext, NotificationActivity::class.java)
                intent.putExtra("user_id",userId)
                startActivity(intent)
                finish()
            }
            R.id.setting -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                var intent = Intent(baseContext, SettingActivity::class.java)
                intent.putExtra("name","myauction")
                intent.putExtra("user_id",userId)
                startActivity(intent)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun callWebserviceForAuction(context : Context,userId : Int?){
        val apiService = ApiInterface.create()
        val users = GenericRequest<User>()
        var listLicenseCar : List<LicenseCar>? = null
        var number : String? = null
        var image : String? = null
        var seq : Long? = null
        var firstprice : Long? = null
        var status : String? = null
        val call = apiService.queryLicenseCarByStatus()
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<LicenseCar>>{
            override fun onResponse(call: Call<List<LicenseCar>>?, response: Response<List<LicenseCar>>?){
                val listcar = ArrayList<Auction>()
                if(response?.code() == 200){
                    for(carlist in response.body().listIterator()){
                        number = carlist.number
                        image = carlist.imageLicenseCar
                        seq = carlist.seq
                        firstprice = carlist.firstprice
                        status = carlist.status
                        val licensecar_auction = Auction(seq,image,number,firstprice,status)
                        listcar!!.add(licensecar_auction)
                    }
                    val recyclerview_auction_main = findViewById<View?>(R.id.recyclerView_auction) as RecyclerView?
                    val linearLayoutManager = LinearLayoutManager(context)
                    adapterAuction = AuctionAdapter(listcar,"auction",userId,status!!.toLong())
                    recyclerview_auction_main!!.adapter = adapterAuction
                    linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    recyclerView_auction.layoutManager = linearLayoutManager
                    //Log.d("list",list.toString())
                    val duration : Long = 1000
                    var position_auction = 0
                    //val pixelsToMove = 30
                    val mHandler = android.os.Handler(Looper.getMainLooper())
                    val SCROLLING_RUNNABLE_AUCTION = object : Runnable {
                        override fun run() {
                            //recyclerview_auction_main.smoothScrollBy(pixelsToMove, 0)
                            if(position_auction < listcar!!.size){
                                //Log.d("time auction",position_auction.toString())
                                recyclerview_auction_main.smoothScrollToPosition(position_auction)
                                position_auction++
                            }else{
                                //Log.d("time auction",position_auction.toString())
                                position_auction = 0
                            }
                            mHandler.postDelayed(this, duration)
                        }
                    }
                    recyclerview_auction_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            val lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                            if (lastItem == linearLayoutManager.getItemCount() - 1) {
                                mHandler.removeCallbacks(SCROLLING_RUNNABLE_AUCTION)
                                val postHandler = android.os.Handler()
                                postHandler.postDelayed(Runnable {
                                    recyclerview_auction_main.adapter = adapterAuction
                                    mHandler.postDelayed(SCROLLING_RUNNABLE_AUCTION, 5000)
                                }, 1000)
                            }
                        }
                    })
                    mHandler.postDelayed(SCROLLING_RUNNABLE_AUCTION, 3000)

                }else{
                    number = "1234"
                    image = "car_default"
                    seq = 0
                    status = "1"
                    firstprice = 10000
                    val licensecar_auction = Auction(seq,image,number,firstprice,status)
                    listcar!!.add(licensecar_auction)
                    val recyclerview_auction_main = findViewById<View?>(R.id.recyclerView_auction) as RecyclerView?
                    val linearLayoutManager = LinearLayoutManager(context)
                    adapterAuction = AuctionAdapter(listcar,"auction",userId,status!!.toLong())
                    recyclerview_auction_main!!.adapter = adapterAuction
                    linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    recyclerView_auction.layoutManager = linearLayoutManager
                }
            }

            override fun onFailure(call: Call<List<LicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }
    fun callWebserviceForRegisterAuction(context : Context,user_id : Int){
        val apiService = ApiInterface.create()
        val users = GenericRequest<User>()
        var listLicenseCar : List<LicenseCar>? = null
        var number : String? = null
        var image : String? = null
        var seq : Long? = null
        var status : String? = null
        val call = apiService.queryRegisterLicenseCarByStatus()
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<LicenseCar>>{
            override fun onResponse(call: Call<List<LicenseCar>>?, response: Response<List<LicenseCar>>?){
                val listcar = ArrayList<Auction>()
                if(response?.code() == 200){
                    for(carlist in response.body().listIterator()){
                        number = carlist.number
                        image = carlist.imageLicenseCar
                        seq = carlist.seq
                        status = carlist.status
                        val licensecar_auction = Auction(seq,image,number,null,status)
                        listcar.add(licensecar_auction)
                    }
                    val recyclerview_register = findViewById<View>(R.id.recyclerview_register) as RecyclerView?
                    val linearLayoutManager_register = LinearLayoutManager(context)
                    adapterRegister = AuctionAdapter(listcar,"register",user_id,status!!.toLong())
                    recyclerview_register!!.adapter = adapterRegister
                    linearLayoutManager_register.orientation = LinearLayoutManager.HORIZONTAL
                    recyclerview_register.layoutManager = linearLayoutManager_register
                    //Log.d("list",list.toString())
                    val duration_register : Long = 3000
                    //val pixelsToMove = 30
                    var position_register = 0
                    val mHandler_register = android.os.Handler(Looper.getMainLooper())
                    val SCROLLING_RUNNABLE_REGISTER = object : Runnable {
                        override fun run() {
                            //recyclerview_auction_main.smoothScrollBy(pixelsToMove, 0)
                            if(position_register < listcar.size){
                                //Log.d("time register",position_register.toString())
                                recyclerview_register.smoothScrollToPosition(position_register)
                                position_register++
                            }else{
                                //Log.d("time register",position_register.toString())
                                position_register = 0
                            }
                            mHandler_register.postDelayed(this, duration_register)
                        }
                    }
                    recyclerview_register.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            val lastItem = linearLayoutManager_register.findLastCompletelyVisibleItemPosition()
                            if (lastItem == linearLayoutManager_register.getItemCount() - 1) {
                                mHandler_register.removeCallbacks(SCROLLING_RUNNABLE_REGISTER)
                                val postHandler = android.os.Handler()
                                postHandler.postDelayed(Runnable {
                                    recyclerview_register.adapter = adapterRegister
                                    mHandler_register.postDelayed(SCROLLING_RUNNABLE_REGISTER, 2000)
                                }, 2000)
                            }
                        }
                    })
                    mHandler_register.postDelayed(SCROLLING_RUNNABLE_REGISTER, 3000)
                }else{
                    number = "1234"
                    image = "car_default"
                    seq = 0
                    status = "1"
                    val licensecar_auction = Auction(seq,image,number,null,status)
                    listcar.add(licensecar_auction)
                    val recyclerview_register = findViewById<View>(R.id.recyclerview_register) as RecyclerView?
                    val linearLayoutManager_register = LinearLayoutManager(context)
                    adapterRegister = AuctionAdapter(listcar,"register",user_id,status!!.toLong())
                    recyclerview_register!!.adapter = adapterRegister
                    linearLayoutManager_register.orientation = LinearLayoutManager.HORIZONTAL
                    recyclerview_register.layoutManager = linearLayoutManager_register
                }
            }

            override fun onFailure(call: Call<List<LicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }

    fun callWebserviceForMyAuction(context : Context,user_id : Int){
        val apiService = ApiInterface.create()
        val user = User(user_id)
        val users = GenericRequest<User>()
        var status  : String?  = null
        var objects : LicenseCar? = null
        var image : String? = null
        var number : String? = null
        var seq : Long?  = null
        val myRegisterAuctionList = ArrayList<Auction?>()
        val myAuctionList = ArrayList<Auction?>()
        val myHistoryAuctionList = ArrayList<Auction?>()
        var statisticList = ArrayList<Statistic>()
        var licensecar_auction : Auction?
        users.request = user
        val call = apiService.myAuction(users)
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>>{
            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?){
                val listlicenseCar = ArrayList<Auction>()
                if(response?.code() == 200) {
                    for (list in response.body().iterator()) {
                        //Log.d("JSON",)
                        objects = list.licenseCar
                        image = objects!!.imageLicenseCar
                        //Log.d("image",image)
                        number = objects!!.number
                        seq = list.licenseCarId
                        status = list.licenseCar!!.status
                        //var licenseCarId = objects.seq
                        licensecar_auction = Auction(seq, image, number, null, status)
                        if (status.equals("1")) {
                            myRegisterAuctionList.add(licensecar_auction)
                        } else if (status.equals("2")) {
                            myAuctionList.add(licensecar_auction)
                        } else {
                            myHistoryAuctionList.add(licensecar_auction)
                        }
                    }
                    countRegisterAuction.text = myRegisterAuctionList.size.toString()
                    countMyAuction.text = myAuctionList.size.toString()
                    countMyHistory.text = myHistoryAuctionList.size.toString()
                }else{
                }
            }

            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }
    fun callWebServiceForSetting(userId : Int){
        val apiService = ApiInterface.create()
        val User = GenericRequest<User>()
        var user = User()
        user.id = userId
        User.request = user
        val call = apiService.checkLogin(User)
        call.enqueue(object : retrofit2.Callback<List<User>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                if(response!!.code() == 200) {
                    for (userList in response.body().iterator()){
                        var name = userList.firstname.plus(" ").plus(userList.lastname)
//                        name_setting.text = name
//                        email_setting.text = userList.mail

                        val hView = nav_view.getHeaderView(0)
                        val textViewHeader = hView.findViewById<View>(R.id.textViewHeader) as TextView?
                        textViewHeader!!.text = name
                        val textViewMailHeader = hView.findViewById<View>(R.id.emailHeader) as TextView?
                        textViewMailHeader!!.text = userList.mail
                        val imageViewHeader = hView.findViewById<View>(R.id.imageViewHeader) as ImageView?
                        nav_view.background = getDrawable(R.drawable.gradiant_nav_view)
                        textViewName.text = userList.firstname
                    }
                }else{
                    Log.d("mesage","error")
                }
            }
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }
}
