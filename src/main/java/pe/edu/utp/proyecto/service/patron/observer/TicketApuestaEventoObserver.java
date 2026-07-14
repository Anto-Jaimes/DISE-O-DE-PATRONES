package pe.edu.utp.proyecto.service.patron.observer;
import org.springframework.context.ApplicationEvent;
public class TicketApuestaEventoObserver extends ApplicationEvent {
    private String mensaje;
    public TicketApuestaEventoObserver (Object source, String mensaje) {
        super(source);
        this.mensaje = mensaje;
    }
    public String getMensaje() {
        return mensaje;
    }
}
