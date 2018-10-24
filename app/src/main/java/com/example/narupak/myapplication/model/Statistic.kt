package com.example.narupak.myapplication.model

/**
 * Created by Narupak on 18/10/2561.
 */
class Statistic {
    var countAuction : Int? = null
    var typeAuction : String? = null

    constructor()

    constructor(countAuction: Int?, typeAuction: String?) {
        this.countAuction = countAuction
        this.typeAuction = typeAuction
    }


}