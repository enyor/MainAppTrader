package co.kaizenpro.mainapp.mainapptrader

import android.content.ContentValues.TAG
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
val PREFS_FILENAME = "co.kaizenpro.mainapp.mainapptrader.prefs"


//val TAG = FirebaseIdService::class.java.simpleName!!




class FirebaseIdService : FirebaseInstanceIdService() {


    override fun onTokenRefresh() {
        super.onTokenRefresh()
        Log.d(TAG, "Token refreshed!")


        val refreshedToken = FirebaseInstanceId.getInstance().token

        // we want to send messages to this application instance and manage this apps subscriptions on the server side
        // so now send the Instance ID token to the app server
        refreshedToken?.let {
            sendRegistrationToServer(it)
        }
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)

        val edit = prefs.edit()
        edit.putString("TOKEN","")
        edit.putString("TOKEN",refreshedToken)
        edit.commit()





    }

}