package co.kaizenpro.mainapp.mainapptrader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdapterReservas extends RecyclerView.Adapter<HolderReserva> {

    private List<ReservaRecibir> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterReservas(Context c) {
        this.c = c;
    }

    public void addMensaje(ReservaRecibir m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderReserva onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_reservas,parent,false);
        return new HolderReserva(v);
    }

    @Override
    public void onBindViewHolder(HolderReserva holder, final int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        if(listMensaje.get(position).getType_mensaje().equals("2")){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(listMensaje.get(position).getUrlFoto()).into(holder.getFotoMensaje());
        }else if(listMensaje.get(position).getType_mensaje().equals("1")){
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }
     /*   if(listMensaje.get(position).getFotoPerfil().isEmpty()){
            holder.getFotoMensajePerfil().setImageResource(R.mipmap.ic_launcher_round);
        }else{
            Glide.with(c).load(listMensaje.get(position).getFotoPerfil()).into(holder.getFotoMensajePerfil());
        }*/
        String codigoHora = listMensaje.get(position).getHora();
        //Date d = new Date(codigoHora);
        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//a pm o am
        String hora = codigoHora.substring(codigoHora.length()-8);
        //holder.getHora().setText(sdf.format(d));
        holder.getHora().setText(codigoHora);

        holder.getFabb().setOnClickListener((View) -> {
                //Toast.makeText(c,listMensaje.get(position).getClientid(),Toast.LENGTH_LONG).show();
                 Intent intent = new Intent(c,Chat.class);
                 String sala = listMensaje.get(position).getClientid() +"@"+ hora;
                 intent.putExtra("sala",sala);
                 intent.putExtra("UserName",listMensaje.get(position).getNombre());
                 c.startActivity(intent);

                }

        );

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

}
