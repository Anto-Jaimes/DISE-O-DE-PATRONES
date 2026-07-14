package pe.edu.utp.proyecto.service.patron.state;
import pe.edu.utp.proyecto.modelo.Apuesta;
public class EstadoCancelado implements EstadoApuesta {
    @Override
    public String getNombre() {
        return "CANCELADA";
    }
    @Override
    public void siguienteEstado(Apuesta apuesta) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
