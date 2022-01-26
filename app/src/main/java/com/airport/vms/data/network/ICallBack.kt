package com.airport.vms.data.network

import retrofit2.Response

interface ICallBack {
    fun onCallBackReceived(action: String, response: Response<Any>)
    fun onFailureReceived(action: String, t: Throwable)
}
