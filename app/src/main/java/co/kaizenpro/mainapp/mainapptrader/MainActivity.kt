package co.kaizenpro.mainapp.mainapptrader

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.content.*
import android.location.LocationManager
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.andrognito.pinlockview.PinLockView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

import kotlinx.android.synthetic.main.activity_main.*

/**
 * A login screen that offers login via email/password.
 */
class MainActivity : AppCompatActivity(), LoaderCallbacks<Cursor> {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null
    var Users = ArrayList<User>()

    val PREFS_FILENAME = "co.kaizenpro.mainapp.mainapptrader.prefs"
    val USERNAME =""
    val PASSWORD =""
    val SAVELOGIN = 0
    val INICIO = 0
    val ID = 0
    var deviceID = ""
    var vpin = ""
    lateinit var mIndicatorDots: IndicatorDots
    lateinit var mPinLockView: PinLockView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		
        deviceID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        deviceID = "MA-"+deviceID.toString().toUpperCase()+"T"

        mPinLockView = findViewById<View>(R.id.pin_lock_view) as PinLockView
        mPinLockView.setPinLockListener(mPinLockListener)

        mIndicatorDots = findViewById<View>(R.id.indicator_dots) as IndicatorDots

        mPinLockView.attachIndicatorDots(mIndicatorDots)


        mPinLockView.setPinLength(4)
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white))
        mPinLockView.isShowDeleteButton = true

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED)
        
		// Set up the login form.
        //populateAutoComplete()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.importantForAutofill =
                    View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS;
        }

        //EP 16072018
        device.setText(deviceID)
        //ocultar
        device.visibility = View.GONE
        //verificar si esta registrado
        VerificaDevice(deviceID)
		


        populateComplete()
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

		 password.setOnKeyListener(View.OnKeyListener { v, i, event ->
            if ( event.action == KeyEvent.ACTION_UP && password.text.length == 4) {
                 //Toast.makeText(this,"Accion",Toast.LENGTH_SHORT).show()

                attemptLogin()
                password.setText("")
                return@OnKeyListener true
            }
            false
        })

        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val gete = prefs.all
        val pr = gete.get("SAVELOGIN")
        if(pr == "1") {

            email.setText(gete.get("USERNAME").toString())
            password.setText(gete.get("PASSWORD").toString())
            checkBoxR.isChecked = true

        }
		
		 val inic = gete.get("INICIO")
        if(inic == null) {
            val intent = Intent(baseContext, RegisterActivity::class.java)

            intent.putExtra("DID", deviceID)
            Toast.makeText(this@MainActivity, "Debe registrarse para acceder", Toast.LENGTH_LONG).show()

            startActivity(intent)

        }


   /*     if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
*/
        email_sign_in_button.setOnClickListener { attemptLogin() }

        registrar.setOnClickListener {
            val intent = Intent(baseContext, RegisterActivity::class.java)
            startActivity(intent)

        }

        val mLocMgr = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if ( !mLocMgr.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }

    }

    fun AlertNoGps() {

        // setup the alert builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GPS Desactivado")
        builder.setMessage("El sistema GPS esta desactivado y es necesario para la ejecución de la aplicación, ¿Desea activarlo?")

        // add the buttons
        builder.setCancelable(false)
        builder.setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> finish()})

        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun populateComplete() {
        if (!mayRequestLocation()) {
            return
        }
    }

    private fun populateAutoComplete() {
        if (!mayRequestContacts()) {
            return
        }

        loaderManager.initLoader(0, null, this)
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS) })
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        return false
    }

    private fun mayRequestLocation(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            Snackbar.make(email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(ACCESS_FINE_LOCATION),REQUEST_ACCESS_FINE_LOCATION ) })
        } else {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateComplete()
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
    /*    if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        } */

        // Check for a valid email address.
		//MOd Sol USU 100818
        /*if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)

            val emailStr = email.text.toString()
            //val passwordStr = password.text.toString()
			val passwordStr = vpin
            val device = device.text.toString()


            Users = ArrayList<User>()

            var URL ="https://mainapp.kaizenpro.co.uk/marcador_login.php?em="+device+"&pw="+passwordStr

            FuelManager.instance.socketFactory = CustomSSLSocketFactory.getSSLSocketFactory(this)
            // consulta Ws para ultima posicion del trader
            URL.httpGet().responseObject(User.Deserializer()) { request, response, result ->
                val (users, err) = result

                result.success {

                    //Add to ArrayList
                    users?.forEach { user ->
                        Users.add(user)


                    }
                    val mLocMgr = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    if ( !mLocMgr.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        AlertNoGps();
                    }
                    val intent = Intent(baseContext, MapsActivity::class.java)

                    mAuthTask = null
                    showProgress(false)

                    if(Users.isNotEmpty()) {
                          //Validar solo acceso a Rol Traders
                        if (Users[0].rol.toInt()!=1){
                            email.error = "Acceso  permitido solo a Traders"
                            email.requestFocus()
                    //    } else if (Users[0].enable.toInt()!=1) {
                      //      email.error = "El usuario no está activo, verifique"
                        //    email.requestFocus()
                        } else {
                            //Guarda los datos
                            val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)

                            if (checkBoxR.isChecked()) {
                                val edit = prefs.edit()
                                edit.putString("SAVELOGIN", "1")
                                edit.putString("USERNAME", email.text.toString())
                                edit.putString("PASSWORD", password.text.toString())
                                edit.commit()
                            } else {
                                val edit = prefs.edit()
                                edit.putString("SAVELOGIN", "0")
                                edit.putString("USERNAME", "")
                                edit.putString("PASSWORD", "")
                                edit.commit()
                            }

                            mPinLockView.resetPinLockView()

                            //Finalmente envia al inicio
                            intent.putExtra("UserId", Users[0].id)
                            intent.putExtra("UserName", Users[0].nombre)

                            val edit = prefs.edit()
                            edit.putInt("ID",Users[0].id)
                            edit.commit()

                            val traderdatos = TraderDatos()

                            traderdatos.userId = Users[0].id.toString()
                            traderdatos.setNombre(Users[0].nombre)
                            //traderdatos.setEspecialidad(Traders[0].especialidad)


                            val prefss = this.getSharedPreferences(PREFS_FILENAME, 0)

                            val gete = prefss.all

                            val id = gete.get("ID")
                            val token =  gete.get("TOKEN")

                            val valores = listOf("id" to id, "token" to token )
                            Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_marcador_token.php", valores).response { request, response, result ->
                                result.success {
                                    Log.d(ContentValues.TAG, "Refreshed token: $token has send this to server!!!")
                                }
                                result.failure {
                                    Log.d(ContentValues.TAG, " token: $token - no refreshed in server")
                                }

                            }



                            startActivity(intent)
                        }
                    }else{
                        mPinLockView.resetPinLockView()
                        //password.error = getString(R.string.error_incorrect_password)
                        //password.requestFocus()
                        Toast.makeText(this,"El PIN introducido es incorrecto",Toast.LENGTH_LONG).show()
                    }


                }




            }





            // mAuthTask = UserLoginTask(emailStr, passwordStr)
           // mAuthTask!!.execute(null as Void?)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    private fun VerificaDevice(client: String) {

        var URL = "https://mainapp.kaizenpro.co.uk/marcador_consulta_client.php?em=" + client
        // consulta Ws para ultima posicion del trader
        URL.httpGet().responseObject(User.Deserializer()) { request, response, result ->
            val (users, err) = result

            result.success {

                //Add to ArrayList
                users?.forEach { user ->
                    Users.add(user)


                }

                if (Users.isEmpty()) {

                    val intent = Intent(baseContext, RegisterActivity::class.java)
                    intent.putExtra("DID", deviceID)
                    Toast.makeText(this@MainActivity, "Debe registrarse para acceder", Toast.LENGTH_LONG).show()
                    startActivity(intent)
                }


            }

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@MainActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        //email.setAdapter(adapter)
    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY)
        val ADDRESS = 0
        val IS_PRIMARY = 1
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val mEmail: String, private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000)

            } catch (e: InterruptedException) {
                return false
            }


            return DUMMY_CREDENTIALS
                    .map { it.split(":") }
                    .firstOrNull { it[0] == mEmail }
                    ?.let {
                        // Account exists, return true if the password matches.
                        it[1] == mPassword
                    }
                    ?: true
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                //finish()


            } else {
                password.error = getString(R.string.error_incorrect_password)
                password.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    var mPinLockListener: PinLockListener = object : PinLockListener {

        var TAG = "PinLock"

        override fun onComplete(pin : String) {
            Log.d(TAG, "Pin complete: " + pin);

            vpin = pin

            attemptLogin()


        }

        override fun onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        override fun onPinChange(pinLength : Int, intermediatePin : String) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);


        }

    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0
        private val REQUEST_ACCESS_FINE_LOCATION = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world", "client1@spoanet.com:client")
    }
}
