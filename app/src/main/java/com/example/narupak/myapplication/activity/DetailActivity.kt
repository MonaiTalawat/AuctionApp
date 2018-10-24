package com.example.narupak.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Callback


class DetailActivity : AppCompatActivity() {

    var amountRegisterAuctionLicenseCar : Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val bundle = intent.extras
        val licenseCarId = bundle.getLong("licenseCarId")
        val userId = bundle.getInt("user_id")
        countRegisterAuction(licenseCarId)
        val registerAucrion = RegisterAuctionLicenseCar(userId,licenseCarId)
        val apiService = ApiInterface.create()
        val call = apiService.queryLicenseCarDetailById(licenseCarId)
        call.enqueue(object : retrofit2.Callback<List<DetailLicenseCar>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<DetailLicenseCar>>?, response: Response<List<DetailLicenseCar>>?) {
                if(response?.code() == 200){
                    //Log.d("list",response.body().toString())
                    for (list in response.body().listIterator()){
                        Log.d("list",list.acutionDate.toString())
                        val startRegisterDate = Date(list.acutionDate*1000)
                        val endRegisterDate = Date(list.endRegisterDate*1000)
                        val df = SimpleDateFormat("d MMMM พ.ศ. yyyy",Locale("th","TH"))
                        startdate_register.text = df.format(startRegisterDate)
                        enddate_register.text = df.format(endRegisterDate)
                        firstprice.text = list.firstprice.toString()
                        val url = list.getImgLicenseCarUrl()
                        Log.d("list",list.getImgLicenseCarUrl())
                        val resource = image_detail!!.getResources().getIdentifier(url, null, image_detail!!.getContext().getPackageName())
                        image_detail!!.setImageResource(resource)
                        count_person.text = amountRegisterAuctionLicenseCar.toString()
                    }
                }else{
                    Log.d("fail","error")
                }
            }
            override fun onFailure(call: Call<List<DetailLicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
        checkRegisterAuction(userId,licenseCarId,this)
        btn_register_auction.setOnClickListener(View.OnClickListener {
            callWebServiceForRegisterAuction(registerAucrion,this)
        })
    }

    private fun checkRegisterAuction(userId: Int, licenseCarId: Long,context: Context) {
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
        call.enqueue(object : Callback<List<RegisterAuctionLicenseCar>>{
            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?) {
               if(response!!.code() == 200){
                   btn_register_auction.visibility = GONE
                   val layout = findViewById<View>(R.id.layout_for_button) as ConstraintLayout
                   val imageView = ImageView(context)
                   imageView.setImageResource(R.drawable.ic_check_black_24dp)
                   layout.addView(imageView)
               }else{
                   Log.d("error",response.message())
               }
            }

            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {

            }
        })
    }

    private fun callWebServiceForRegisterAuction(registerAuction  : RegisterAuctionLicenseCar,context : Context){
        val apiService = ApiInterface.create()
        val registerAuctionList = GenericRequest<RegisterAuctionLicenseCar>()
        registerAuctionList.request = registerAuction
        val call = apiService.registerAuctionLicenseCar(registerAuctionList)
        call.enqueue(object : retrofit2.Callback<ResponseRegister> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<ResponseRegister>?, response: Response<ResponseRegister>?) {
                if(response!!.code() == 200) {
                    btn_register_auction.visibility = GONE
                    //btn_register_auction.text = ""
                    val layout = findViewById<View>(R.id.layout_for_button) as ConstraintLayout
                    val imageView = ImageView(context)
                    imageView.setImageResource(R.drawable.ic_check_black_24dp)
                    layout.addView(imageView)
                }else{
                    Log.d("mesage","error")
                }
            }
            override fun onFailure(call: Call<ResponseRegister>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }

    private fun countRegisterAuction(licenseCarId: Long) {
        val apiService = ApiInterface.create()
        val call = apiService.countRegisterAuction(licenseCarId)
        call.enqueue(object : Callback<Long>{
            override fun onResponse(call: Call<Long>?, response: Response<Long>?) {
                var amountRegisterAuction = response!!.body()
                amountRegisterAuctionLicenseCar = amountRegisterAuction
            }

            override fun onFailure(call: Call<Long>?, t: Throwable?) {

            }
        })
    }
    override fun onBackPressed() {
        val bundle = intent.extras
        val userId = bundle.getInt("user_id")
        val intent = Intent(baseContext,MainMenuActivity::class.java)
        intent.putExtra("user_id",userId)
        startActivity(intent)
        finish()
    }

}
