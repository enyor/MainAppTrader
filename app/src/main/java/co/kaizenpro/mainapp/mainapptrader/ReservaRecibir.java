package co.kaizenpro.mainapp.mainapptrader;



public class ReservaRecibir extends Reserva {

    private String hora;

    public ReservaRecibir() {
    }

    public ReservaRecibir(String hora) {
        this.hora = hora;
    }

    public ReservaRecibir(String mensaje, String urlFoto, String nombre, String fotoPerfil, String type_mensaje, String clientid, String hora) {
        super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje, clientid);
        this.hora = hora;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
