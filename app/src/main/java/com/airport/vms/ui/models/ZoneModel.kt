package com.airport.vms.ui.models
import com.google.gson.annotations.SerializedName


data class ZoneModel(
    @SerializedName("data")
    val data: ArrayList<Data>,
    @SerializedName("flag")
    val flag: Int,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("zone_id")
        val zoneId: String,
        @SerializedName("zone_name")
        val zoneName: String,
        var isSelect:Boolean=false

    )
}