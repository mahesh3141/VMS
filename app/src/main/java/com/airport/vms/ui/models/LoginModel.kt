package com.airport.vms.ui.models

import com.google.gson.annotations.SerializedName


data class LoginModel(
    @SerializedName("data")
    val data: Data,
    @SerializedName("flag")
    val flag: Int,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("name")
        val name: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("username")
        val username: String
    )
}