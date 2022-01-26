package com.airport.vms.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import com.airport.vms.R
import com.airport.vms.applications.prefs
import com.airport.vms.interfaces.ZoneResult
import com.airport.vms.ui.models.ZoneModel
import kotlinx.android.synthetic.main.row_zone_select.*

class ZoneDialog(
    context: Context,
    var data: ArrayList<ZoneModel.Data>,
    zoneResult: ZoneResult
): Dialog(context, android.R.style.ThemeOverlay_Material_Dialog_Alert)  {

    init {
        setContentView(R.layout.row_zone_select)
        if (LLZone?.childCount?:0 >0)
            LLZone?.removeAllViews()

        val radioGroup = RadioGroup(context)

        data.forEachIndexed { index, data ->

            val chkZone = RadioButton(context)
            chkZone.text = data.zoneName
            chkZone.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked) {
                    data.isSelect = true
                    prefs.ZoneId = data.zoneId
                    prefs.ZoneName = data.zoneName
                }else{
                    data.isSelect = false
                }
            }
            radioGroup.addView(chkZone)
        }
        LLZone?.addView(radioGroup)
        btnDone?.setOnClickListener {
            zoneResult.selectedZone(prefs.ZoneName)
            dismiss()
        }
        this.setCanceledOnTouchOutside(false)
        show()
    }

}