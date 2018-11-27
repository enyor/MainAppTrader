package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_new_servicio.*
import kotlinx.android.synthetic.main.activity_pop_servicio.*
import android.view.View.OnDragListener



class Popup_servicio_new : Activity() {

    private var UserId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_servicio)
        UserId = intent.getIntExtra("UserId", 0)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width * .9).toInt(), (height * .85).toInt())


    btn_crear.setOnClickListener{

        val nm = nombre.text
        val inf = info.text
        val prc = precio.text

        val valores = listOf("nombre" to nm, "info" to inf, "precio" to prc, "id" to UserId)
        Fuel.get("https://mainapp.kaizenpro.co.uk/crear_servicio.php", valores).response { request, response, result ->

            result.success {
                finish()
                val resulta = "Servicio Registrado Correctamente"
                Toast.makeText(this,resulta.toString(),Toast.LENGTH_LONG).show()
            }
            result.failure {
                val resulta = "No se pudo registrar, intente nuevamente"
                Toast.makeText(this,resulta.toString(),Toast.LENGTH_LONG).show()
            }





        }

    }


    }

}