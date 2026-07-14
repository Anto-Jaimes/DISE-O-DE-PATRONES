package pe.edu.utp.proyecto.service.patron.strategy;
public class TicketApuestasStrategy implements RespuestaStrategy {
    @Override
    public String responder(String opcion) {
        return "Ha seleccionado TicketApuestas. Un asesor le atenderÃ¡ en breve.";
    }
}
