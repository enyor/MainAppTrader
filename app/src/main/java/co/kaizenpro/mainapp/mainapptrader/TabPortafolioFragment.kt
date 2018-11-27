package co.kaizenpro.mainapp.mainapptrader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.httpGet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pop_portafolio.*
import kotlinx.android.synthetic.main.activity_pop_portafolio.view.*

/**
 * A simple [Fragment] subclass.
 */
class TabPortafolioFragment : Fragment() {


    var img = ""
    var img_cover = ""
    var dir = ""

    private var UserId = 0
    var count = 0
    lateinit var recyclerPersonajes : RecyclerView
    var URL = "https://mainapp.kaizenpro.co.uk/consulta_portafolio.php?id=";

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v = inflater!!.inflate(R.layout.activity_pop_portafolio, container, false)

        UserId = MapsActivity.UserId
        val nombre = MapsActivity.UserName
        val especialidad = MapsActivity.Especialidad
        val enable = MapsActivity.enable
        val lat = MapsActivity.lat
        val lng = MapsActivity.lng
        val tiempo = MapsActivity.tiempo
        img = MapsActivity.img
        img_cover = MapsActivity.img_cover
        dir = MapsActivity.dir
        val sexo = MapsActivity.sexo

        URL += UserId

        recyclerPersonajes = v.findViewById<RecyclerView>(R.id.recyclerId)
        var llm = LinearLayoutManager(v.context)



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

        v.fab_agregar.setOnClickListener{
            val intent = Intent(v.context, Popup_portafolio_new::class.java)
            intent.putExtra("count", count)
            intent.putExtra("UserId",UserId)
            startActivityForResult(intent,100)

        }



        return v


    }// Required empty public constructor

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            var itemsData = arrayListOf<ItemPortafolio>()
            //  llenarLista()
            URL.httpGet().responseObject(Portafolio.Deserializer()) { request, response, result ->
                val (portafolios, err) = result


                //Add to ArrayList
                portafolios?.forEach { portafolio ->
                    itemsData.add(ItemPortafolio(portafolio.id_item, portafolio.titulo, portafolio.contenido, portafolio.imagen))
                }

                val adapter = PortafolioAdapter(itemsData)


                recyclerPersonajes.isNestedScrollingEnabled = false
                //recyclerPersonajes.setHasFixedSize(true)


                recyclerPersonajes.adapter = adapter

                count = recyclerPersonajes.adapter.itemCount

            }
        }
    }
}