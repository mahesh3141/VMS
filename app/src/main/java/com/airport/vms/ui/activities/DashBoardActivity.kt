package com.airport.vms.ui.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.airport.vms.R
import com.airport.vms.applications.prefs
import com.airport.vms.data.extensions.DelegatesExt
import com.airport.vms.data.network.APIInterface
import com.airport.vms.data.network.ApiHandler
import com.airport.vms.data.network.ICallBack
import com.airport.vms.data.utils.AppConstants
import com.airport.vms.interfaces.ZoneResult
import com.airport.vms.ui.adapters.CustomPagerAdapter
import com.airport.vms.ui.adapters.DashBoardAdapter
import com.airport.vms.ui.dialogs.ZoneDialog
import com.airport.vms.ui.models.DashBoardModel
import com.airport.vms.ui.models.ZoneModel
import kotlinx.android.synthetic.main.activity_dash_board.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class DashBoardActivity : AppCompatActivity(), ICallBack,ZoneResult {
    var apiInterface: APIInterface? = null
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setSupportActionBar(app_bar)
        apiInterface = ApiHandler.getServiceMethods()
        getAllZone()
        createViewPager()
        getDashBoardData()
        btnQrScan?.setOnClickListener { startActivity(Intent(this, QRScanner::class.java)) }
        llHome?.setOnClickListener {  }
        llFlight?.setOnClickListener {  AppConstants.showAlert(this,getString(R.string.commingsoon)) }
        llFlightInfo?.setOnClickListener {  AppConstants.showAlert(this,getString(R.string.commingsoon)) }
        llHelp?.setOnClickListener {  AppConstants.showAlert(this,getString(R.string.commingsoon)) }
        btnSendQR?.setOnClickListener {
            startActivityForResult(Intent(this,QRScanner::class.java),AppConstants.OPEN_QR_SCAN)
        }
        imgSync?.setOnClickListener { getDashBoardData() }
        imgVipScan?.visibility = View.GONE
        imgVipScan?.setOnClickListener { startActivityForResult(Intent(this,QRScanner::class.java),AppConstants.OPEN_QR_SCAN) }
    }

    override fun onResume() {
        super.onResume()
        txtUserName?.text = "Hi ".plus(prefs.customerFirstName)
        if(prefs.ZoneId.isNotEmpty()) {
            txtLocation?.text = "You are at ".plus(prefs.ZoneName)
            AppConstants.getsArray?.forEach datalist@ {
                if(prefs.ZoneId.equals(it.zoneId)){
                    if(it.zoneStatus==3 ) {
                        btnSendQR?.visibility = View.INVISIBLE
                        imgVipScan?.visibility = View.VISIBLE
                        return@datalist
                    }else{
                        btnSendQR?.visibility = View.VISIBLE
                        imgVipScan?.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun getDashBoardData() {
        showLoader( "Loading...", true)
        val call: Call<Any>? = apiInterface?.getDashBoardData()
        call?.enqueue(ApiHandler.getCallBack(this, "dashBoard"))
    }

    private fun createViewPager() {
        val arrayImage = arrayListOf<Int>(
            R.drawable.air0,
            R.drawable.air8,
            R.drawable.air2,
            R.drawable.air3,
            R.drawable.air4,
            R.drawable.air5
        )
        val adapter = CustomPagerAdapter(this, arrayImage)
        viewPager?.adapter = adapter
        viewPager?.pageMargin = 5
        val dotsCount = viewPager?.adapter?.count
        val dots = arrayOfNulls<ImageView>(dotsCount!!)

        for (i in 0 until dotsCount) {
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.non_active_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            layout_dot.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.active_dot
            )
        )
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots[i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.non_active_dot
                        )
                    )
                }
                dots[position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_dot
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == dotsCount) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }
        timer = Timer() // This will create a new Thread
        timer?.schedule(object : TimerTask() { // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)

    }


    private fun getAllZone() {
      //  AppConstants.showLoader(txtUserName?.context!!, "Loading...", true)
        val call: Call<Any>? = apiInterface?.getAllZone()
        call?.enqueue(ApiHandler.getCallBack(this, "getZone"))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_profile -> {
                AppConstants.showAlert(this,getString(R.string.commingsoon))
                return true
            }
            R.id.menu_zone -> {
                ZoneDialog(this, AppConstants.zoneData?: ArrayList(), this as ZoneResult)
                return true
            }
            R.id.menu_aboutUs -> {
                AppConstants.showAlert(this,getString(R.string.commingsoon))
                return true
            }
            R.id.menu_setting -> {
                AppConstants.showAlert(this,getString(R.string.commingsoon))
                return true
            }
            R.id.menu_logOut->{
                alert(Appcompat) {
                    message = getString(R.string.message_logout)
                    title = getString(R.string.logOut)
                    yesButton {
                        DelegatesExt.clearPrefs()
                        finish()
                    }
                    noButton { }
                }?.show()
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCallBackReceived(action: String, response: Response<Any>) {
      showLoader("", false)
        when(action){
            "getZone"->{
                if (response.isSuccessful) {
                    val data = ApiHandler.getDataObject(
                        response.body() ?: return,
                        ZoneModel::class.qualifiedName.toString()
                    ) as ZoneModel?
                    if (data?.flag == 1) {
                        AppConstants.zoneData = data.data
                        if (prefs.isCustLogin && prefs.customerId.isNotEmpty() && prefs.ZoneId.isEmpty()) {
                            displayZoneDialog(data.data)
                        }
                    } else {
                        AppConstants.showAlert(recycler?.context ?: return, data?.message ?: "")
                    }
                }
            }
            "dashBoard"->{
                if (response.isSuccessful) {
                    val data = ApiHandler.getDataObject(response.body()?:return,DashBoardModel::class.qualifiedName.toString()) as DashBoardModel?
                    val adapter = data?.let { DashBoardAdapter(recycler?.context ?: return, it) }
                    recycler?.adapter = adapter
                    AppConstants.getsArray = data
                    data?.forEach datalist@ {
                        if(prefs.ZoneId.equals(it.zoneId)){
                            if(it.zoneStatus==3) {
                                btnSendQR?.visibility = View.INVISIBLE
                                imgVipScan?.visibility = View.VISIBLE
                                return@datalist
                            }else{
                                btnSendQR?.visibility = View.VISIBLE
                                imgVipScan?.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AppConstants.OPEN_QR_SCAN->{
                    if(data?.getStringExtra("scanStatus")=="1")
                        getDashBoardData()
                }
            }
        }
    }

    private fun displayZoneDialog(data: ArrayList<ZoneModel.Data>) {
        ZoneDialog(this,data,this as ZoneResult)
    }

    override fun onFailureReceived(action: String, t: Throwable) {
        t.printStackTrace()
        println("***onFailure  " + t.message)
        showLoader( "", false)
    }

    override fun selectedZone(zoneName: String) {
        txtLocation?.text = "You are at ".plus(zoneName)
        AppConstants.getsArray?.forEach datalist@ {
            if(prefs.ZoneId.equals(it.zoneId)){
                if(it.zoneStatus==3) {
                    btnSendQR?.visibility = View.INVISIBLE
                    imgVipScan?.visibility = View.VISIBLE
                    return@datalist
                }else{
                    btnSendQR?.visibility = View.VISIBLE
                    imgVipScan?.visibility = View.INVISIBLE
                }
            }
        }
     /*   val adapter = DashBoardAdapter(recycler?.context ?: return, AppConstants.zoneData?:ArrayList())
        recycler?.adapter = adapter*/
    }
    var dialog: ProgressDialog? = null
    fun showLoader(message:String,isShow:Boolean){

        if (dialog == null) {
            dialog = indeterminateProgressDialog(message, "")
            dialog?.setCanceledOnTouchOutside(false)
        }
        if (isShow)
            dialog?.show()
        else
            dialog?.dismiss()
    }
}
