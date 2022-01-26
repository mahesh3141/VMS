package com.airport.vms.data.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Spanned
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.text.HtmlCompat
import com.airport.vms.R
import com.airport.vms.ui.models.DashBoardModel
import com.airport.vms.ui.models.ZoneModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import org.jetbrains.anko.indeterminateProgressDialog
object AppConstants {
    const val DOMAIN = ""
    const val URL = "${DOMAIN}ws/index.php?"
    var dialog: ProgressDialog? = null

    const val OPEN_QR_SCAN = 1001

    /**
     * Logging Constant*/
    const val LOG_URL = "URL ==> "
    const val LOG_RESPONSE = "RESPONSE ===> "
    const val LOG_GSON = "GSON ERROR ===>"

    var zoneData: ArrayList<ZoneModel.Data>?=null
    var getsArray:ArrayList<DashBoardModel.DashBoardModelItem>?=null

    fun checkInternateConnection(context: Context?): Boolean {
        val cManager: ConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }



    fun showAlert( context: Context, data:String){
       context.alert { message = data
       yesButton {  }
           isCancelable = false
       }.show()
   }

    fun setHtmlText(content: String): Spanned {
        var source = content
        source = source.replace("&lt;", "<").replace("&gt;", ">")

        return HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
                or HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
                or HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV
                or HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
                or HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV
                or HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING
                or HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST
                or HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS, null,HtmlTagHandler)

    }

}