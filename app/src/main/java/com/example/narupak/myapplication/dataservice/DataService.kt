package com.example.narupak.myapplication.dataservice

import com.example.narupak.myapplication.model.Auction

/**
 * Created by Narupak on 22/8/2561.
 */
class DataService {

    val ourInstance : DataService = DataService()

    fun getInstance(): DataService {
        return ourInstance
    }

    fun getimageAuction(): ArrayList<Auction> {

        val list = ArrayList<Auction>()
        list.add(Auction("tabian1"))
        list.add(Auction("tabian2"))
        list.add(Auction("tabian3"))
        return list
    }

}