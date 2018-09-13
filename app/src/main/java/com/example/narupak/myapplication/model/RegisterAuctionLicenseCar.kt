package com.example.narupak.myapplication.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by Narupak on 31/8/2561.
 */
class RegisterAuctionLicenseCar {

    var seq: Long? = null

    @SerializedName("licenseCarVM")
    var licenseCar : LicenseCar? = null
    var registerDate: Date? = null

    @SerializedName("userVM")
    var user: User? = null
    //private long userId;
    var userId : Int? = null
    var licenseCarId : Long? = null

    constructor(user_id: Int?, license_car_id: Long?) {
        this.userId = user_id
        this.licenseCarId = license_car_id
    }

    constructor(seq: Long?, licenseCar: LicenseCar?, registerDate: Date?, user: User?) {
        this.seq = seq
        this.licenseCar = licenseCar
        this.registerDate = registerDate
        this.user = user
    }

    constructor()


}