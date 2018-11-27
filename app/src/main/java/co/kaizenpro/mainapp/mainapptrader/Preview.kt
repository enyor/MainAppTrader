package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.maps.model.LatLng



import org.json.JSONArray
import java.text.SimpleDateFormat
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_preview.*


class Preview : AppCompatActivity() {

    var texto = ""
    lateinit var fromPosition: LatLng
    lateinit var toPosition: LatLng
    var nombre = ""
    var especialidad = ""
    var hora = ""
    var UserName = ""
    var sala = ""
    var dir = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)


     /*   val bundle = intent.getParcelableExtra<Bundle>("bundle")
        fromPosition = bundle.getParcelable<LatLng>("from_position")
        toPosition = bundle.getParcelable<LatLng>("to_position")
        nombre = intent.getStringExtra("nombre")
        especialidad = intent.getStringExtra("especialidad")
        val enable = intent.getStringExtra("enable")
        val UserId = intent.getIntExtra(" Id",0)
        val duracion = intent.getIntExtra("duracion",0)
        val limite = intent.getIntExtra("limite",60)
        UserName = intent.getStringExtra("UserName")
        val Uid = intent.getIntExtra("Uid",0)
        val Tid = intent.getIntExtra("Tid",0)
        val imgt = intent.getStringExtra("img")
        val imgtc = intent.getStringExtra("img_cover")
        dir = intent.getStringExtra("dir")
        sala = Uid.toString()+"@"+Tid.toString()

*/

      // setSupportActionBar(toolbar)

     //   val bar = actionBar
     //   bar.setDisplayShowHomeEnabled(false)
     //   bar.setDisplayShowTitleEnabled(false)


        //

   /*     fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        } */
        configureTabLayout()


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        //comentado para dejar ajuste completo
      //  window.setLayout((width * .9).toInt(), (height * .9).toInt())

       // val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+imgt
//        val imageUriC = "https://mainapp.kaizenpro.co.uk/assets/"+imgtc
       // val imageUriC = "https://mainapp.kaizenpro.co.uk/assets/Trader_Cover_"+Tid.toString()+".jpeg"

        //val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //val v = inflater.inflate(R.layout.custom_info_window, null)

        //val avatar = v.findViewById<ImageView>(R.id.img)

        // Comentado segun cambio semana 1 de septiembre
       // Picasso.with(this@TabLayoutDemoActivity).load(imageUriC).into(cover)

      /*  Picasso.with(this@TabLayoutDemoActivity).load(imageUri).into( object: com.squareup.picasso.Target{

            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                //cover.background = BitmapDrawable(this@TabLayoutDemoActivity.getResources(), bitmap)
                Log.d("TAG", "Image loaded")
            }

            override fun onBitmapFailed(errorDrawable: Drawable) {
                Log.d("TAG", "FAILED")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {
                Log.d("TAG", "Prepare Load")
            }
        })
        */



      /*  Picasso.with(this@TabLayoutDemoActivity)
                .load(imageUri)
                .resize(100,100)
                .centerCrop()
                .transform(CircleTransformPicasso())
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(img)
*/

      /*  Glide.with(this@TabLayoutDemoActivity)
                .load(Uri.parse(imageUri)) // add your image url
                .override(100,100)
                .centerCrop()
                .transform(CircleTransform(this@TabLayoutDemoActivity)) // applying the image transformer
                .into(img)*/

        //Picasso.with(this@Popup).load(imageUri).transform(CircleTransform()).into(avatar)

      //  tnombre2.setText(nombre)
        //tespecialidad2.setText(especialidad)

//        btnavegar.isEnabled = false
//        btnavegar.setOnClickListener{onclicreservar()}


        //Cargamos los horarios segun el calculo de tiempo
        val time = java.util.Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm:ss")
        val hora = sdf.format(time)




        // calculo de horarios y radibutton




        //val TraderDatos = applicationContext as TraderDatos

       // TraderDatos.setImg(imageUri)




        return

    }

    private fun configureTabLayout() {

        //tab_layout.addTab(tab_layout.newTab().setText("Contacto"))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_portafolio))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_check_grey))

     //   tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_servicios_grey))

        val adapter = TabPagerPreviewAdapter(supportFragmentManager,
                tab_layout.tabCount)
        pager.adapter = adapter

        pager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
               // tab.setIcon(R.drawable.ic_check)
                when (tab.position){
                    0 -> {tab.setIcon(R.drawable.ic_portafolio)}
                    1 -> {tab.setIcon(R.drawable.ic_check)}

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                when (tab.position){
                    0 -> {tab.setIcon(R.drawable.ic_portafolio_grey)}
                    1 -> {tab.setIcon(R.drawable.ic_check_grey)}

                }

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })



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

    override fun onRestart() {
        super.onRestart()
      //  finish()

    }


}