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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pop_datos.*

/**
 * Created by gedica on 29/03/2018.
 */

class Popup_datos : Activity() {

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
        setContentView(R.layout.activity_pop_datos)
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

            val imageUric = "https://mainapp.kaizenpro.co.uk/assets/"+MapsActivity.img
            val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+MapsActivity.img_cover
        //Picasso.with(v.context).load(imageUric).into(v.cover)
		Glide.with(this@Popup_datos).load(Uri.parse(imageUric)).into(cover)

        Glide.with(this@Popup_datos)
                .load(Uri.parse(imageUri)) // add your image url
                .override(80,80)
                .centerCrop()
                .transform(CircleTransform(this@Popup_datos)) // applying the image transformer
                .skipMemoryCache(true)
                .into(avatar)


        btreservar.setOnClickListener{onclicreservar(lat,lng)}

        avatar.setOnClickListener { val intent = Intent(this@Popup_datos, UploadActivity::class.java)
            intent.putExtra("UserId",UserId.toString());
        //startActivity(intent)
        startActivityForResult(intent,400)

        }

        cover.setOnClickListener { val intent = Intent(this@Popup_datos, UploadActivity::class.java)
            intent.putExtra("UserId","Cover_"+UserId.toString());
            //startActivity(intent)
            startActivityForResult(intent,500)

        }



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
        var especialidad = Especialidad.text.toString()
        var enable = 0
        if (sw1.isChecked) {
            enable = 1
        }else{
             enable = 0
        }
        var sexo = ""
        if (M.isChecked){
            sexo = "M"
        }else{
            sexo = "F"
        }
        var tm = tftime.text
        dir = direccion.text.toString()

        var nom = Nombre.text.toString()

        var edad = tfedad.text.toString()


        val intent = Intent(this, MapsActivity::class.java)
        val valores = listOf("id" to id, "Lat" to Lat, "Lng" to Lng,"Enable" to enable, "Tiempo" to tm, "especialidad" to especialidad, "sexo" to sexo, "img" to img, "img_cover" to img_cover, "dir" to dir, "Nombre" to nom, "edad" to edad)
        Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_marcador_nuevo.php", valores).response { request, response, result ->
            result.success {
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
                Glide.clear(avatar)
                Glide.with(this@Popup_datos)
                        .load(Uri.parse(imageUri)) // add your image url
                        .override(80,80)
                        .centerCrop()
                        .transform(CircleTransform(this@Popup_datos)) // applying the image transformer
                        .into(avatar)
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
                Glide.clear(cover)
                Glide.with(this@Popup_datos)
                        .load(Uri.parse(imageUri)) // add your image url
                        //.override(80,80)
                        //.centerCrop()
                       // .transform(CircleTransform(this@Popup_datos)) // applying the image transformer
                        .into(cover)



            }
        }
    }





}