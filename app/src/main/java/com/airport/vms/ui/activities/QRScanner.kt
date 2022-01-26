package com.airport.vms.ui.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airport.vms.R
import com.airport.vms.applications.prefs
import com.airport.vms.data.network.APIInterface
import com.airport.vms.data.network.ApiHandler
import com.airport.vms.data.network.ICallBack
import com.airport.vms.data.utils.AppConstants
import com.airport.vms.ui.models.CommonResponse
import com.budiyev.android.codescanner.CodeScanner
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_q_r_scanner.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.yesButton
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class QRScanner : AppCompatActivity(), ICallBack {

    var codeScanner: CodeScanner? = null
    var apiInterface: APIInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_scanner)
        apiInterface = ApiHandler.getServiceMethods()
        createScannerView()
        scannerView?.setOnClickListener {
            codeScanner?.startPreview()
        }
    }

    private fun createScannerView() {
        codeScanner = CodeScanner(this, scannerView ?: return)
        codeScanner?.setDecodeCallback { qrResult ->
            runOnUiThread(Runnable {
                txtQRData?.text = qrResult.text
                println("*** QR " + qrResult.text)
                val jsonObject = JSONObject()
                jsonObject.put("userID", prefs.customerId)
                jsonObject.put("zoneID", prefs.ZoneId)
                jsonObject.put("qrData", qrResult.text)
                println("*** json QR $jsonObject")
                sentQRData(jsonObject.toString())
            })
        }
    }

    private fun sentQRData(qrJson: String) {
        try{
            if (AppConstants.checkInternateConnection(this)) {
                showLoader("Please Wait...", true)
                val call: Call<Any>? = apiInterface?.scanQR(qrJson ?: "")
                call?.enqueue(ApiHandler.getCallBack(this, "scanQR"))
            } else {
                AppConstants.showAlert(this, "Please check your data connection")
            }
        }catch (e:Exception){
            e.printStackTrace()
            println("*** post QR Error "+e.message)
        }

    }

    override fun onResume() {
        super.onResume()
        grandPermission()
    }

    private fun grandPermission() {
        Dexter.withContext(applicationContext).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    codeScanner?.startPreview()
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest?, token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    alert {
                        message = getString(R.string.requireCamera)
                        yesButton { }
                    }.show()
                }

            }).check()
    }

    override fun onCallBackReceived(action: String, response: Response<Any>) {
        showLoader("",false)
        if (response.isSuccessful) {
            val data = response.body()
            val respModel = data?.let {
                ApiHandler.getDataObject(
                    it,
                    CommonResponse::class.qualifiedName.toString()
                )
            } as CommonResponse?
            when (respModel?.status) {
                1, 2, 3 -> {
                    alert {
                        message = AppConstants.setHtmlText(respModel?.message)
                        yesButton {
                            val intent = Intent()
                            intent.putExtra("scanStatus",respModel.status.toString())
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }.show()
                }
            }

        }

    }

    override fun onFailureReceived(action: String, t: Throwable) {
        t.printStackTrace()
        println("***QRError " + t.message)
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
