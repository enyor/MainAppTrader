package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pop_datos_aux.*

/**
 * Created by gedica on 29/03/2018.
 */

class Popup_datos_aux : Activity() {

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
        setContentView(R.layout.activity_pop_datos_aux)
        UserId = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre")
        val especialidad = intent.getStringExtra("especialidad")
        val enable = intent.getStringExtra("enable")
        val lat = intent.getDoubleExtra("lat",0.0)
        val lng = intent.getDoubleExtra("lng",0.0)
        val tiempo = intent.getIntExtra("tiempo",0)
        img = intent.getStringExtra("img")
        img_cover = intent.getStringExtra("img_cover")
        dir = intent.getStringExtra("dir")

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width * .9).toInt(), (height * .85).toInt())

            val imageUric = "https://mainapp.kaizenpro.co.uk/assets/"+img_cover
            val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+img




        btreservar.setOnClickListener{onclicreservar(lat,lng)}



        Nombre.setText(nombre)

    }


    private fun onclicreservar(la:Double, ln:Double){

        var id = UserId
        var Lat = la
        var Lng = ln
        var enable = 1
        var tm = 60

        var nom = Nombre.text.toString()

        val intent = Intent(this, MapsActivity::class.java)
        img = "avatar.png"

        val valores = listOf("id" to id, "Lat" to Lat, "Lng" to Lng,"Enable" to enable, "Tiempo" to tm, "img" to img, "Nombre" to nom)
        Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_marcador_nuevo_aux.php", valores).response { request, response, result ->

            result.success {
                MapsActivity.UserName = nom
                setResult(RESULT_OK, intent)
                finish ()
            }

            result.failure {
                Toast.makeText(this, "Error:" , Toast.LENGTH_LONG).show();

                setResult(RESULT_CANCELED, intent)
                finish ()
            }

        }







    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 400){
            if(resultCode == AppCompatActivity.RESULT_OK) {
                var Fn = data!!.getStringExtra("filename")
                img = Fn
                val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+Fn
                //   Picasso.with(this@Popup_datos).load(imageUri).resize(100,100).centerInside().into(avatar)

                // } else {
                //   val imageUri = "https://spoanet.com/work/bantic/demo/public/images/avatar.png"
                //  Picasso.with(this@Popup_datos).load(imageUri).resize(100,100).centerInside().into(avatar)


            }
        }
        if(requestCode == 500){
            if(resultCode == AppCompatActivity.RESULT_OK) {
                var Fn = data!!.getStringExtra("filename")
                img_cover = Fn
                val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+Fn
                //   Picasso.with(this@Popup_datos).load(imageUri).resize(100,100).centerInside().into(avatar)




            }
        }
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