package com.example.narupak.myapplication.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by Narupak on 30/8/2561.
 */
class LicenseCar {
    var seq: Long? = null
    var auctionDate: Long? = null
    var auctionTime: Long? = null
    var endRegisterDate: Long? = null
    var firstprice: Long? = null
    var imageLicenseCar: String? = null
    var number: String? = null
    var startRegisterDate: Long? = null
    var status: String? = null

    constructor(seq: Long?, imageLicenseCar: String?, number: String?) {
        this.seq = seq
        this.imageLicenseCar = imageLicenseCar
        this.number = number
    }

    constructor(seq: Long?, auctionDate: Long?, auctionTime: Long, endRegisterDate: Long?, firstprice: Long?, imageLicenseCar: String?, number: String?, startRegisterDate : Long?, status: String?) {
        this.seq = seq
        this.auctionDate = auctionDate
        this.auctionTime = auctionTime
        this.endRegisterDate = endRegisterDate
        this.firstprice = firstprice
        this.imageLicenseCar = imageLicenseCar
        this.number = number
        this.startRegisterDate = startRegisterDate
        this.status = status
    }

    constructor()


}

class ResponseLicenseCar{
    var listcar : LicenseCar? = null
}