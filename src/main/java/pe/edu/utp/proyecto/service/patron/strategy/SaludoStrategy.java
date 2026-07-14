package pe.edu.utp.proyecto.service.patron.strategy;
public class SaludoStrategy implements RespuestaStrategy {
    @Override
    public String responder(String opcion) {
        return "Bienvenido a Claro. Marque 1 para TicketApuestas, 2 para Soporte.";
    }
}
