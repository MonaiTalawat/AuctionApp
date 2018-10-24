package com.example.narupak.myapplication.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.narupak.myapplication.activity.fragment.RegisterAuctionFragment
import java.util.*
import kotlin.collections.ArrayList
import android.os.Bundle
import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.R.id.tabLayoutMyAuction
import com.example.narupak.myapplication.activity.fragment.AuctionFragment
import com.example.narupak.myapplication.activity.fragment.FinishAuctionFragment
import kotlinx.android.synthetic.main.activity_action_bar_tab.*


/**
 * Created by Narupak on 18/10/2561.
 */
class SectionPagerAdapter : FragmentPagerAdapter {
    private val fragments: ArrayList<Fragment>
    private var userId : Int? = null
    private  var positionAuction : Int? = null

    constructor(fm: FragmentManager?, fragments: ArrayList<Fragment>,userId : Int?,positionAuction: Int) : super(fm) {
        this.fragments = fragments
        this.userId = userId
        this.positionAuction = positionAuction
    }


    override fun getItem(position: Int): Fragment {
        return this.fragments.get(position)
    }

    override fun getCount(): Int {
        return this.fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> {
                return "เปิดให้ลงทะเบียน"
            }
            1 -> {
                return "กำลังประมูล"
            }
            else -> {
                return "สิ้นสุดการประมูล"
            }
        }
    }

}