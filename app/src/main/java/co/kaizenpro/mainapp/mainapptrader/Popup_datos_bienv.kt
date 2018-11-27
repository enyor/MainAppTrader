package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_pop_datos_bienv.*

/**
 * Created by gedica on 29/03/2018.
 */

class Popup_datos_bienv : Activity() {

    var texto = ""
    var img = ""
    var img_cover = ""
    var dir = ""
    lateinit var fromPosition: LatLng
    lateinit var toPosition: LatLng
    private var UserId = 0

    val URL = "https://mainapp.kaizenpro.co.uk/actualizar_marcador.php";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_datos_bienv)


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width * .9).toInt(), (height * .85).toInt())






        btreservar.setOnClickListener{onclicreservar()}




    }


    private fun onclicreservar(){
        val PREFS_FILENAME = "co.kaizenpro.mainapp.mainapptrader.prefs"
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val edit = prefs.edit()
        edit.putString("BIENVENIDA", "1")
        edit.commit()
    finish()


    }






}