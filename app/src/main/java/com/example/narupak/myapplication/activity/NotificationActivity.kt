package com.example.narupak.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.NotificationCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.AdapterNoti
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.R.id.notification
import android.app.NotificationManager
import android.content.Context
import com.example.narupak.myapplication.R.layout.activity_notification
import kotlinx.android.synthetic.main.activity_notification.*
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri


class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        var recyclerview_notification = findViewById<View>(R.id.recyclerview_noti) as (RecyclerView)
        recyclerview_notification.setHasFixedSize(true)
        var listnoti = ArrayList<Auction>()
        listnoti.add(Auction("tabian1"))
        listnoti.add(Auction("tabian2"))
        listnoti.add(Auction("tabian3"))
        val linearLayoutManager = LinearLayoutManager(this)
        var adapterNoti = AdapterNoti(listnoti)
        recyclerview_notification.adapter = adapterNoti
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerview_notification.layoutManager = linearLayoutManager
        button2.setOnClickListener(View.OnClickListener {
            showNotification()
        })
    }

    fun showNotification(){

        val intent = Intent(this, MainMenuActivity::class.java)
        //intent.putExtra("message", MESSAGE)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainMenuActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this) // this is context
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("DevAhoy News")
                .setContentText("สวัสดีครับ ยินดีต้อนรับเข้าสู่บทความ Android Notification :)")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1000, notification)
    }
}
