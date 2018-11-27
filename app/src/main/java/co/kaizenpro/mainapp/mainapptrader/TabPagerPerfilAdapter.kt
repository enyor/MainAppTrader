package co.kaizenpro.mainapp.mainapptrader

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TabPagerPerfilAdapter(fm: FragmentManager, private var tabCount: Int) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return TabPerfilFragment()
            1 -> return TabPortafolioFragment()
            2 -> return TabServiciosFragment()

        //2 -> return Tab3Fragment()
           // 3 -> return Tab3Fragment() //Tab1Fragment()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}