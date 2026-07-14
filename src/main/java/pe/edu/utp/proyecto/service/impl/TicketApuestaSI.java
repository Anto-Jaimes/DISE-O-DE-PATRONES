package pe.edu.utp.proyecto.service.impl;
import pe.edu.utp.proyecto.service.patron.observer.TicketApuestaEventoObserver;
import pe.edu.utp.proyecto.modelo.TicketApuesta;
import pe.edu.utp.proyecto.repository.TicketApuestaR;
import pe.edu.utp.proyecto.service.interfaces.TicketApuestaServicio;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TicketApuestaSI implements TicketApuestaServicio{
    private final TicketApuestaR repo;
    private final ApplicationEventPublisher publisher;
    public TicketApuestaSI (TicketApuestaR repo, ApplicationEventPublisher publisher){
        this.repo = repo;
        this.publisher = publisher;
    }
    @Override
    public List<TicketApuesta> listar() {
        return repo.findAll();
    }
    @Override
    public TicketApuesta guardar(TicketApuesta v) {

        TicketApuesta nuevaTicketApuesta = repo.save(v);
        publisher.publishEvent(new TicketApuestaEventoObserver(this, "Nueva ticket registrada por monto: " + v.getMonto()));
        return nuevaTicketApuesta;
    }
    @Override
    public TicketApuesta buscarPorId(String id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public TicketApuesta actualizar(String id, TicketApuesta v){

        TicketApuesta existente = repo.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setTipoServicio(v.getTipoServicio());
        existente.setMonto(v.getMonto());
        existente.setApuesta(v.getApuesta());
        return repo.save(existente);
    }
    @Override
    public void eliminar(String id) {
        repo.deleteById(id);
    }
    @Override
    public double calcularTotalTicketApuestas() {
        return repo.findAll().stream().mapToDouble(TicketApuesta::getMonto).sum();
    }
}
