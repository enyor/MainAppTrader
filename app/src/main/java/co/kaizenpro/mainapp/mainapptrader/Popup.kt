package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.Fuel.Companion.get
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.success
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pop.*
import com.google.android.gms.maps.model.LatLng
import java.nio.file.Files.size
import android.widget.Spinner
import com.bumptech.glide.Glide


/**
 * Created by gedica on 29/03/2018.
 */

class Popup : Activity() {

    var texto = ""
    var img = ""
    lateinit var fromPosition: LatLng
    lateinit var toPosition: LatLng
    private var UserId = 0

    val URL = "https://mainapp.kaizenpro.co.uk/actualizar_marcador.php";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop)
        UserId = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre")
        val especialidad = intent.getStringExtra("especialidad")
        val enable = intent.getStringExtra("enable")
        val lat = intent.getDoubleExtra("lat",0.0)
        val lng = intent.getDoubleExtra("lng",0.0)
        val tiempo = intent.getIntExtra("tiempo",0)
        img = intent.getStringExtra("img")
        val dir = intent.getStringExtra("dir")

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width * .8).toInt(), (height * .75).toInt())

        val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+img
        //Picasso.with(this@Popup).load(imageUri).resize(100,100).centerInside().into(avatar)
        Glide.with(this@Popup)
                .load(Uri.parse(imageUri)) // add your image url
                .override(80,80)
                .centerCrop()
                .transform(CircleTransform(this@Popup)) // applying the image transformer
                .into(avatar)

        btreservar.setOnClickListener{onclicreservar(lat,lng)}





        Nombre.setText(nombre)
        Especialidad.setText(especialidad)
        direccion.setText(dir)
        tftime.setText(tiempo.toString())
        if(enable=="0"){
            sw1.isChecked = false
        } else {
            sw1.isChecked = true
        }

    }


    private fun onclicreservar(la:Double, ln:Double){

        var id = UserId
        var Lat = la
        var Lng = ln
        var enable = 0
        if (sw1.isChecked) {
            enable = 1
        }else{
             enable = 0
        }
        var tm = tftime.text
        val valores = listOf("id" to id, "Lat" to Lat, "Lng" to Lng,"Enable" to enable, "Tiempo" to tm )
        Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_marcador.php", valores).response { request, response, result ->


        }


        val intent = Intent(this, MapsActivity::class.java)

            setResult(RESULT_OK, intent)
            finish ()


    }

    private fun getRequestUrl(origin: LatLng, dest: LatLng): String {
        //Value of origin
        val str_org = "origin=" + origin.latitude + "," + origin.longitude
        //Value of destination
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        //Set value enable the sensor
        val sensor = "sensor=false"
        //Mode for find direction
        val mode = "mode=driving"
        //Build the full param
        val param = "$str_org&$str_dest&$sensor&$mode"
        //Output format
        val output = "json"
        //Create url to request
        return "https://maps.googleapis.com/maps/api/directions/$output?$param"
    }

    private fun getRequestUrl2(origin: LatLng, dest: LatLng): String {
        val urls = ("https://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + origin.latitude + "," + origin.longitude
                + "&destination=" + dest.latitude + "," + dest.longitude
                + "&sensor=false&units=metric&mode=driving")
        return urls
    }





}