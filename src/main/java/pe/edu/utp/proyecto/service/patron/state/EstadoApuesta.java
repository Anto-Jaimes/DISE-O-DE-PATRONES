package pe.edu.utp.proyecto.service.patron.state;
import pe.edu.utp.proyecto.modelo.Apuesta;
public interface EstadoApuesta {
    String getNombre();
    void siguienteEstado(Apuesta apuesta);
}
