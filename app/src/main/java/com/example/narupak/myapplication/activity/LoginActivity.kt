package com.example.narupak.myapplication.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.editText_username
import com.example.narupak.myapplication.R.id.user
import com.example.narupak.myapplication.model.User
import com.example.narupak.myapplication.service.ApiInterface
import junit.runner.Version.id
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        register.setOnClickListener(View.OnClickListener {
            val intent = Intent(baseContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        })

        btn_login.setOnClickListener(View.OnClickListener {
            val username = editText_username.text.toString()
            val password = edittext_password.text.toString()
            val user = User(username,password,null,null,null,null,null)
            callWebservice(user)
        })

    }
    fun callWebservice(user : User){
        val apiService = ApiInterface.create()
        val users = GenericRequest<User>()
        users.request = user
        val call = apiService.checkLogin(users)
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                if (response?.code() == 200) {
                        for(list in response.body().listIterator()){
                            val intent = Intent(baseContext, MainMenuActivity::class.java)
                            intent.putExtra("user_id",list.id)
                            startActivity(intent)
                            finish()
                            //Toast.makeText(applicationContext,list.id.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
                else{
                    //Toast.makeText(applicationContext,"login failed",Toast.LENGTH_LONG).show()
                }
            }
                override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.d("failed", t.toString())
            }
        })
    }
}
