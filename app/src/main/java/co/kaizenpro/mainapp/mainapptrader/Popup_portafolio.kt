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
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_pop_portafolio.*

/**
 * Created by gedica on 29/03/2018.
 */

class Popup_portafolio : Activity() {


    var img = ""
    var img_cover = ""
    var dir = ""

    private var UserId = 0
    var count = 0
    lateinit var recyclerPersonajes : RecyclerView


    var URL = "https://mainapp.kaizenpro.co.uk/consulta_portafolio.php?id=";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_portafolio)
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

        URL += UserId

        recyclerPersonajes = findViewById<RecyclerView>(R.id.recyclerId)
        var llm = LinearLayoutManager(this@Popup_portafolio)



        recyclerPersonajes.layoutManager = llm
        //recyclerPersonajes.setLayoutManager(LinearLayoutManager(context))

        var itemsData = arrayListOf<ItemPortafolio>()
        //  llenarLista()
        URL.httpGet().responseObject(Portafolio.Deserializer()) { request, response, result ->
            val (portafolios, err) = result


            //Add to ArrayList
            portafolios?.forEach { portafolio ->
                itemsData.add(ItemPortafolio(portafolio.id_item,portafolio.titulo,portafolio.contenido,portafolio.imagen))
            }

         val  adapter = PortafolioAdapter(itemsData)



            recyclerPersonajes.isNestedScrollingEnabled = false
            //recyclerPersonajes.setHasFixedSize(true)


            recyclerPersonajes.adapter = adapter

            count = recyclerPersonajes.adapter.itemCount
        }

        fab_agregar.setOnClickListener{
            val intent = Intent(this, Popup_portafolio_new::class.java)
            intent.putExtra("count", count)
            intent.putExtra("UserId",UserId)
            startActivityForResult(intent,100)

        }




        //  llenarLista()
  /*      val itemsData = arrayListOf(ItemPortafolio("Goku", "Son Gokū es el protagonista del manga y anime Dragon Ball creado por Akira Toriyama.", R.drawable.icon_caballero),
                ItemPortafolio("Gohan", "Son Gohan es un personaje del manga y anime Dragon Ball creado por Akira Toriyama. Es el primer hijo de Son Gokū y Chi-Chi", R.drawable.icon_caballero),
                ItemPortafolio("Gohan", "Son Gohan es un personaje del manga y anime Dragon Ball creado por Akira Toriyama. Es el primer hijo de Son Gokū y Chi-Chi", R.drawable.icon_caballero),
                ItemPortafolio("Krilin", "Krilin es un personaje de la serie de manga y anime Dragon Ball. Es el primer rival en artes marciales de Son Gokū aunque luego se convierte en su mejor amigo.", R.drawable.icon_caballero),
                ItemPortafolio("Vegueta", "Vegeta es un personaje ficticio perteneciente a la raza llamada saiyajin, del manga y anime Dragon Ball.", R.drawable.icon_caballero))
*/





    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    /*    if(requestCode == 400){
            if(resultCode == AppCompatActivity.RESULT_OK) {
                var Fn = data!!.getStringExtra("filename")
                img = Fn
                val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+Fn
                //   Picasso.with(this@Popup_datos).load(imageUri).resize(100,100).centerInside().into(avatar)
                Glide.clear(avatar)
                Glide.with(this@Popup_portafolio)
                        .load(Uri.parse(imageUri)) // add your image url
                        .override(80,80)
                        .centerCrop()
                        .transform(CircleTransform(this@Popup_portafolio)) // applying the image transformer
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
                Glide.with(this@Popup_portafolio)
                        .load(Uri.parse(imageUri)) // add your image url
                        //.override(80,80)
                        //.centerCrop()
                       // .transform(CircleTransform(this@Popup_datos)) // applying the image transformer
                        .into(cover)



            }
        }
    */


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

    override fun onRestart() {
        super.onRestart()
    }

    override fun onContentChanged() {
        super.onContentChanged()
    }

    override fun onPause() {
        super.onPause()


    }

    override fun onPostResume() {
        super.onPostResume()
       // recyclerPersonajes.adapter.notifyDataSetChanged()

    }



}