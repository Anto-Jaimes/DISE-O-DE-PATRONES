package pe.edu.utp.proyecto.service.patron.state;
import pe.edu.utp.proyecto.modelo.Apuesta;
public class Apuestafinalizada implements EstadoApuesta {
    @Override
    public String getNombre() {
        return "FINALIZADA";
    }
    @Override
    public void siguienteEstado(Apuesta apuesta) {

    }
}
