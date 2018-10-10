package com.example.narupak.myapplication.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Narupak on 5/9/2561.
 */
class AuctionRealtimeDatabase {

    var bidder : Long? = null
    var bidtime : Long? = null
    var status : String? = null
    var firstTime : Long? = null
    var price : Long? = null


    constructor()
    constructor(bidder: Long?, bidtime: Long?, status: String?, firstTime: Long?, price: Long?) {
        this.bidder = bidder
        this.bidtime = bidtime
        this.status = status
        this.firstTime = firstTime
        this.price = price
    }


}

class Mapdata{
    var id : String? = null
    var bidTime : String? = null
    var price : String? = null

    constructor(id: String?, userName: String?, price: String?) {
        this.id = id
        this.bidTime = userName
        this.price = price
    }
}