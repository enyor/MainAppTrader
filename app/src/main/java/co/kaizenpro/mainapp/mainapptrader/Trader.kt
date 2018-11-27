package co.kaizenpro.mainapp.mainapptrader

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Trader(
        val id: Int,
        val nombre: String,
        val LatLng: String,
        val especialidad: String,
        val telefono: String,
        val sexo: String,
        val enable: String,
        val imagen: String,
        val imagen_cover: String,
        val direccion: String,
        val rate: Float,
        val tespera: Int,
        val activo: String,
        val edad: Int,
        val pago: Int

){
    class Deserializer: ResponseDeserializable<Array<Trader>> {
        override fun deserialize(content: String): Array<Trader>? = Gson().fromJson(content, Array<Trader>::class.java)
    }
}
