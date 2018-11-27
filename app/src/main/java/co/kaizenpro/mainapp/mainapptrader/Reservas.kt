package co.kaizenpro.mainapp.mainapptrader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_reservas.*

class Reservas : AppCompatActivity() {


    private var adapter: AdapterReservas? = null
    private val database: FirebaseDatabase? = null
    private val databaseReference: DatabaseReference? = null

    private val PHOTO_SEND = 1
    private val PHOTO_PERFIL = 2
    private val fotoPerfilCadena: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)
        //var database = FirebaseDatabase.getInstance();
        var database = FirebaseDatabase.getInstance("https://mainapp-199018.firebaseio.com");

        var Tid = intent.getIntExtra("Tid",0)
        val UserName = intent.getStringExtra("UserName")
        var sala = "reserv"+"@"+Tid.toString()

        nombre.setText(UserName)

        var databaseReference = database.getReference(sala);//Sala de chat (nombre)

      adapter = AdapterReservas(this);

        val l = LinearLayoutManager(this)
        rvMensajes.layoutManager = l
        rvMensajes.adapter = adapter


       /* btnEnviar.setOnClickListener {
            databaseReference.push().setValue(ReservaEnviar(txtMensaje.getText().toString(), nombre.getText().toString(), "", "1","12" ,ServerValue.TIMESTAMP))
            txtMensaje.setText("")
        }
*/
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        })

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot?, s: String?) {
                val m = dataSnapshot?.getValue<ReservaRecibir>(ReservaRecibir::class.java)
                adapter!!.addMensaje(m)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

    private fun setScrollbar() {
        rvMensajes.scrollToPosition(adapter!!.getItemCount() - 1)
    }





}