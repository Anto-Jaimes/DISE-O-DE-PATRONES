package pe.edu.utp.proyecto.service.interfaces;
import pe.edu.utp.proyecto.modelo.Apuesta;
import java.util.List;

/**
 * Servicio para gestionar las operaciones relacionadas con las Apuestas.
 */
public interface ApuestaServicio {
    /** Lista todas las apuestas registradas */
    List<Apuesta> listar();
    /** Guarda una nueva apuesta en el sistema */
    Apuesta guardar(Apuesta l);
    /** Busca una apuesta por su identificador único */
    Apuesta buscarPorId(String id);
    /** Actualiza los datos de una apuesta existente */
    Apuesta actualizar(String id, Apuesta l);
    /** Elimina una apuesta por su identificador */
    void eliminar(String id);
    /** Lista todas las apuestas asociadas a un usuario específico */
    List<Apuesta> listarPorUsuario(String usuarioId);
    /** Inicia el proceso de una apuesta, inicializando su estado */
    Apuesta iniciarApuesta(Apuesta apuesta);
    /** Marca una apuesta como en progreso */
    Apuesta marcarEnProgreso(String id);
    /** Marca una apuesta como finalizada */
    Apuesta marcarFinalizada(String id);
    /** Marca una apuesta como cancelada */
    Apuesta marcarCancelada(String id);
}
