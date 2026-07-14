package pe.edu.utp.proyecto.service.interfaces;
import pe.edu.utp.proyecto.modelo.Partido;
import java.util.List;

/**
 * Servicio para gestionar las operaciones relacionadas con los Partidos.
 */
public interface PartidoServicio {
    /** Lista todos los partidos registrados */
    List<Partido> listar();
    /** Guarda un nuevo partido en el sistema */
    Partido guardar(Partido c);
    /** Busca un partido por su identificador único */
    Partido buscarPorId(String id);
    /** Actualiza los datos de un partido existente */
    Partido actualizar(String id, Partido c);
    /** Elimina un partido por su identificador */
    void eliminar(String id);
}
