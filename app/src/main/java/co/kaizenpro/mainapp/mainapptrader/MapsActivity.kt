package co.kaizenpro.mainapp.mainapptrader

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


import android.location.Criteria
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.LocationListener
import android.util.Log
import android.os.Looper
import android.graphics.Color
import com.github.kittinunf.fuel.httpGet
import com.google.android.gms.maps.model.*


import android.app.ProgressDialog
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.progressDialog
import java.lang.Thread.sleep


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    var mylocation_real = LatLng(-0.0 , -0.0)
    private lateinit var mMap: GoogleMap
    var mylocation = LatLng(51.507391, -0.127853)
    var destination = LatLng(-0.0 , -0.0)
    var actualizado = false
    var isActivo = false



    //private lateinit var md : GMapV2Direction
    private lateinit var pd: ProgressDialog

    //Minimo tiempo para updates en Milisegundos
    private val MIN_TIEMPO_ENTRE_UPDATES = (1000 * 60 * 1).toLong() // 1 minuto
    //Minima distancia para updates en metros.
    private val MIN_CAMBIO_DISTANCIA_PARA_UPDATES: Float = 1.5f // 1.5 metros

    var URL = "https://mainapp.kaizenpro.co.uk/consulta_marcador_trader.php?id=";
    var Traders = ArrayList<Trader>()
    var URLs = "https://mainapp.kaizenpro.co.uk/consulta_marcadores.php";
    var Traderss = ArrayList<Trader>()

    //Declaracion de datos globales
    companion object {
        var UserId = 0
        var UserName = ""
        var Especialidad = ""
        var img = ""
        var img_cover = ""
        var dir = ""
        var tiempo = 0
        var lat = -0.0
        var lng = -0.0
        var enable = ""
        var sexo = ""
        var edad = 0
        var pago = 0

    }

    var isFabOpen = false
    private lateinit var fab1: FloatingActionButton
    private lateinit var fab2: FloatingActionButton
    private lateinit var fab_open: Animation
    private lateinit var fab_close: Animation

    val PREFS_FILENAME = "co.kaizenpro.mainapp.mainapptrader.prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.importantForAutofill =
                    View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS;
        }

        ventana_inicio()

        //
        fl_suscripcion.visibility = View.VISIBLE

        fab1 = findViewById(R.id.fabshareClient)
        fab2 = findViewById(R.id.fabshareTrader)
        //fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_open = AnimationUtils.loadAnimation(applicationContext,R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(applicationContext,R.anim.fab_close)

        fab1.startAnimation(fab_close)
        fab2.startAnimation(fab_close)

        fab1.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Te invito a instalar MainApp Client https://play.google.com/apps/testing/co.kaizenpro.mainapp.mainapp")
            //https://play.google.com/apps/testing/co.kaizenpro.mainapp.mainapp
            startActivity(Intent.createChooser(intent, "Compartir MainApp Client con"))
        }
        fab2.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Te invito a instalar MainApp Trader https://play.google.com/apps/testing/co.kaizenpro.mainapp.mainapptrader")
            //https://play.google.com/apps/testing/co.kaizenpro.mainapp.mainapp
            startActivity(Intent.createChooser(intent, "Compartir MainApp trader con"))
        }


        UserId = intent.getIntExtra("UserId",0)
        UserName = intent.getStringExtra("UserName")
        URL += UserId

        // Revision de mensajes no leidos
        Obtener_mensajes()
        // Fin revision de mensajes no leidos


        val mLocMgr = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.C2D_MESSAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.C2D_MESSAGE) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            Log.e("MapsActivity", "No se tienen permisos necesarios!, se requieren.")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.C2D_MESSAGE, Manifest.permission.C2D_MESSAGE), 225)
            return
        } else {
            Log.i("MapsActivity", "Permisos necesarios OK!.")
            mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIEMPO_ENTRE_UPDATES, MIN_CAMBIO_DISTANCIA_PARA_UPDATES, locListener, Looper.getMainLooper())
        }


        fab.setOnClickListener {
            val intent = Intent(this@MapsActivity, Reservas::class.java)
            intent.putExtra("Tid", UserId)
            intent.putExtra("Uid", 12)
            intent.putExtra("UserName", UserName)


            startActivity(intent)
        }



        var progressBar = ProgressBar(this@MapsActivity, null, android.R.attr.progressBarStyleSmall)
        progressBar.visibility = View.VISIBLE

      //  pd = progressDialog( "Calculando ubicación", "Localizando")
      //  pd.max = 100
      //  pd.progress = 25
    //    pd.show()

        fl_suscripcion.setOnClickListener {

            // Cambiar por paypal
            val url = "https://www.paypal.me/fpa18/30"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
            fl_suscripcion.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        Obtener_mensajes()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.C2D_MESSAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "Debes aceptar para disfrutar de todos los servicios de la aplicación!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.C2D_MESSAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        // Enabling MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);
        mMap.uiSettings.isZoomControlsEnabled = true

        //Obtengo la ultima localizacion conocida
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()


        var location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false))



        if (location == null) {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val provider = locationManager.getBestProvider(criteria, false)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIEMPO_ENTRE_UPDATES, MIN_CAMBIO_DISTANCIA_PARA_UPDATES, locListener, Looper.getMainLooper())

            location = locationManager.getLastKnownLocation(provider)


        }


            crear_marcadores()
            crear_marcadores_auxiliares()





        //val latitude = location.latitude
        //val longitude = location.longitude


        // Add a marker in Sydney and move the camera
      //  val sydney = LatLng(-34.603112, -58.384678)
      //  mMap.addMarker(MarkerOptions().position(sydney).title("Trader Aurora").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_dama)))

      //  val otro = LatLng(-34.602405, -58.379936)
      //  drawMarker(otro, "Trader Roger", "M")






    }

    private fun actualizar_datos(Loc: LatLng){
        val intent = Intent(this@MapsActivity, Popup_datos_aux::class.java)
        val lat = Loc.latitude
        val lng = Loc.longitude

        intent.putExtra("id",UserId)
        intent.putExtra("nombre", "")
        intent.putExtra("especialidad", "")
        intent.putExtra("enable", 1)
        intent.putExtra("lat",lat)
        intent.putExtra("lng",lng)
        intent.putExtra("tiempo",60)
        intent.putExtra("img","avatar.png")
        intent.putExtra("img_cover","")
        intent.putExtra("dir","")
        intent.putExtra("info","Debe actualizar sus datos antes de empezar")


        startActivityForResult(intent,800)
    }

    private fun ventana_inicio(){
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val gete = prefs.all
        val inic = gete.get("BIENVENIDA")

        if (inic == null || inic == "") {
            val intent = Intent(this@MapsActivity, Popup_datos_bienv::class.java)
            startActivity(intent)
        }
    }

    private fun crear_marcadores(){
        // consulta Ws para ultima posicion del trader
        URL.httpGet().responseObject(Trader.Deserializer()) { request, response, result ->
            val (traders, err) = result
            //Traders.clear()

            if(traders!!.isNotEmpty()){
                actualizado = true
              //  pd.progress= 100
              //  pd.cancel()
            }
            //Add to ArrayList
            traders?.forEach { trader ->
                Traders.add(trader)
                val lat = trader.LatLng.split(',')
                if (trader.activo.equals("1")){
                    fabalert.visibility = View.GONE
                } else {
                    fabalert.visibility = View.VISIBLE
                    fabalert.setOnClickListener {
                        Toast.makeText(this@MapsActivity,"No estas activo, por favor revisa tu suscripción!", Toast.LENGTH_LONG).show()
                    }
                }
                val ubicacion = LatLng(lat[0].toDouble(), lat[1].toDouble())
                mylocation = ubicacion
                drawMarker(mylocation,"Yo",trader.sexo)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 16F))


            }
            //Datos Globales
            if (Traders.isNotEmpty()) {
                Especialidad = Traders[0].especialidad
                tiempo = Traders[0].tespera
                enable = Traders[0].enable
                sexo = Traders[0].sexo
                edad = Traders[0].edad
                pago = Traders[0].pago

                if (Traders[0].imagen == "" || Traders[0].imagen == null) {
                    img = "avatar.png"
                } else {
                    img = Traders[0].imagen
                }
                if (Traders[0].imagen_cover == "" || Traders[0].imagen_cover == null) {
                    img_cover = "avatar.png"
                } else {
                    img_cover = Traders[0].imagen_cover
                }
                if (Traders[0].direccion == null || Traders[0].direccion == "") {
                    dir = "N/A"
                } else {
                    dir = Traders[0].direccion
                }

                if (pago==0){
                    fl_suscripcion.visibility = View.VISIBLE
                } else {
                    fl_suscripcion.visibility = View.GONE
                }

            }
/*
            fabtool.setOnClickListener {
                val intent = Intent(this@MapsActivity, Popup_datos::class.java)
                val lat = mylocation.latitude
                val lng = mylocation.longitude

                intent.putExtra("id",UserId)
                intent.putExtra("nombre", Traders[0].nombre)
                intent.putExtra("especialidad", Traders[0].especialidad)
                intent.putExtra("enable", Traders[0].enable)
                intent.putExtra("lat",lat)
                intent.putExtra("lng",lng)
                if(Traders[0].imagen==null){
                    intent.putExtra("img","avatar.png")
                }else{
                    intent.putExtra("img",Traders[0].imagen)
                }
                if(Traders[0].imagen_cover==null){
                    intent.putExtra("img_cover","avatar.png")
                }else{
                    intent.putExtra("img_cover",Traders[0].imagen_cover)
                }
                if (Traders[0].direccion==null){
                    intent.putExtra("dir","N/A")
                }else{
                    intent.putExtra("dir",Traders[0].direccion)
                }

                intent.putExtra("tiempo",Traders[0].tespera)


                startActivityForResult(intent,100)

            } */

            fabtool.setOnClickListener {
                val intent = Intent(this@MapsActivity, Perfil::class.java)
                lat = mylocation.latitude
                lng = mylocation.longitude

                startActivityForResult(intent,100)

            }


            fabpreview.setOnClickListener {
                val intent = Intent(this@MapsActivity, Preview::class.java)
                startActivityForResult(intent,100)

            }


            fabnotification.setOnClickListener {
      /*          val intent = Intent(this@MapsActivity, Popup_servicio::class.java)
                val lat = mylocation.latitude
                val lng = mylocation.longitude

                intent.putExtra("id",UserId)
                intent.putExtra("nombre", Traders[0].nombre)
                intent.putExtra("especialidad", Traders[0].especialidad)
                intent.putExtra("enable", Traders[0].enable)
                intent.putExtra("lat",lat)
                intent.putExtra("lng",lng)
                if(Traders[0].imagen==null){
                    intent.putExtra("img","avatar.png")
                }else{
                    intent.putExtra("img",Traders[0].imagen)
                }
                if(Traders[0].imagen_cover==null){
                    intent.putExtra("img_cover","avatar.png")
                }else{
                    intent.putExtra("img_cover",Traders[0].imagen_cover)
                }
                if (Traders[0].direccion==null){
                    intent.putExtra("dir","N/A")
                }else{
                    intent.putExtra("dir",Traders[0].direccion)
                }

                intent.putExtra("tiempo",Traders[0].tespera)


                startActivityForResult(intent,100)
*/
                val intent = Intent(this@MapsActivity, Reservas::class.java)
                intent.putExtra("Tid", UserId)
                intent.putExtra("UserName", UserName)


                startActivity(intent)
            }
            fabportafolio.setOnClickListener {
                val intent = Intent(this@MapsActivity, Popup_portafolio::class.java)
                val lat = mylocation.latitude
                val lng = mylocation.longitude

                intent.putExtra("id",UserId)
                intent.putExtra("nombre", Traders[0].nombre)
                intent.putExtra("especialidad", Traders[0].especialidad)
                intent.putExtra("enable", Traders[0].enable)
                intent.putExtra("lat",lat)
                intent.putExtra("lng",lng)
                if(Traders[0].imagen==null){
                    intent.putExtra("img","avatar.png")
                }else{
                    intent.putExtra("img",Traders[0].imagen)
                }
                if(Traders[0].imagen_cover==null){
                    intent.putExtra("img_cover","avatar.png")
                }else{
                    intent.putExtra("img_cover",Traders[0].imagen_cover)
                }
                if (Traders[0].direccion==null){
                    intent.putExtra("dir","N/A")
                }else{
                    intent.putExtra("dir",Traders[0].direccion)
                }

                intent.putExtra("tiempo",Traders[0].tespera)


                startActivityForResult(intent,100)

            }
            if(Traders.isNotEmpty()){
                fabvisibility.visibility = View.VISIBLE
                if(Traders[0].enable == "0"){
                    fabvisibility.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY))
                    isActivo = false
                }else{
                    fabvisibility.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2DEA98")))
                    isActivo = true
                }
            } else{
                fabvisibility.visibility = View.INVISIBLE

        }
            fabvisibility.setOnClickListener {

                if (Traders.isNotEmpty()){
                if(isActivo){
                    // lo desactivamos
                    var id = UserId
                    var tm = 60
                    var enable = 0
                        if (Traders[0].tespera != null){
                            tm = Traders[0].tespera
                        }

                    val valores = listOf("id" to id, "Enable" to enable, "Tiempo" to tm)
                    Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_visibilidad_marcador.php", valores).response { request, response, result ->

                        result.success {
                            Toast.makeText(this, "Visibilidad: Desactivado" , Toast.LENGTH_LONG).show();
                            isActivo = false
                            fabvisibility.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY))

                        }
                        result.failure {
                            Toast.makeText(this, "Error: Intente nuevamente" , Toast.LENGTH_LONG).show();

                        }
                    }
                } else {
                    // lo activamos
                    var id = UserId
                    var tm = 60
                    var enable = 1
                    if (Traders[0].tespera != null){
                        tm = Traders[0].tespera
                    }

                    val valores = listOf("id" to id, "Enable" to enable, "Tiempo" to tm)
                    Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_visibilidad_marcador.php", valores).response { request, response, result ->

                        result.success {
                            Toast.makeText(this, "Visibilidad: Activa" , Toast.LENGTH_LONG).show();
                            isActivo = true
                            fabvisibility.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2DEA98")))
                        }
                        result.failure {
                            Toast.makeText(this, "Error: Intente nuevamente" , Toast.LENGTH_LONG).show();

                        }
                    }
                }
                } else {
                    Toast.makeText(this, "Debe primero asignar un punto de ubicación para poder Activar/Desactivar Visibilidad" , Toast.LENGTH_LONG).show();
                }


            }

            fabcompartir.setOnClickListener {
                animateFAB()
              /*  val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "Te invito a instalar MainApp Trader https://play.google.com/apps/testing/co.kaizenpro.mainapp.mainapptrader")
                //https://play.google.com/apps/testing/co.kaizenpro.mainapp.mainapp
                startActivity(Intent.createChooser(intent, "Compartir con"))
            */
            }
        }



    }

    private fun crear_marcadores_auxiliares(){

        // Crea marcadores adicionales
        URLs.httpGet().responseObject(Trader.Deserializer()) { request, response, result ->
            val (traderss, err) = result

            //Add to ArrayList
            traderss?.forEach { traders ->
                Traderss.add(traders)
                val lat = traders.LatLng.split(',')
                val otro = LatLng(lat[0].toDouble(), lat[1].toDouble())
                if (traders.id != UserId){
                    drawMarker_aux(otro)
                }
            }

        }

    }


    private fun drawMarker(point: LatLng,title: String, sexo: String) {
        // Creating an instance of MarkerOptions
        val markerOptions = MarkerOptions()

        // Setting latitude and longitude for the marker
        markerOptions.position(point)

        // Set title
        markerOptions.title(title)


        // Icon
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        if (sexo=="M") {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_caballero))
        }else{
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_dama))
        }

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions)

        // Set a listener for info window events.
        mMap.setOnMarkerClickListener { marker ->
            val position = marker.position
            //Using position get Value from arraylist
            false
        }
       /* mMap.setOnMapLongClickListener { marker ->
            val latitude = marker.latitude

            false

        }*/

        mMap.setOnMapLongClickListener { marker ->
            // Removing the marker and circle from the Google Map
            mMap.clear()
            drawMarker(marker,"Yo",Traders[0].sexo)
            val intent = Intent(this, Popup::class.java)
            val lat = marker.latitude
            val lng = marker.longitude

            intent.putExtra("id",UserId)
            intent.putExtra("nombre", Traders[0].nombre)
            intent.putExtra("especialidad", Traders[0].especialidad)
            intent.putExtra("enable", Traders[0].enable)
            intent.putExtra("lat",lat)
            intent.putExtra("lng",lng)
            intent.putExtra("tiempo",Traders[0].tespera)

            if(Traders[0].imagen==""){
                intent.putExtra("img","avatar.png")
            }else{
                intent.putExtra("img",Traders[0].imagen)
            }
            if (Traders[0].direccion==""){
                intent.putExtra("dir","N/A")
            }else{
                intent.putExtra("dir",Traders[0].direccion)
            }


            startActivityForResult(intent,100)
           // Toast.makeText(this, "Nuevo punto de ubicación asignado", Toast.LENGTH_LONG).show();


        }


        /*mMap.setOnInfoWindowClickListener {  marker ->
            val position = marker.position

            val args = Bundle()
            args.putParcelable("from_position", mylocation)
            args.putParcelable("to_position", position)
            destination = position

            val intent = Intent(this, Popup::class.java)
            intent.putExtra("bundle", args)
            //startActivity(intent)
            //
            startActivityForResult(intent,100)

        } */

    }

    private fun drawMarker_aux(point: LatLng) {
        // Creating an instance of MarkerOptions
        val markerOptions = MarkerOptions()

        // Setting latitude and longitude for the marker
        markerOptions.position(point)

        // Set title
        //markerOptions.title(title)


        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_anonymous))

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions)

        // Set a listener for info window events.
        mMap.setOnMarkerClickListener { marker ->
            val position = marker.position
            //Using position get Value from arraylist
            false
        }

    }


    var locListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.i("MapsActivity", "Lat " + location.getLatitude() + " Long " + location.getLongitude())
            mylocation_real = LatLng(location.getLatitude(),location.getLongitude())
           // pd.progress= 100
           // pd.cancel()
            if(!actualizado) {
                actualizar_datos(mylocation_real)
                actualizado = true

            }
        }

        override fun onProviderDisabled(provider: String) {
            Log.i("MapsActivity", "onProviderDisabled()")
        }

        override fun onProviderEnabled(provider: String) {
            Log.i("MapsActivity", "onProviderEnabled()")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.i("MapsActivity", "onStatusChanged()")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            if(resultCode == RESULT_OK) {
                mMap.clear()
                sleep(20)
                crear_marcadores()
                crear_marcadores_auxiliares()
                mMap.clear()


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation_real, 16F))

                Toast.makeText(this, "Nuevo punto de ubicación asignado", Toast.LENGTH_LONG).show();
                   } else {

                //Toast.makeText(this, "Acción Cancelada, intente nuevamente", Toast.LENGTH_LONG).show();


                }
            }
        if(requestCode == 800){
            if(resultCode == RESULT_OK) {
                mMap.clear()
                crear_marcadores_auxiliares()
                crear_marcadores()
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation_real, 16F))

                Toast.makeText(this, "Datos actualizados", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Lo sentimos, no pudimos actualizar sus datos, intente nuevamente", Toast.LENGTH_LONG).show();
            }
        }
        }



    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    private fun animateFAB(){

        if(isFabOpen){


            fab1.startAnimation(fab_close)
            fab2.startAnimation(fab_close)
            fab1.setClickable(false)
            fab2.setClickable(false)
            isFabOpen = false

        } else {


            fab1.startAnimation(fab_open)
            fab2.startAnimation(fab_open)
            fab1.setClickable(true)
            fab2.setClickable(true)
            isFabOpen = true

        }
    }

    private fun Obtener_mensajes(){
        fabmsj.visibility = View.GONE
        var databaser = FirebaseDatabase.getInstance("https://mainapp-199018.firebaseio.com");

        var sala = "reserv"+"@"+ UserId
        var cmsj = 0

        var databaseReference = databaser.getReference(sala);//Sala de chat (nombre)
        databaseReference.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                     var client = dataSnapshot.children
                    client.forEach {
                        us ->
                        var chil = us.getValue(Reserva::class.java)
                        var cid = chil?.clientid
                        var reference = databaser.getReference(cid)
                        var query = reference.orderByChild("leido").equalTo(false)
                        query.addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // dataSnapshot is the "issue" node with all children with id 0
                                    //for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                    // do something with the individual "issues"
                                    // }
                                    var mensaje = dataSnapshot.children
                                    mensaje.forEach { us ->

                                        var leido = us.child("leido").value
                                        if (leido!!.toString().equals("false")){
                                            cmsj++
                                            fabmsj.visibility = View.VISIBLE
                                        }
                                    }


                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })
                        //fin
                    }



                }


            }

            override fun onCancelled(databaseError : DatabaseError) {

            }

        })

        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val gete = prefs.all
        val nres = gete.get("NRESERVA")

        // Si la reserva es nueva activamos notificacion
        if (nres.toString().equals("1")){
            fabmsj.visibility = View.VISIBLE
        }


        // var query = reference.orderByKey().startAt("reserv")
            if (cmsj > 0){


            }

    }















}

