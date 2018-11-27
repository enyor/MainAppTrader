package co.kaizenpro.mainapp.mainapptrader;


import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

public class TraderDatos extends Application {

    public String img;
    public String imgcover;
    public String nombre;
    public int edad;
    public String TraderId;
    public String UserId;
    public String UserName;
    public String especialidad;
    public String enable;
    public String limite;
    public String dir;
    public int duracion;
    public LatLng frompos;
    public LatLng topos;
    public Float  rating;

    public TraderDatos (){
    }

    public TraderDatos(String img, String imgcover, String nombre, String TraderId, String UserId, String UserName, String especialidad, String enable, String limite, String dir, int duracion, Float rating){

        this.img    =   img;
        this.imgcover = imgcover;
        this.nombre =   nombre;
        this.TraderId   =   TraderId;
        this.UserId =   UserId;
        this.UserName   =   UserName;
        this.especialidad   =   especialidad;
        this.enable =   enable;
        this.limite = limite;
        this.dir    = dir;
        this.duracion   = duracion;
        this.rating     = rating;

    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;

    }
    public String getImgcover() {
        return imgcover;
    }
    public void setImgcover(String imgcover) {
        this.imgcover = imgcover;

    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre){
        this.UserName = nombre;
    }
    public String getTraderId() {
        return TraderId;
    }
    public String getUserId() {
        return UserId;
    }
    public void setUserId(String userid){
        this.UserId = userid;
    }

    public String getUserName() {
        return UserName;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad){
        this.especialidad = especialidad;
    }
    public String getEnable() {
        return enable;
    }
    public String getLimite() {
        return limite;
    }
    public String getDir() {
        return dir;
    }
    public int getDuracion() {
        return duracion;
    }
    public LatLng getFrompos() {
        return frompos;
    }
    public LatLng getTopos() {
        return topos;
    }
    public Float getRating() {
        return rating;
    }
    public void setRating(Float rating){
        this.rating = rating;
    }
}
