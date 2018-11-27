package co.kaizenpro.mainapp.mainapptrader;

import java.util.Map;


public class MensajeEnviar extends Mensaje {
    private Map hora;

    public MensajeEnviar() {
    }

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String type_mensaje, Boolean leido, Map hora) {
        super(mensaje, nombre, fotoPerfil, type_mensaje, leido);
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String urlFoto, String nombre, String fotoPerfil, String type_mensaje, Boolean leido, Map hora) {
        super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje, leido);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
