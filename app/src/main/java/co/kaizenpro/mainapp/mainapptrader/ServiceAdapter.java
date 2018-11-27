package co.kaizenpro.mainapp.mainapptrader;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.*;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServicioViewHolder>{

    ArrayList<ItemServicio> listaPortafolio;
    Context context;

    public ServiceAdapter(ArrayList<ItemServicio> listaPersonaje) {
        this.listaPortafolio=listaPersonaje;
    }

    @Override
    public ServicioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service,null,false);
        context = parent.getContext();
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServicioViewHolder holder, int position) {
        holder.txtNombre.setText(listaPortafolio.get(position).getNombre());
        holder.txtInformacion.setText(listaPortafolio.get(position).getInfo());
        holder.txtprecio.setText(listaPortafolio.get(position).getPrecio());

        holder.fedit.setOnClickListener(
                view -> {
                    Intent intent = new Intent(context, Popup_servicio_edit.class);
                    intent.putExtra("id_servicio", listaPortafolio.get(position).getIdServicio());
                    intent.putExtra("nombre", listaPortafolio.get(position).getNombre());
                    intent.putExtra("info", listaPortafolio.get(position).getInfo());
                    intent.putExtra("precio", listaPortafolio.get(position).getPrecio());


                    context.startActivity(intent);
                }
        );

        holder.fdelete.setOnClickListener(
                view -> {
                  //  Toast.makeText(context,"Presionando Borrar "+listaPortafolio.get(position).getIdServicio(), Toast.LENGTH_LONG).show();

                    Integer id = listaPortafolio.get(position).getIdServicio();

                    List<Pair> requestParams = new ArrayList<Pair>(1);

                    requestParams.add( new Pair("id_servicio", id.toString()) );

                    JSONObject json = new JSONObject();

                    try {
                        json.put("id_servicio", id.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String jsonString = new Gson().toJson(requestParams);

                    Fuel.get("http://mainapp.kaizenpro.co.uk/eliminar_servicio.php?id_servicio="+id.toString()).header( new kotlin.Pair("id_servicio",id.toString())).body(json.toString(), Charset.forName("UTF-8")).responseString(new Handler<String>() {
                        @Override
                        public void success(Request request, Response response, String s) {
                            removeAt(position);
                            Toast.makeText(context,"Servicio eliminado", Toast.LENGTH_LONG).show();


                        }

                        @Override
                        public void failure(Request request, Response response, FuelError fuelError) {
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
    public void avisar() {
    }

    public class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre,txtInformacion, txtprecio;
        FloatingActionButton fedit, fdelete;

        public ServicioViewHolder(View itemView) {
            super(itemView);
            txtNombre= (TextView) itemView.findViewById(R.id.idNombre);
            txtInformacion= (TextView) itemView.findViewById(R.id.idInfo);
            txtprecio= (TextView) itemView.findViewById(R.id.precio);
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