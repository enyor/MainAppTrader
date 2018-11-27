package co.kaizenpro.mainapp.mainapptrader


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
import kotlinx.android.synthetic.main.fragment_tab2.view.*


/**
 * A simple [Fragment] subclass.
 */
class Tab2Fragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v = inflater!!.inflate(R.layout.fragment_tab2, container, false)

        var URL = "https://mainapp.kaizenpro.co.uk/consulta_servicio.php?id="+MapsActivity.UserId

        val imageUriC = "https://mainapp.kaizenpro.co.uk/assets/Trader_Cover_"+MapsActivity.UserId+".jpeg"

        //Picasso.with(v.context).load(imageUriC).into(v.cover)
		Glide.with(v.context).load(Uri.parse(imageUriC)).into(v.cover)
				
        v.tnombre2.setText(MapsActivity.UserName)
        v.tespecialidad2.setText(MapsActivity.Especialidad)

        var recyclerPersonajes = v.findViewById<RecyclerView>(R.id.recyclerId)
        var llm = LinearLayoutManager(activity)

        recyclerPersonajes.layoutManager = llm
        //recyclerPersonajes.setLayoutManager(LinearLayoutManager(context))

        //  llenarLista()
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




        return v


    }// Required empty public constructor
}
