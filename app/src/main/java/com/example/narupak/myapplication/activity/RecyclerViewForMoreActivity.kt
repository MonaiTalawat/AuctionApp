package com.example.narupak.myapplication.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.narupak.myapplication.adapter.MoreAdapter
import com.example.narupak.myapplication.model.Auction
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.LicenseCar
import com.example.narupak.myapplication.model.RegisterAuctionLicenseCar
import com.example.narupak.myapplication.model.User
import com.example.narupak.myapplication.service.ApiInterface
import kotlinx.android.synthetic.main.activity_recycler_view_for_more.*
import retrofit2.Call
import retrofit2.Response


class RecyclerViewForMoreActivity : AppCompatActivity() {

    var ListAuction = ArrayList<Auction>()
    var type_page : String?  = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_for_more)
        var bundle = intent.extras
        //Toast.makeText(applicationContext,bundle.getString("name"),Toast.LENGTH_LONG).show()
        var userId = bundle.getInt("user_id")
        if(bundle.getString("name").equals("auction")){
            callWebserviceForMoreAuction(this,userId)
            layout_for_more.background = getDrawable(R.drawable.background_for_moreauction)
        }else if(bundle.getString("name").equals("register")){
            callWebserviceForMoreRegisterAuction(this,userId)
            layout_for_more.background = getDrawable(R.drawable.background_for_registerauction)
        }else{
            callWebserviceForMyAuctionRecyclerView(this,userId)
        }
    }

    fun callWebserviceForMoreAuction(context : Context,userId : Int?){
        val apiService = ApiInterface.create()
        val users = GenericRequest<User>()
        var listLicenseCar : List<LicenseCar>? = null
        var number : String? = null
        var image : String? = null
        var seq : Long? = null
        val call = apiService.queryLicenseCarByStatus()
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<LicenseCar>>{
            override fun onResponse(call: Call<List<LicenseCar>>?, response: Response<List<LicenseCar>>?){
                val listcar = ArrayList<Auction>()
                if(response?.code() == 200){
                    val recyclerView_more = findViewById<View>(R.id.recyclerview_more) as RecyclerView
                    recyclerView_more.setHasFixedSize(true)
                    for(carlist in response.body().listIterator()){
                        number = carlist.number
                        image = carlist.imageLicenseCar
                        seq = carlist.seq
                        val licensecar_auction = Auction(seq,image,number,null)
                        listcar!!.add(licensecar_auction)
                    }
                    var adapter_more : MoreAdapter? = null
                    header_more.text = "ทะเบียนรถที่กำลังประมูลเพิ่มเติม"
                    type_page = "auction"
                    adapter_more = MoreAdapter(listcar,type_page.toString(),userId)
                    recyclerView_more.adapter = adapter_more
                    val linearLayoutManager = LinearLayoutManager(baseContext)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_more.layoutManager = linearLayoutManager
                }else{
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<LicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }

    fun callWebserviceForMoreRegisterAuction(context : Context,userId : Int?){
        val apiService = ApiInterface.create()
        val users = GenericRequest<User>()
        var listLicenseCar : List<LicenseCar>? = null
        var number : String? = null
        var image : String? = null
        var seq : Long? = null
        val call = apiService.queryRegisterLicenseCarByStatus()
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<LicenseCar>>{
            override fun onResponse(call: Call<List<LicenseCar>>?, response: Response<List<LicenseCar>>?){
                val listcar = ArrayList<Auction>()
                if(response?.code() == 200){
                    val recyclerView_more = findViewById<View>(R.id.recyclerview_more) as RecyclerView
                    recyclerView_more.setHasFixedSize(true)
                    for(carlist in response.body().listIterator()){
                        number = carlist.number
                        image = carlist.imageLicenseCar
                        seq = carlist.seq
                        val licensecar_auction = Auction(seq,image,number,null)
                        listcar.add(licensecar_auction)
                    }
                    var adapter_more : MoreAdapter? = null
                    header_more.text = "ทะเบียนรถที่เปิดให้ลงทะเบียนเพิ่มเติม"
                    type_page = "register"
                    adapter_more = MoreAdapter(listcar,type_page.toString(),userId)
                    recyclerView_more.adapter = adapter_more
                    val linearLayoutManager = LinearLayoutManager(baseContext)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_more.layoutManager = linearLayoutManager
                }else{
                    Toast.makeText(applicationContext,"failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<LicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }

    fun callWebserviceForMyAuctionRecyclerView(context : Context,user_id : Int){
        val apiService = ApiInterface.create()
        val user = User(user_id)
        val users = GenericRequest<User>()
        users.request = user
        val call = apiService.myAuction(users)
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>>{
            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?){
                val listlicenseCar = ArrayList<Auction>()
                if(response?.code() == 200){
                    val recyclerView_more = findViewById<View>(R.id.recyclerview_more) as RecyclerView
                    for (list in response.body().iterator()){
                        //Log.d("JSON",)
                        var objects = list.licenseCar
                        val image = objects!!.imageLicenseCar
                        //Log.d("image",image)
                        val number = objects.number
                        val seq = objects.seq
                        val licensecar_auction = Auction(seq,image,number,null)
                        listlicenseCar.add(licensecar_auction)
                    }
                    var adapter_more : MoreAdapter? = null
                    header_more.text = "ทะเบียนรถของฉันเพิ่มเติม"
                    type_page = "myauction"
                    adapter_more = MoreAdapter(listlicenseCar,type_page.toString(),user_id)
                    recyclerView_more.adapter = adapter_more
                    val linearLayoutManager = LinearLayoutManager(baseContext)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_more.layoutManager = linearLayoutManager
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
        var bundle = intent.extras
        var userId = bundle.getInt("user_id")
        var intent = Intent(baseContext,MainMenuActivity::class.java)
        intent.putExtra("user_id",userId)
        startActivity(intent)
        finish()
    }



}
