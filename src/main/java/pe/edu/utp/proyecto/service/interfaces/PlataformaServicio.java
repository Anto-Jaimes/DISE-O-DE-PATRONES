package pe.edu.utp.proyecto.service.interfaces;
import pe.edu.utp.proyecto.modelo.Plataforma;
import java.util.List;

/**
 * Servicio para gestionar las opciones de la Plataforma (IVR).
 */
public interface PlataformaServicio {
    /** Lista todas las opciones de plataforma registradas */
    List<Plataforma> listar();
    /** Guarda una nueva opción de plataforma en el sistema */
    Plataforma guardar(Plataforma i);
    /** Busca una opción de plataforma por su identificador único */
    Plataforma buscarPorId(String id);
    /** Actualiza los datos de una opción de plataforma existente */
    Plataforma actualizar(String id, Plataforma i);
    /** Elimina una opción de plataforma por su identificador */
    void eliminar(String id);
}
