package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_pop_datos.*
import kotlinx.android.synthetic.main.activity_pop_servicio.*

/**
 * Created by gedica on 29/03/2018.
 */

class Popup_servicio : Activity() {

    var texto = ""
    var img = ""
    var img_cover = ""
    var dir = ""
    lateinit var fromPosition: LatLng
    lateinit var toPosition: LatLng
    private var UserId = 0

    var URL = "https://mainapp.kaizenpro.co.uk/consulta_servicio.php?id=";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_servicio)
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

        URL += UserId

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width * .9).toInt(), (height * .85).toInt())

        var recyclerPersonajes = findViewById<RecyclerView>(R.id.recyclerId)
        var llm = LinearLayoutManager(this@Popup_servicio)

        recyclerPersonajes.layoutManager = llm
        //recyclerPersonajes.setLayoutManager(LinearLayoutManager(context))

        var itemsData = arrayListOf<ItemServicio>()
        //  llenarLista()
        URL.httpGet().responseObject(Servicio.Deserializer()) { request, response, result ->
            val (servicios, err) = result


            //Add to ArrayList
            servicios?.forEach { servicio ->
            itemsData.add(ItemServicio(servicio.id_servicio,servicio.titulo,servicio.descripcion,servicio.precio))
            }

            val adapter = ServiceAdapter(itemsData)


            recyclerPersonajes.isNestedScrollingEnabled = false
            //recyclerPersonajes.setHasFixedSize(true)


            recyclerPersonajes.adapter = adapter
        }

        fab_agregar.setOnClickListener{
            val intent = Intent(this, Popup_servicio_new::class.java)
            intent.putExtra("UserId",UserId)
            startActivityForResult(intent,100)

        }


      /*  val itemsData = arrayListOf(ItemServicio("Corte", "Servicio de corte de cabello para caballeros.", "5.50"),
                ItemServicio("Coloración", "Aplicación de color según carta en muestrario diponible", "21.00"),
                ItemServicio("Alisado Japones", "Entre sus ventajas es el tiempo que permanece elalisado japones alisado permanente perfecto tiempo liso y sedoso: varios meses, incluso un año en ciertos tipos de cabello.", "12.00"),
                ItemServicio("Micropigmentación de Cejas", " consiste en un tatuaje temporal donde el “lápiz” (porque no es máquina de tatuaje) penetra con tinta la capa más superficial de tu piel.", "12.00"),
                ItemServicio("Depilación láser", "Es un método de depilación que existe desde 1994 basado en la eliminación del vello de forma permanente mediante la emisión lumínica del láser, por lo cual, es un tipo de fotodepilación..", "3.00"))
*/







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



        val valores = listOf("id" to id, "Lat" to Lat, "Lng" to Lng,"Enable" to enable, "Tiempo" to tm, "especialidad" to especialidad, "sexo" to sexo, "img" to img, "img_cover" to img_cover, "dir" to dir )
        Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_marcador_nuevo.php", valores).response { request, response, result ->


        }


        val intent = Intent(this, MapsActivity::class.java)

            setResult(RESULT_OK, intent)
            finish ()


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
                Glide.with(this@Popup_servicio)
                        .load(Uri.parse(imageUri)) // add your image url
                        .override(80,80)
                        .centerCrop()
                        .transform(CircleTransform(this@Popup_servicio)) // applying the image transformer
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
                Glide.with(this@Popup_servicio)
                        .load(Uri.parse(imageUri)) // add your image url
                        //.override(80,80)
                        //.centerCrop()
                       // .transform(CircleTransform(this@Popup_datos)) // applying the image transformer
                        .into(cover)



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

    override fun onResume() {
        super.onResume()
    }



}