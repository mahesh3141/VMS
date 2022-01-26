package com.airport.vms.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airport.vms.R
import com.airport.vms.ui.models.DashBoardModel
import com.airport.vms.ui.models.ZoneModel
import kotlinx.android.synthetic.main.row_dashboard.view.*

class DashBoardAdapter(var context: Context, var arrayList:ArrayList<DashBoardModel.DashBoardModelItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_dashboard, parent, false)
        return DashViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DashViewHolder).itemView.txtPassengerCount?.text = arrayList[position].totalPresent?: ""
       (holder as DashViewHolder).itemView.txtGateName?.text = arrayList[position].zoneName?: ""
        when(arrayList[position].color){
            "Green"->{
                (holder as DashViewHolder).itemView.txtPassengerCount?.setBackgroundColor(ContextCompat.getColor(context,R.color.green))
            }
            "Orange"->{
                (holder as DashViewHolder).itemView.txtPassengerCount?.setBackgroundColor(ContextCompat.getColor(context,R.color.orange))
            }
            "Red"->{
                (holder as DashViewHolder).itemView.txtPassengerCount?.setBackgroundColor(ContextCompat.getColor(context,R.color.redColor))
            }
        }
        if(position==(arrayList.size-1))
            (holder as DashViewHolder).itemView.txtPassengerCount?.visibility = View.GONE
       //
    }

    inner class DashViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        init {
           /* view?.setOnClickListener {
                onItemClick.invoke(arrayList[adapterPosition], adapterPosition)
            }*/
        }
    }
}