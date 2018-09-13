package com.example.narupak.myapplication.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.narupak.myapplication.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
            }

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }).start()
    }
}
