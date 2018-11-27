package co.kaizenpro.mainapp.mainapptrader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.kaizenpro.mainapp.mainapptrader.R.id.txtMensaje
import co.kaizenpro.mainapp.mainapptrader.R.id.nombre
import kotlinx.android.synthetic.main.activity_chat.*
import android.support.v7.widget.RecyclerView
import co.kaizenpro.mainapp.mainapptrader.R.id.fotoPerfil
import com.bumptech.glide.Glide
import co.kaizenpro.mainapp.mainapptrader.R.id.nombre
import com.google.android.gms.tasks.OnSuccessListener
import android.content.Intent
import co.kaizenpro.mainapp.mainapptrader.R.id.rvMensajes
import co.kaizenpro.mainapp.mainapptrader.R.id.rvMensajes
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageButton
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import com.google.firebase.database.ChildEventListener






class Chat : AppCompatActivity() {


    private var adapter: AdapterMensajes? = null
    private val database: FirebaseDatabase? = null
    private lateinit var databaseReference: DatabaseReference
    var childListener: ChildEventListener? = null

    private val PHOTO_SEND = 1
    private val PHOTO_PERFIL = 2
    private val fotoPerfilCadena: String? = null
    val PREFS_FILENAME = "co.kaizenpro.mainapp.mainapptrader.prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //var database = FirebaseDatabase.getInstance();
        var database = FirebaseDatabase.getInstance("https://mainapp-199018.firebaseio.com");
        //var Uid = intent.getIntExtra("Uid",0)
        //var Tid = intent.getIntExtra("Tid",0)
        val UserName = intent.getStringExtra("UserName")
        //var sala = Uid.toString()+"@"+Tid.toString()
        var sala = intent.getStringExtra("sala")


        nombre.setText(UserName)

        //liberamos el aviso de reserva nueva
        val prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        val edit = prefs.edit()
        edit.putString("NRESERVA", "0")
        edit.commit()


        databaseReference = database.getReference(sala);//Sala de chat (nombre)

      adapter = AdapterMensajes(this);

        val l = LinearLayoutManager(this)
        rvMensajes.layoutManager = l
        rvMensajes.adapter = adapter

        btnEnviar.setOnClickListener {
            databaseReference.push().setValue(MensajeEnviar(txtMensaje.getText().toString(), nombre.getText().toString(), "", "1", false, ServerValue.TIMESTAMP))
            txtMensaje.setText("")
        }

        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        })



        childListener = databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot?, s: String?) {
                val m = dataSnapshot?.getValue<MensajeRecibir>(MensajeRecibir::class.java)
                val k = dataSnapshot?.key
                val db = databaseReference.child(k)
                val camp = HashMap<String, Any>()
                camp.put("leido", true)
                db.updateChildren(camp)

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

    override fun onPause() {
        super.onPause()
        databaseReference.removeEventListener(childListener)
    }



}
