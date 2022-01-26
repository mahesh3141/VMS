package com.airport.vms.data.utils

import com.airport.vms.applications.VMS
import com.airport.vms.data.extensions.DelegatesExt

class AppSharedPreferences {
    var ZoneName by DelegatesExt.preference(VMS.ctx,"ZoneName","")
    var ZoneId by DelegatesExt.preference(VMS.ctx,"ZoneId","")
    var zoneArray by DelegatesExt.preference(VMS.ctx,"zoneArray","")
    var languageCode by DelegatesExt.preference(VMS.ctx, "languageCode", "en")
    var isCustLogin by DelegatesExt.preference(VMS.ctx, "isCustLogin", false)

    //Customer Related Data
    var customerId by DelegatesExt.preference(VMS.ctx, "customerId", "")
    var customerEmail by DelegatesExt.preference(VMS.ctx, "customerEmail", "")
    var customerPassword by DelegatesExt.preference(VMS.ctx, "customerPassword", "")
    var customerCurrency by DelegatesExt.preference(VMS.ctx, "customerCurrency", "")
    var customerFirstName by DelegatesExt.preference(VMS.ctx, "customerFirstName", "")
    var customerLastName by DelegatesExt.preference(VMS.ctx, "customerLastName", "")
    var customerNotification by DelegatesExt.preference(VMS.ctx, "customerNotification", false)
}