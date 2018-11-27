package co.kaizenpro.mainapp.mainapptrader;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.*;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class PortafolioAdapter extends RecyclerView.Adapter<PortafolioAdapter.PortafolioViewHolder>{
    Context context;
    ArrayList<ItemPortafolio> listaPortafolio;

    public PortafolioAdapter(ArrayList<ItemPortafolio> listaPersonaje) {
        this.listaPortafolio=listaPersonaje;
    }

    @Override
    public PortafolioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portafolio,null,false);
        context = parent.getContext();
        return new PortafolioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PortafolioViewHolder holder, int position) {

        holder.txtNombre.setText(listaPortafolio.get(position).getNombre());
        holder.txtInformacion.setText(listaPortafolio.get(position).getInfo());
        //holder.foto.setImageResource(listaPortafolio.get(position).getImagenId());
        String imageUri = "https://mainapp.kaizenpro.co.uk/assets/"+listaPortafolio.get(position).getImagenId();
        Glide.with(context)
                .load(Uri.parse(imageUri)) // add your image url
                .into(holder.foto);

        holder.fedit.setOnClickListener(
                view -> {
                    Intent intent = new Intent(context, Popup_portafolio_edit.class);

                    intent.putExtra("iditem", listaPortafolio.get(position).getIdItem());
                    intent.putExtra("nombre", listaPortafolio.get(position).getNombre());
                    intent.putExtra("info", listaPortafolio.get(position).getInfo());
                    intent.putExtra("imagen", listaPortafolio.get(position).getImagenId());

                    context.startActivity(intent);
                }
        );

        holder.fdelete.setOnClickListener(
                view -> {
                    //  Toast.makeText(context,"Presionando Borrar "+listaPortafolio.get(position).getIdServicio(), Toast.LENGTH_LONG).show();

                    Integer id = listaPortafolio.get(position).getIdItem();

                    List<Pair> requestParams = new ArrayList<Pair>(1);

                    requestParams.add( new Pair("id_item", id.toString()) );

                    JSONObject json = new JSONObject();

                    try {
                        json.put("id_item", id.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String jsonString = new Gson().toJson(requestParams);

                    Fuel.get("https://mainapp.kaizenpro.co.uk/eliminar_item_portafolio.php?id_item="+id.toString()).header( new kotlin.Pair("id_servicio",id.toString())).body(json.toString(), Charset.forName("UTF-8")).responseString(new Handler<String>() {
                        @Override
                        public void success(Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                            removeAt(position);
                            Toast.makeText(context,"Item eliminado", Toast.LENGTH_LONG).show();





                        }

                        @Override
                        public void failure(Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {
                            Toast.makeText(context,"Error eliminando, intente nuevamente", Toast.LENGTH_LONG).show();

                        }
                    });

                }
        );

    }

    @Override
    public int getItemCount() {
        return listaPortafolio.size();
    }

    public class PortafolioViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre,txtInformacion;
        FloatingActionButton fedit, fdelete;
        ImageView foto;

        public PortafolioViewHolder(View itemView) {
            super(itemView);
            txtNombre= (TextView) itemView.findViewById(R.id.idNombre);
            txtInformacion= (TextView) itemView.findViewById(R.id.idInfo);
            foto= (ImageView) itemView.findViewById(R.id.idImagen);
            fedit= (FloatingActionButton) itemView.findViewById(R.id.fb_edit);
            fdelete= (FloatingActionButton) itemView.findViewById(R.id.fb_delete);
        }
    }

    public void removeAt(int position){
        listaPortafolio.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listaPortafolio.size());

    }
}