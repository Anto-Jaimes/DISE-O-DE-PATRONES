package pe.edu.utp.proyecto.service.patron.state;
import pe.edu.utp.proyecto.modelo.Apuesta;
public class Apuestaenproceso implements EstadoApuesta {
    @Override
    public String getNombre() {
        return "EN PROCESO";
    }
    @Override
    public void siguienteEstado(Apuesta apuesta) {
        apuesta.setEstadoApuesta(new Apuestafinalizada());
    }
}
