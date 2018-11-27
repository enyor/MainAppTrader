package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_edit_portafolio.*

class Popup_portafolio_edit : Activity() {

    private var UserId = 0
    var count = 0
    var img = ""
    var nombrer = ""
    var infor = ""
    var id = 0
    var imagen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_portafolio)

        imagen = intent.getStringExtra("imagen")
        nombrer = intent.getStringExtra("nombre")
        infor = intent.getStringExtra("info")
        id = intent.getIntExtra("iditem",0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.importantForAutofill =
                    View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS;
        }

        nombre.setText(nombrer)
        info.setText(infor)

        val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+imagen
        //   Picasso.with(this@Popup_datos).load(imageUri).resize(100,100).centerInside().into(avatar)
        Glide.with(this@Popup_portafolio_edit)
                .load(Uri.parse(imageUri))
                .into(cover)


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width * .9).toInt(), (height * .85).toInt())

        cover.setOnClickListener { val intent = Intent(this@Popup_portafolio_edit, UploadActivity::class.java)

            intent.putExtra("UserId",imagen);
            //startActivity(intent)
            startActivityForResult(intent,500)

        }

        btn_crear.setOnClickListener{

            val nm = nombre.text
            val inf = info.text

            if (img == ""){
                img = imagen
            }


            val valores = listOf("nombre" to nm, "info" to inf, "imagen" to img, "id" to id)
            Fuel.get("https://mainapp.kaizenpro.co.uk/actualizar_item_portafolio.php", valores).response { request, response, result ->

                result.success {
                    finish()
                    val resulta = "Item Registrado Correctamente"
                    Toast.makeText(this, resulta.toString(), Toast.LENGTH_LONG).show()
                }
                result.failure {
                    val resulta = "No se pudo registrar, intente nuevamente"
                    Toast.makeText(this, resulta.toString(), Toast.LENGTH_LONG).show()
                }





            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 500){
            if(resultCode == AppCompatActivity.RESULT_OK) {
                var Fn = data!!.getStringExtra("filename")
                img = Fn
                val imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+Fn
                //   Picasso.with(this@Popup_datos).load(imageUri).resize(100,100).centerInside().into(avatar)
                Glide.clear(cover)
                Glide.with(this@Popup_portafolio_edit)
                        .load(Uri.parse(imageUri))
                        .into(cover)

            }
        }
    }




}