package com.example.narupak.myapplication.model

/**
 * Created by Narupak on 31/8/2561.
 */
class DetailLicenseCar {

    var DRAWABLE = "drawable/"

    var seq: Long? = null
    var version: Long? = null
    var acutionDate: Long = 0
    var auctionTime: Long = 0
    var createBy: String? = null
    var createDate: Long = 0
    var endRegisterDate: Long = 0
    var firstprice: Long? = null
    var imageLicenseCar: String? = null
    var number: String? = null
    var startRegisterDate: Long = 0
    var status: String? = null
    var updateBy: String? = null
    var updateDate: Long = 0


    constructor(seq: Long?, version: Long?, acutionDate: Long, auctionTime: Long, createBy: String?, createDate: Long, endRegisterDate: Long, firstprice: Long?, imageLicenseCar: String?, number: String?, startRegisterDate: Long, status: String?, updateBy: String?, updateDate: Long) {
        this.seq = seq
        this.version = version
        this.acutionDate = acutionDate
        this.auctionTime = auctionTime
        this.createBy = createBy
        this.createDate = createDate
        this.endRegisterDate = endRegisterDate
        this.firstprice = firstprice
        this.imageLicenseCar = imageLicenseCar
        this.number = number
        this.startRegisterDate = startRegisterDate
        this.status = status
        this.updateBy = updateBy
        this.updateDate = updateDate
    }

    constructor(imageLicenseCar: String?) {
        this.imageLicenseCar = imageLicenseCar
    }

    fun getImgLicenseCarUrl(): String {
        return "drawable/" + imageLicenseCar
    }

    fun setImgLicenseCarUrl(imgUrl: String) {
        this.imageLicenseCar = imgUrl
    }
}