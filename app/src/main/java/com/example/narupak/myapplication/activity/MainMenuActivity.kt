package com.example.narupak.myapplication.activity

import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Response


class MainMenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var dataservice : DataService? = null
    var adapterAuction : AuctionAdapter? = null
    var adapterRegister : AuctionAdapter? = null
    var adapter_myauction : AuctionAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

       // val recyclerview_auction_main = findViewById<View?>(R.id.recyclerView_auction) as RecyclerView?

        var bundle = intent.extras
        var userid = bundle.getInt("user_id")
        callWebserviceForAuction(this,userid)
        callWebserviceForRegisterAuction(this,userid)
        callWebserviceForMyAuction(this,userid)

        nav_view.setNavigationItemSelectedListener(this)

        more_auction.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
            intent.putExtra("name","auction")
            intent.putExtra("user_id",userid)
            startActivity(intent)
        })
        more_register_auction.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
            intent.putExtra("name","register")
            intent.putExtra("user_id",userid)
            startActivity(intent)
        })
        myauction_more.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
            intent.putExtra("name","myauction")
            intent.putExtra("user_id",userid)
            startActivity(intent)
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
                }else{
                    val intent = Intent(baseContext, SettingActivity::class.java)
                    intent.putExtra("user_id",userId)
                    startActivity(intent)
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
            }
            R.id.register_auction -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
                intent.putExtra("name","register")
                intent.putExtra("user_id",userId)
                startActivity(intent)
            }
            R.id.my_auction -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                val intent = Intent(baseContext, RecyclerViewForMoreActivity::class.java)
                intent.putExtra("name","myauction")
                intent.putExtra("user_id",userId)
                startActivity(intent)
            }
            R.id.notification -> {
                val intent = Intent(baseContext, NotificationActivity::class.java)
                startActivity(intent)
            }
            R.id.setting -> {
                var bundle = intent.extras
                var userId = bundle.getInt("user_id")
                var intent = Intent(baseContext, SettingActivity::class.java)
                intent.putExtra("name","myauction")
                intent.putExtra("user_id",userId)
                startActivity(intent)
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
                    adapterAuction = AuctionAdapter(listcar,"auction",userId,status)
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

                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
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
                    adapterRegister = AuctionAdapter(listcar,"register",user_id,status)
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
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
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
        users.request = user
        val call = apiService.myAuction(users)
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>>{
            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?){
                val listlicenseCar = ArrayList<Auction>()
                if(response?.code() == 200){
                    for (list in response.body().iterator()){
                        //Log.d("JSON",)
                        var objects = list.licenseCar
                        val image = objects!!.imageLicenseCar
                        //Log.d("image",image)
                        val number = objects.number
                        val seq = list.licenseCarId
                        status = list.licenseCar!!.status
                        //var licenseCarId = objects.seq
                        val licensecar_auction = Auction(seq,image,number,null,status)
                        listlicenseCar.add(licensecar_auction)
                    }
                    val myrecyclerview = findViewById<View>(R.id.myrecyclerview) as RecyclerView?
                    val mylinearLayoutmanager = LinearLayoutManager(context)
                    adapter_myauction = AuctionAdapter(listlicenseCar,"myauction",user_id,status)
                    myrecyclerview!!.adapter = adapter_myauction
                    mylinearLayoutmanager.orientation = LinearLayoutManager.HORIZONTAL

                    myrecyclerview.layoutManager = mylinearLayoutmanager

                    val myduration : Long = 3000
        //val pixelsToMove = 30
        var myposition = 0
        val mymHandler = android.os.Handler(Looper.getMainLooper())
        val MY_SCROLLING_RUNNABLE = object : Runnable {
            override fun run() {
                //recyclerview_auction_main.smoothScrollBy(pixelsToMove, 0)
                if(myposition < listlicenseCar.size){
                    //Log.d("time",myposition.toString())
                    myrecyclerview.smoothScrollToPosition(myposition)
                    myposition++
                }else{
                    //Log.d("time",myposition.toString())
                    myposition = 0
                }
                mymHandler.postDelayed(this, myduration)
            }
        }
        myrecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem = mylinearLayoutmanager.findLastCompletelyVisibleItemPosition()
                if (lastItem == mylinearLayoutmanager.getItemCount() - 1) {
                    mymHandler.removeCallbacks(MY_SCROLLING_RUNNABLE)
                    val postHandler = android.os.Handler()
                    postHandler.postDelayed(Runnable {
                        myrecyclerview.adapter = adapter_myauction
                        mymHandler.postDelayed(MY_SCROLLING_RUNNABLE, 2000)
                    }, 2000)
                }
            }
        })
        mymHandler.postDelayed(MY_SCROLLING_RUNNABLE, 3000)
                }else{
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }
}
