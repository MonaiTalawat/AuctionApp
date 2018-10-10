package com.example.narupak.myapplication.service

import com.example.narupak.myapplication.GenericRequest
import com.example.narupak.myapplication.activity.HistoryAuctionActivity
import com.example.narupak.myapplication.model.*
import com.example.narupak.myapplication.viewholder.HistoryAuctionViewholder
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created by Narupak on 29/8/2561.
 */
interface ApiInterface {
    @POST("registerUser")
    abstract fun registerUser(@Body user : GenericRequest<User>) : Call<User>

    @POST("checkAuthen")
    abstract fun checkLogin(@Body user : GenericRequest<User>) : Call<List<User>>

    @GET("licenseCar")
    abstract fun queryLicenseCarByStatus() : Call<List<LicenseCar>>

    @GET("registerLicenseCar")
    abstract fun queryRegisterLicenseCarByStatus() : Call<List<LicenseCar>>

    @GET("queryLicenseCarDetailById")
    abstract fun queryLicenseCarDetailById(@Query("licenseCarId")id : Long) : Call<List<DetailLicenseCar>>

    @POST("registerAuctionLicenseCar")
    abstract fun registerAuctionLicenseCar(@Body register : GenericRequest<RegisterAuctionLicenseCar>) : Call<ResponseRegister>

    @POST("queryMyAuction")
    abstract fun myAuction(@Body User :GenericRequest<User>) : Call<List<RegisterAuctionLicenseCar>>

    @POST("checkRegisterAuction")
    abstract fun checkRegisterAuction(@Body registerAuctionLicenseCar: GenericRequest<RegisterAuctionLicenseCar>) : Call<List<RegisterAuctionLicenseCar>>

    @GET("queryMemberRegisterAuctionLicenseCar")
    abstract fun queryMemberRegisterAuctionLicenseCar(@Query("license_car_id") licenseCarId : Int) : Call<List<RegisterAuctionLicenseCar>>

    @GET("getCurrentTimeStamp")
    abstract fun queryCurrentTimeStamp(): Call<Long>

    @POST("updateStatusLicenseCar")
    abstract fun updateStatusLicenseCar(@Query("licenseCarId") licenseCarId : Long) : Call<LicenseCar>

    @POST("insertSaveAuction")
    abstract fun saveAuction(@Body dataAuction : GenericRequest<WinnerAuction>) : Call<WinnerAuction>

    @GET("getCurrentTimeStamp")
    abstract fun currentTimeStamp() : Call<Long>

    @POST("insertHistory")
    abstract fun saveHistory(@Body history: GenericRequest<SaveHistory>) : Call<SaveHistory>

    @POST("queryHistoryAuctionByUserId")
    abstract fun queryHistoryByUserId(@Query("userId") userId : Long) : Call<List<Long>>

    @POST("querySaveAuctionByLicenseCarId")
    abstract fun querySaveAuctionByLicenseCarId(@Query("licenseCarId") licenseCarId: Long) : Call<List<SaveAuction>>

    companion object Factory {
        val client = OkHttpClient()
        val BASE_URL = "http://157.179.133.174:8080/"
        fun create(): ApiInterface {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build()

            val gson = GsonBuilder()
                    .setDateFormat("yyyy-mm-dd")
                    .setLenient()
                    .create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}