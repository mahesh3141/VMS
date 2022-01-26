package com.airport.vms.data.network

interface NotifyTaskDone {
    fun onTaskDone(action: String, data: DataModel?)
}