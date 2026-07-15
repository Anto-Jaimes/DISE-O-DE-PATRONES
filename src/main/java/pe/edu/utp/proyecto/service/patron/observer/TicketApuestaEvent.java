package pe.edu.utp.proyecto.service.patron.observer;
import org.springframework.context.ApplicationEvent;
public class TicketApuestaEvent extends ApplicationEvent {
    private final String mensaje;
    public TicketApuestaEvent (Object source, String mensaje) {
        super(source);
        this.mensaje = mensaje;
    }
    public String getMensaje() {
        return mensaje;
    }
}
