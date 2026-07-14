package pe.edu.utp.proyecto.service.interfaces;
import pe.edu.utp.proyecto.modelo.TicketApuesta;
import java.util.List;

/**
 * Servicio para gestionar las operaciones relacionadas con los Tickets de Apuesta.
 */
public interface TicketApuestaServicio {
    /** Lista todos los tickets de apuesta registrados */
    List<TicketApuesta> listar();
    /** Guarda un nuevo ticket de apuesta en el sistema */
    TicketApuesta guardar(TicketApuesta v);
    /** Busca un ticket de apuesta por su identificador único */
    TicketApuesta buscarPorId(String id);
    /** Actualiza los datos de un ticket de apuesta existente */
    TicketApuesta actualizar(String id, TicketApuesta v);
    /** Elimina un ticket de apuesta por su identificador */
    void eliminar(String id);
    /** Calcula el monto total acumulado de todos los tickets de apuestas */
    double calcularTotalTicketApuestas();
}
