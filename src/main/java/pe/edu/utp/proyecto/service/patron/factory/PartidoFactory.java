package pe.edu.utp.proyecto.service.patron.factory;
import pe.edu.utp.proyecto.modelo.Partido;
import org.springframework.stereotype.Component;
@Component
public class PartidoFactory {
    public Partido crearPartido(String equipo1, String equipo2) {
        Partido partido = new Partido();
        partido.setEquipo1(equipo1);
        partido.setEquipo2(equipo2);
        partido.setDeporte("Fútbol");
        return partido;
    }
}

// Archivo actualizado
