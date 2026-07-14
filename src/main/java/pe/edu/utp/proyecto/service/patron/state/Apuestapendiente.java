package pe.edu.utp.proyecto.service.patron.state;
import pe.edu.utp.proyecto.modelo.Apuesta;
public class Apuestapendiente implements EstadoApuesta {
    @Override
    public String getNombre() {
        return "PENDIENTE";
    }
    @Override
    public void siguienteEstado(Apuesta apuesta) {
        apuesta.setEstadoApuesta(new Apuestaenproceso());
    }
}
