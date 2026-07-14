package pe.edu.utp.proyecto.service.patron.observer;
import pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SupervisorListener {
    @EventListener
    public void manejarTicketApuesta(TicketApuestaEventoObserver evento) {

        log.info("[NOTIFICACIÃ“N IMPORTANTE] Supervisor informado: {}", evento.getMensaje());
        BitacoraSingleton.getInstancia().registrar("Evento registrado: " + evento.getMensaje());
    }
}
