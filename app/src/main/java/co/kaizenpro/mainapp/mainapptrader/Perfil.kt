package co.kaizenpro.mainapp.mainapptrader

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_perfil.*
import java.text.SimpleDateFormat

class Perfil : AppCompatActivity() {

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
        setContentView(R.layout.activity_perfil)



        configureTabLayout()


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)



        //Cargamos los horarios segun el calculo de tiempo
        val time = java.util.Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm:ss")
        val hora = sdf.format(time)






        return

    }

    private fun configureTabLayout() {

        //tab_layout.addTab(tab_layout.newTab().setText("Contacto"))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_user_black))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_portafolio_grey))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_check_grey))

     //   tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_servicios_grey))

        val adapter = TabPagerPerfilAdapter(supportFragmentManager,
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
                    0 -> {tab.setIcon(R.drawable.ic_user_black)}
                    1 -> {tab.setIcon(R.drawable.ic_portafolio)}
                    2 -> {tab.setIcon(R.drawable.ic_check)}

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                when (tab.position){
                    0 -> {tab.setIcon(R.drawable.ic_user_grey)}
                    1 -> {tab.setIcon(R.drawable.ic_portafolio_grey)}
                    2 -> {tab.setIcon(R.drawable.ic_check_grey)}

                }

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })



    }






    override fun onRestart() {
        super.onRestart()
      //  finish()

    }


}