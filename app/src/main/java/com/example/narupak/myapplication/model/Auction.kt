package com.example.narupak.myapplication.model

/**
 * Created by Narupak on 22/8/2561.
 */
class Auction {
    val DRAWABLE = "drawable/"

    private var imgUrl: String? = null
    var seq: Long? = null
    var imageLicenseCar: String? = null
    var number: String? = null
    var licenseCarId : Long? = null
    var firstpriceLicenseCar : Long? = null
    var status : String? = null

    constructor(imgUrl: String?) {
        this.imgUrl = imgUrl
    }

    constructor(seq: Long?, imageLicenseCar: String?, number: String?, firstpriceLicenseCar: Long?, status: String?) {
        this.seq = seq
        this.imageLicenseCar = imageLicenseCar
        this.number = number
        this.firstpriceLicenseCar = firstpriceLicenseCar
        this.status = status
    }


    fun getImgUrl(): String {
        return DRAWABLE + imageLicenseCar
    }

    fun setImgUrl(imgUrl: String) {
        this.imgUrl = imgUrl
    }


}

class ResponseRegister{
    var responses : Responses? = null
}

class Responses{
    var result = null
    var message = null
}