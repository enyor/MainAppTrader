package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pop_datos.*
import kotlinx.android.synthetic.main.activity_pop_datos.view.*

/**
 * A simple [Fragment] subclass.
 */
class TabPerfilFragment : Fragment() {


    val URL = "https://mainapp.kaizenpro.co.uk/actualizar_marcador.php";
    var img = ""
    var img_cover = ""
    var dir = ""
    private var UserId = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v = inflater!!.inflate(R.layout.activity_pop_datos, container, false)

        UserId = MapsActivity.UserId
        val nombre =  MapsActivity.UserName
        val especialidad =  MapsActivity.Especialidad
        val enable =  MapsActivity.enable
        val lat =  MapsActivity.lat
        val lng =  MapsActivity.lng
        val tiempo =  MapsActivity.tiempo
        img =  MapsActivity.img
        img_cover =  MapsActivity.img_cover
        dir =  MapsActivity.dir
        val sexo = MapsActivity.sexo
        val nedad = MapsActivity.edad

        val imageUric = "https://mainapp.kaizenpro.co.uk/assets/"+img
        val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+img_cover

        //Picasso.with(v.context).load(imageUric).into(v.cover)
		Glide.with(v.context).load(Uri.parse(imageUric)).into(v.cover)
		
        Glide.with(v.context)
                .load(Uri.parse(imageUri)) // add your image url
                .override(80,80)
                .centerCrop()
                .transform(CircleTransform(v.context)) // applying the image transformer
                .skipMemoryCache(true)
                .into(v.avatar)

        v.Nombre.setText(nombre)
        v.Especialidad.setText(especialidad)
        v.direccion.setText(dir)

        v.tftime.setText(tiempo.toString())
        v.tfedad.setText(nedad.toString())
        if(enable=="0"){
            v.sw1.isChecked = false
        } else {
            v.sw1.isChecked = true
        }

        if(sexo=="M"){
            v.M.isChecked = true
            v.F.isChecked = false
        }else{
            v.M.isChecked = false
            v.F.isChecked = true

        }

        v.btreservar.setOnClickListener{
            var id = UserId
            var Lat = lat
            var Lng = lng
            var especialidad = v.Especialidad.text.toString()
            var enable = 0
            if (v.sw1.isChecked) {
                enable = 1
            }else{
                enable = 0
            }
            var sexo = ""
            if (v.M.isChecked){
                sexo = "M"
            }else{
                sexo = "F"
            }
            var tm = v.tftime.text
            dir = v.direccion.text.toString()

            var nom = v.Nombre.text.toString()

            var edad = tfedad.text.toString()

            val valores = listOf("id" to id, "Lat" to Lat, "Lng" to Lng,"Enable" to enable, "Tiempo" to tm, "especialidad" to especialidad, "sexo" to sexo, "img" to img, "img_cover" to img_cover, "dir" to dir, "Nombre" to nom, "edad" to edad)
            Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_marcador_nuevo.php", valores).response { request, response, result ->


            }
            MapsActivity.UserName = nom
            MapsActivity.Especialidad = especialidad
            MapsActivity.sexo = sexo
            MapsActivity.dir =  dir
            MapsActivity.edad = edad.toInt()

            val intent = Intent(v.context, MapsActivity::class.java)
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish ()
        }

        v.avatar.setOnClickListener { val intent = Intent(v.context, UploadActivity::class.java)
            intent.putExtra("UserId",UserId.toString());
            //startActivity(intent)
            startActivityForResult(intent,400)

        }

        v.cover.setOnClickListener { val intent = Intent(v.context, UploadActivity::class.java)
            intent.putExtra("UserId","Cover_"+UserId.toString());
            //startActivity(intent)
            startActivityForResult(intent,500)

        }



        return v


    }// Required empty public constructor

    private fun onclicreservar(la:Double, ln:Double, v:Activity){

        var id = UserId
        var Lat = la
        var Lng = ln
        var especialidad = v.Especialidad.text.toString()
        var enable = 0
        if (v.sw1.isChecked) {
            enable = 1
        }else{
            enable = 0
        }
        var sexo = ""
        if (v.M.isChecked){
            sexo = "M"
        }else{
            sexo = "F"
        }
        var tm = v.tftime.text
        dir = v.direccion.text.toString()

        var nom = v.Nombre.text.toString()



        val valores = listOf("id" to id, "Lat" to Lat, "Lng" to Lng,"Enable" to enable, "Tiempo" to tm, "especialidad" to especialidad, "sexo" to sexo, "img" to img, "img_cover" to img_cover, "dir" to dir, "Nombre" to nom)
        Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_marcador_nuevo.php", valores).response { request, response, result ->


        }


        val intent = Intent(v, MapsActivity::class.java)

        v.setResult(Activity.RESULT_OK, intent)
        v.finish ()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 400){
            if(resultCode == AppCompatActivity.RESULT_OK) {
                var Fn = data!!.getStringExtra("filename")
                img = Fn
                val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+Fn
                //   Picasso.with(this@Popup_datos).load(imageUri).resize(100,100).centerInside().into(avatar)
                Glide.clear(this.avatar)
                Glide.with(this)
                        .load(Uri.parse(imageUri)) // add your image url
                        .override(80,80)
                        .centerCrop()
                        .transform(CircleTransform(this.context)) // applying the image transformer
                        .into(this.avatar)
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
                Glide.clear(this.cover)
                Glide.with(this)
                        .load(Uri.parse(imageUri)) // add your image url
                        //.override(80,80)
                        //.centerCrop()
                        // .transform(CircleTransform(this@Popup_datos)) // applying the image transformer
                        .into(this.cover)



            }
        }
    }







}