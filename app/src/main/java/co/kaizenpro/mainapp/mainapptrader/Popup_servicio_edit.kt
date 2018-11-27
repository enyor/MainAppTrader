package co.kaizenpro.mainapp.mainapptrader

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_edit_servicio.*

class Popup_servicio_edit : Activity() {

    private var id_servicio = 0
    private var nombrer = ""
    private var infor = ""
    private var precior = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_servicio)
        id_servicio = intent.getIntExtra("id_servicio", 0)
        precior = intent.getIntExtra("precio", 0)
        nombrer = intent.getStringExtra("nombre")
        infor = intent.getStringExtra("info")

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width * .9).toInt(), (height * .85).toInt())

        nombre.setText(nombrer)
        info.setText(infor)




    btn_crear.setOnClickListener{

        val nm = nombre.text
        val inf = info.text
        val prc = precio.text

        val valores = listOf("nombre" to nm, "info" to inf, "precio" to prc, "id" to id_servicio)
        Fuel.get("http://mainapp.kaizenpro.co.uk/actualizar_servicio.php", valores).response { request, response, result ->

            result.success {
                finish()
                val resulta = "Servicio Actualizado Correctamente"
                Toast.makeText(this, resulta.toString(), Toast.LENGTH_LONG).show()
            }
            result.failure {
                val resulta = "No se pudo registrar, intente nuevamente"
                Toast.makeText(this, resulta.toString(), Toast.LENGTH_LONG).show()
            }





        }

    }


    }



}