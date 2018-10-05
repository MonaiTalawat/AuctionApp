package com.example.narupak.myapplication.model

/**
 * Created by Narupak on 5/9/2561.
 */
class AuctionRealtimeDatabase {
    var bidder : String? = null
    var bidtime : String? = null
    var status : String? = null
    var firstTime : String? = null
    var price : Long? = null


    constructor()
    constructor(bidder: String?, bidtime: String?, status: String?, firstTime: String?, price: Long?) {
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