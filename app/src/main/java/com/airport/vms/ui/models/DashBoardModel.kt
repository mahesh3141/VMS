package com.airport.vms.ui.models
import com.google.gson.annotations.SerializedName


class DashBoardModel : ArrayList<DashBoardModel.DashBoardModelItem>(){
    data class DashBoardModelItem(
        @SerializedName("color")
        val color: String,
        @SerializedName("total_present")
        val totalPresent: String,
        @SerializedName("zone_id")
        val zoneId: String,
        @SerializedName("zone_name")
        val zoneName: String,
        @SerializedName("zone_status")
        val zoneStatus: Int
    )
}