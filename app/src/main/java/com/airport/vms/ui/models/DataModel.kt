package com.airport.vms.ui.models

import android.os.Parcel
import android.os.Parcelable

open class DataModel() : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModel> {
        override fun createFromParcel(parcel: Parcel): DataModel {
            return DataModel(parcel)
        }

        override fun newArray(size: Int): Array<DataModel?> {
            return arrayOfNulls(size)
        }
    }
}