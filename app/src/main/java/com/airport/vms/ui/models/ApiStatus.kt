package com.airport.vms.ui.models

class ApiStatus {
    companion object {
        const val ERROR = "error"
        const val LOADING = "loading"
        const val SUCCESS = "success"
        const val IDLE = "idle"
        const val LOGGED_OUT = "logged_out"

        fun setSuccess(message: String, data: Any?): ApiStatus {
            return ApiStatus().apply {
                status = SUCCESS
                this.message = message
                this.data = data
            }
        }

        fun setError(message: String): ApiStatus {
            return ApiStatus().apply {
                status = ERROR
                this.message = message
            }
        }
    }

    var status: String = IDLE
    var message: String = ""
    var data: Any? = null
    var shouldGoBack: Boolean = false
    var shouldRetry: Boolean = false
    var isProgressLoader: Boolean = true
    var progressValue:Int = 0
    var maxValue:Int=0
}
