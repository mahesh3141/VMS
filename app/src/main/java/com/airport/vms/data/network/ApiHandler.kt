package com.airport.vms.data.network

import com.airport.vms.ui.models.LoginModel
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import com.airport.vms.BuildConfig
import retrofit2.http.GET

object ApiHandler {
    //private  val DOMAIN = "http://www.traxnx.com"
    private  val DOMAIN = "http://www.swscart.com"
    //vms/get_all_zone
    private var retrofit: Retrofit
    private val headers = HashMap<String, String>()

    init {
        val interceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        retrofit = Retrofit.Builder().client(client).baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
     fun getServiceMethods(): APIInterface? {
        return retrofit.create(APIInterface::class.java)
    }

    fun getDataObject(data: Any, modelName: String): Any {
        val gson = Gson()
        val tmpData = gson.toJson(data)
        val model = Class.forName(modelName)
        return gson.fromJson(tmpData, model)
    }

    //send Type like this : val type = object : TypeToken<ArrayList<ModelName>>() {}.type
    fun getDataArray(data: Any, modelName: Type): ArrayList<Any> {
        val gson = Gson()
        val tmpData = gson.toJson(data)
        return gson.fromJson(tmpData, modelName)
    }

    fun getCallBack(listener: ICallBack?, action: String): Callback<Any> {
        return object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                listener?.onFailureReceived(action, t)
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                listener?.onCallBackReceived(action, response)
            }
        }
    }
}
interface APIInterface {
    @POST("/vms/login?")
    @FormUrlEncoded
    fun doLogin(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<Any>?

    @GET("/vms/get_all_zone")
    fun getAllZone(): Call<Any>?

    @POST("/vms/submit_scan_data")
    @FormUrlEncoded
    fun scanQR(@Field("qrData") qrData:String?):Call<Any>?

    @GET("/vms/dashboard_data")
    fun getDashBoardData():Call<Any>?
}
