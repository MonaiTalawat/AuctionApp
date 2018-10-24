package com.example.narupak.myapplication.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.View
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.drawable.icon_registerauction2
import com.example.narupak.myapplication.R.id.tabLayoutMyAuction
import com.example.narupak.myapplication.R.id.viewPagerMyAuction
import com.example.narupak.myapplication.activity.fragment.AuctionFragment
import com.example.narupak.myapplication.activity.fragment.FinishAuctionFragment
import com.example.narupak.myapplication.activity.fragment.RegisterAuctionFragment
import com.example.narupak.myapplication.adapter.SectionPagerAdapter
import kotlinx.android.synthetic.main.activity_action_bar_tab.*
import kotlinx.android.synthetic.main.activity_action_bar_tab.view.*
import kotlinx.android.synthetic.main.image.view.*
import java.util.*
import android.support.design.widget.TabLayout



class ActionBarTabActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_bar_tab)

        val bundle = intent.extras
        val userId = bundle.getInt("user_id")
        val positionAuction = bundle.getInt("position_auction")
        // Making new tabs and adding to tabLayout
        tabLayoutMyAuction.addTab(tabLayoutMyAuction.newTab().setText("เปิดให้ลงทะเบียน"))
        tabLayoutMyAuction.addTab(tabLayoutMyAuction.newTab().setText("กำลังประมูล"))
        tabLayoutMyAuction.addTab(tabLayoutMyAuction.newTab().setText("ประวัติการประมูล"))

        val args = Bundle()
        args.putInt("user_id", userId)
        // Adding fragments to a list
        val fragments = ArrayList<Fragment>()
        fragments.add(Fragment.instantiate(this, RegisterAuctionFragment::class.java.getName(),args))
        fragments.add(Fragment.instantiate(this, AuctionFragment::class.java.getName(),args))
        fragments.add(Fragment.instantiate(this, FinishAuctionFragment::class.java.getName(),args))
        viewPagerMyAuction.setAdapter(SectionPagerAdapter(supportFragmentManager, fragments,userId,positionAuction))
        viewPagerMyAuction.setCurrentItem(positionAuction)
        // Attaching fragments into tabLayout with ViewPager
        tabLayoutMyAuction.setupWithViewPager(viewPagerMyAuction)
//        val view1 = layoutInflater.inflate(R.layout.image, null)
//        view1.findViewById<View>(R.id.icon).setBackgroundResource(R.drawable.icon_registerauction)
//        tabLayoutMyAuction.getTabAt(0)!!.customView = view1
//        tabLayoutMyAuction.getTabAt(0)!!.text = "เปิดให้ลงทะเบียน"
        tabLayoutMyAuction.getTabAt(0)!!.setIcon(getDrawable(R.drawable.icon_registerauction))
        tabLayoutMyAuction.getTabAt(1)!!.setIcon(getDrawable(R.drawable.icon_auction))
        tabLayoutMyAuction.getTabAt(2)!!.setIcon(getDrawable(R.drawable.icon_history))

        tabLayoutMyAuction.setSelectedTabIndicatorHeight(((5 * getResources().getDisplayMetrics().density).toInt()))
        tabLayoutMyAuction.setTabTextColors(Color.parseColor("#D3D3D3") ,Color.BLACK)
        tabLayoutMyAuction.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                if(position == 0){
                    tabLayoutMyAuction.background = getDrawable(R.drawable.gradient3)
                }else if(position == 1){
                    tabLayoutMyAuction.background = getDrawable(R.drawable.gradient1)
                }else{
                    tabLayoutMyAuction.background = getDrawable(R.drawable.gradiant4)
                }
            }
        })
        if(tabLayoutMyAuction.selectedTabPosition == 0){
            tabLayoutMyAuction.background = getDrawable(R.drawable.gradient3)
        }else if(tabLayoutMyAuction.selectedTabPosition == 1){
            tabLayoutMyAuction.background = getDrawable(R.drawable.gradient1)
        }else{
            tabLayoutMyAuction.background = getDrawable(R.drawable.gradiant4)
        }

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
