package pe.edu.utp.proyecto.service.impl;
import java.util.List;
import org.springframework.stereotype.Service;
import pe.edu.utp.proyecto.modelo.Apuesta;
import pe.edu.utp.proyecto.repository.ApuestaR;
import pe.edu.utp.proyecto.service.interfaces.ApuestaServicio;

@Service
public class ApuestaSI implements ApuestaServicio { 
    private final ApuestaR repo;
    public ApuestaSI(ApuestaR repo) {
        this.repo = repo;
    }
    @Override
    public List<Apuesta> listar() {
        return repo.findAll();
    }
    @Override
    public Apuesta guardar(Apuesta l) {
        return repo.save(l);
    }
    @Override
    public Apuesta buscarPorId(String id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public Apuesta actualizar(String id, Apuesta l) {
        Apuesta existente = repo.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setTipo(l.getTipo());
        existente.setFecha(l.getFecha());
        existente.setEstado(l.getEstado());
        existente.setHuboTicketApuesta(l.getHuboTicketApuesta());
        existente.setMonto(l.getMonto());
        existente.setUsuario(l.getUsuario());
        existente.setPartido(l.getPartido());
        return repo.save(existente);
    }
    @Override
    public void eliminar(String id) {
        repo.deleteById(id);
    }
    @Override
    public List<Apuesta> listarPorUsuario(String usuarioId) {
        return repo.findByUsuarioId(usuarioId);
    }
    @Override
    public Apuesta iniciarApuesta(Apuesta apuesta) {
        apuesta.setEstado("EN PROCESO");
        return repo.save(apuesta);
    }
    @Override
    public Apuesta marcarEnProgreso(String id) {
        Apuesta apuesta = buscarPorId(id);
        if (apuesta == null) return null;
        apuesta.setEstado("EN PROCESO");
        return repo.save(apuesta);
    }
    @Override
    public Apuesta marcarFinalizada(String id) {
        Apuesta apuesta = buscarPorId(id);
        if (apuesta == null) return null;
        apuesta.setEstado("FINALIZADA");
        return repo.save(apuesta);
    }
    @Override
    public Apuesta marcarCancelada(String id) {
        Apuesta apuesta = buscarPorId(id);
        if (apuesta == null) return null;
        apuesta.setEstado("CANCELADA");
        return repo.save(apuesta);
    }
}
