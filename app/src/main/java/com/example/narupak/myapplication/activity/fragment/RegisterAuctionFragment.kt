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
import com.example.narupak.myapplication.adapter.MoreAdapter
import com.example.narupak.myapplication.model.Auction
import com.example.narupak.myapplication.model.RegisterAuctionLicenseCar
import com.example.narupak.myapplication.model.User
import com.example.narupak.myapplication.service.ApiInterface
import kotlinx.android.synthetic.main.activity_recycler_view_for_my_auction.*
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegisterAuctionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegisterAuctionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterAuctionFragment : Fragment() {

    var userId : Int? = null
    var recyclerView_moreRegisterAuction :RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        userId = args.getInt("user_id")
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        callWebserviceForMyRegisterAuction(this.context,userId!!)
        val view = inflater!!.inflate(R.layout.fragment_register_auction, container, false)
        recyclerView_moreRegisterAuction = view!!.findViewById<View>(R.id.recyclerViewForMoreMyRegisterAuction) as? RecyclerView
        return view
    }

    fun callWebserviceForMyRegisterAuction(context : Context, userId : Int){
        val apiService = ApiInterface.create()
        val user = User(userId)
        val users = GenericRequest<User>()
        var status : String? = null
        var moreRegisterAuctionList = ArrayList<Auction>()
        users.request = user
        val call = apiService.myAuction(users)
        Log.d("REQUEST", call.toString() + "")
        call.enqueue(object : retrofit2.Callback<List<RegisterAuctionLicenseCar>>{
            override fun onResponse(call: Call<List<RegisterAuctionLicenseCar>>?, response: Response<List<RegisterAuctionLicenseCar>>?){
                val listlicenseCar = ArrayList<Auction>()
                if(response?.code() == 200){

                    for (list in response.body().iterator()){
                        //Log.d("JSON",)
                        var objects = list.licenseCar
                        val image = objects!!.imageLicenseCar
                        //Log.d("image",image)
                        val number = objects.number
                        val seq = objects.seq
                        status = objects.status
                        val licensecar_auction = Auction(seq,image,number,null,status)
                        listlicenseCar.add(licensecar_auction)
                    }
                    for(moreList in listlicenseCar){
                        if(moreList.status.equals("1")) {
                            moreRegisterAuctionList.add(moreList)
                        }
                    }
                    var adapterRegisterAuction : MoreAdapter? = null
//                    header_more.text = "ทะเบียนรถของฉันเพิ่มเติม"
                    var type_page = "myauction"

                    /////////////////////////// set RecyclerViewMoreRegisterAuction//////////////////////////////////
                    adapterRegisterAuction = MoreAdapter(moreRegisterAuctionList,type_page,userId)
                    recyclerView_moreRegisterAuction!!.adapter = adapterRegisterAuction
                    val linearLayoutManagerMoreRegisterAuction = LinearLayoutManager(context)
                    linearLayoutManagerMoreRegisterAuction.orientation = LinearLayoutManager.VERTICAL
                    recyclerView_moreRegisterAuction!!.layoutManager = linearLayoutManagerMoreRegisterAuction
                    /////////////////////////// set RecyclerViewMoreRegisterAuction//////////////////////////////////

                }else{
                    Toast.makeText(context,"failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<RegisterAuctionLicenseCar>>?, t: Throwable?) {
                Log.d("failed",t.toString())
            }
        })
    }

}// Required empty public constructor
