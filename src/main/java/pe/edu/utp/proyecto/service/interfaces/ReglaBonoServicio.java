package pe.edu.utp.proyecto.service.interfaces;
import pe.edu.utp.proyecto.modelo.ReglaBono;
import java.util.List;

/**
 * Servicio para gestionar las operaciones relacionadas con las Reglas de Bonos.
 */
public interface ReglaBonoServicio {
    /** Lista todas las reglas de bonos registradas */
    List<ReglaBono> listar();
    /** Guarda una nueva regla de bono en el sistema */
    ReglaBono guardar(ReglaBono m);
    /** Busca una regla de bono por su identificador único */
    ReglaBono buscarPorId(String id);
    /** Actualiza los datos de una regla de bono existente */
    ReglaBono actualizar(String id, ReglaBono m);
    /** Elimina una regla de bono por su identificador */
    void eliminar(String id);
}
