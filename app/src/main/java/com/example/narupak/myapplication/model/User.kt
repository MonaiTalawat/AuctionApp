package com.example.narupak.myapplication.model

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import java.time.LocalDateTime

/**
 * Created by Narupak on 29/8/2561.
 */
class User {

    @SerializedName("id")
    var id : Int? = null

    @SerializedName("userId")
    var userId : Int? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("firstname")
    var firstname: String? = null

    @SerializedName("lastname")
    var lastname: String? = null

    @SerializedName("address")
    var address: String? = null
    @SerializedName("tel")
    var tel: String? = null

    @SerializedName("mail")
    var mail: String? = null

    constructor(username: String?, password: String?, firstname: String?, lastname: String?,address : String?, tel: String?, mail: String?) {
        this.username = username
        this.password = password
        this.firstname = firstname
        this.lastname = lastname
        this.address = address
        this.tel = tel
        this.mail = mail
    }

    constructor(userId: Int?) {
        this.userId = userId
    }

    constructor()


}

class Response{

    @SerializedName("mail")
    var res : List<User>? = null

    var listcar : LicenseCar? = null
}