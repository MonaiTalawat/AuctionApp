package com.example.narupak.myapplication.activity

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.model.RegisterAuctionLicenseCar
import com.example.narupak.myapplication.model.ResponseRegister
import com.example.narupak.myapplication.model.User
import com.example.narupak.myapplication.service.ApiInterface
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_setting.*
import retrofit2.Call
import retrofit2.Response

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        var bundle = intent.extras
        var userId = bundle.getInt("user_id")
        callWebServiceForSetting(userId)
        btn_logout.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext,LoginActivity::class.java)
            startActivity(intent)
            finish()
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
                        name_setting.text = name
                        email_setting.text = userList.mail
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

    override fun onBackPressed() {
        super.onBackPressed()
        var bundle = intent.extras
        var userId = bundle.getInt("user_id")
        var intent = Intent(baseContext,MainMenuActivity::class.java)
        intent.putExtra("user_id",userId)
        startActivity(intent)
        finish()
    }
}
