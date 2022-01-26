package com.airport.vms.ui.activities

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
import com.airport.vms.ui.models.LoginModel
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.indeterminateProgressDialog
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity(), ICallBack {

    var apiInterface: APIInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        apiInterface = ApiHandler.getServiceMethods()

        if (AppConstants.checkInternateConnection(this)) {
            if(prefs.isCustLogin && prefs.customerEmail.isNotEmpty() && prefs.customerPassword.isNotEmpty()){
                txtInputUsername?.editText?.setText(prefs.customerEmail?:"")
                txtInputPassword?.editText?.setText(prefs.customerPassword?:"")
                callLogin()
            }
        } else {
            AppConstants.showAlert(this, "Please check your data connection")
        }

        btnLogin?.setOnClickListener {


            if (AppConstants.checkInternateConnection(this)) {
                if (isValidLogin()) {
                   callLogin()
                } else {
                    println("**** invalid user name & password")
                    //startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))
                    // finish()
                }
            } else {
                AppConstants.showAlert(this, "Please check your data connection")
            }
        }
    }

    private fun callLogin() {
        showLoader("Please Wait...",true )
        println("**** click login")
        val call: Call<Any>? = apiInterface?.doLogin(
            txtInputUsername?.editText?.text?.toString(),
            txtInputPassword?.editText?.text?.toString()
        )
        call?.enqueue(ApiHandler.getCallBack(this, "login"))
    }

    private fun isValidLogin(): Boolean {
        if (txtInputUsername?.editText?.text?.isEmpty() == true) {
            AppConstants.showAlert(txtInputUsername?.context!!, "Please enter user name")
            return false
        }
        if (txtInputPassword?.editText?.text?.isEmpty() == true) {
            AppConstants.showAlert(txtInputUsername?.context!!, "Please enter password")
            return false
        }
        return true
    }


    override fun onCallBackReceived(action: String, response: Response<Any>) {
        showLoader("", false)
        if (response.isSuccessful) {
            val data = response.body()
            val logModel = data?.let {
                ApiHandler.getDataObject(
                    it,
                    LoginModel::class.qualifiedName.toString()
                )
            } as LoginModel?
            // val type = object : TypeToken<ArrayList<LoginModel>>() {}.type
            //  val logModel = data?.let { ApiHandler.getDataArray(it,type) }
            if (logModel?.flag == 1) {

                // AppConstants.showAlert(btnLogin?.context ?: return, logModel?.message)
                println("name " + logModel?.data?.name)
                println("lastname " + logModel?.data?.username)
                prefs.customerId = logModel?.data?.userId
                prefs.isCustLogin = true
                prefs.customerFirstName = logModel?.data?.name
                prefs.customerEmail =  txtInputUsername?.editText?.text?.toString()?.trim()?:""
                prefs.customerPassword = txtInputPassword?.editText?.text?.toString()?.trim()?:""
                startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))
                finish()
            } else {
                AppConstants.showAlert(btnLogin?.context ?: return, logModel?.message ?: "")
            }
        } else {
            AppConstants.showAlert(btnLogin?.context ?: return, response.message())
        }
    }

    override fun onFailureReceived(action: String, t: Throwable) {
        showLoader("", false)
        t.printStackTrace()
        println("*** Error " + t.message)
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
