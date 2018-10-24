package com.example.narupak.myapplication.activity.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.narupak.myapplication.GenericRequest

import com.example.narupak.myapplication.R
import com.example.narupak.myapplication.adapter.AdapterHistoryAuction
import com.example.narupak.myapplication.adapter.MoreAdapter
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.service.ApiInterface
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FinishAuctionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FinishAuctionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinishAuctionFragment : Fragment() {

    var userId : Int? = null
    var AdapterHistoryAuction : AdapterHistoryAuction? = null
    var saveAuctionHistoryList = ArrayList<SaveAuctionHistory>()
    var recyclerViewForMoreMyFinishAuction : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        userId = args.getInt("user_id")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //callWebserviceForMyRegisterAuction(this.context,userId!!)
        val view = inflater!!.inflate(R.layout.fragment_finish_auction, container, false)
        recyclerViewForMoreMyFinishAuction = view!!.findViewById<View>(R.id.recyclerViewForMoreMyFinishAuction) as? RecyclerView
        callWebServiceForHistory(userId!!)
        return view
    }
    fun callWebServiceForHistory(userId: Int){
        val apiService = ApiInterface.create()
        val call = apiService.queryHistoryByUserId(userId)
        call.enqueue(object : retrofit2.Callback<List<SaveAuction>> {
            override fun onResponse(call: Call<List<SaveAuction>>?, response: Response<List<SaveAuction>>?) {
                if(response!!.code() == 200) {
                        var saveAuction = response.body()
                        for (save in saveAuction) {
                            var endAuctionDate = save.endAuctionDate
                            var finalprice = save.finalprice
                            val licenseCar = save.licenseCarsVM
                            var imageLicenseCar = licenseCar!!.imageLicenseCar
                            var numberLicenseCar = licenseCar.number
                            var seq = licenseCar.seq
                            val user = save.userVM
                            var firstName = user!!.firstname
                            var lastName = user.lastname
                            var id = user.id
                            var saveAuctionHistory = SaveAuctionHistory()
                            saveAuctionHistory.endAuctionDate = endAuctionDate
                            saveAuctionHistory.finalprice = finalprice
                            saveAuctionHistory.imageLicenseCar = imageLicenseCar
                            saveAuctionHistory.firstName = firstName
                            saveAuctionHistory.lastName = lastName
                            saveAuctionHistory.numberLicenseCar = numberLicenseCar
                            saveAuctionHistory.id = id!!.toLong()
                            saveAuctionHistory.seq = seq
                            saveAuctionHistoryList.add(saveAuctionHistory)
                        }
                    val linearLayoutManager = LinearLayoutManager(context)
                    AdapterHistoryAuction = AdapterHistoryAuction(saveAuctionHistoryList,userId!!)
                    recyclerViewForMoreMyFinishAuction!!.adapter = AdapterHistoryAuction
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerViewForMoreMyFinishAuction!!.layoutManager = linearLayoutManager
                    saveAuctionHistoryList = ArrayList<SaveAuctionHistory>()
                }else{
                    Log.d("failed","failed")
                }
            }
            override fun onFailure(call: Call<List<SaveAuction>>?, t: Throwable?) {

            }

        })
    }
//    fun callWebServiceForQuerySaveAuction(licenseCarId: Long, userId: Int) : ArrayList<SaveAuctionHistory>{
//        val apiService = ApiInterface.create()
//        val call = apiService.querySaveAuctionByLicenseCarId(licenseCarId)
//        //var saveAuctionHistoryLists = ArrayList<SaveAuctionHistory>()
//        call.enqueue(object : retrofit2.Callback<ArrayList<SaveAuction>> {
//            override fun onResponse(call: Call<ArrayList<SaveAuction>>?, response: Response<ArrayList<SaveAuction>>?) {
//                if(response!!.code() == 200) {
//                    var saveAuction = response.body()
//                    Log.d("saveAuction",saveAuction.toString())
//                    for(save in saveAuction){
//                        var endAuctionDate = save.endAuctionDate
//                        var finalprice = save.finalprice
//                        val licenseCar = save.licenseCarsVM
//                        var imageLicenseCar = licenseCar!!.imageLicenseCar
//                        var numberLicenseCar = licenseCar.number
//                        var seq = licenseCar.seq
//                        val user = save.userVM
//                        var firstName = user!!.firstname
//                        var lastName = user.lastname
//                        var id = user.id
//                        var saveAuctionHistory = SaveAuctionHistory()
//                        saveAuctionHistory.endAuctionDate = endAuctionDate
//                        saveAuctionHistory.finalprice = finalprice
//                        saveAuctionHistory.imageLicenseCar = imageLicenseCar
//                        saveAuctionHistory.firstName = firstName
//                        saveAuctionHistory.lastName = lastName
//                        saveAuctionHistory.numberLicenseCar = numberLicenseCar
//                        saveAuctionHistory.id = id!!.toLong()
//                        saveAuctionHistory.seq = seq
//                        saveAuctionHistoryList.add(saveAuctionHistory)
//                        // Log.d("saveAuctionHistoryList",saveAuctionHistoryList.toString())
//                    }
//
//                }else{
//                    Log.d("failed","failed")
//                }
//            }
//            override fun onFailure(call: Call<ArrayList<SaveAuction>>?, t: Throwable?) {
//                Log.d("failed",t.toString())
//            }
//
//        })
//        return saveAuctionHistoryList
//    }
}// Required empty public constructor
