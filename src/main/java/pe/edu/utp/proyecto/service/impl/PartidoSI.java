package pe.edu.utp.proyecto.service.impl;
import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.repository.PartidoR;
import pe.edu.utp.proyecto.service.interfaces.PartidoServicio;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PartidoSI implements PartidoServicio {

    private final PartidoR repo;
    public PartidoSI(PartidoR repo) {
        this.repo = repo;
    }
    @Override
    public List<Partido> listar() {
        return repo.findAll();
    }
    @Override
    public Partido guardar(Partido c){
        return repo.save(c);
    }
    @Override
    public Partido buscarPorId(String id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public Partido actualizar(String id, Partido c){

        Partido existente = repo.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setEquipo1(c.getEquipo1());
        existente.setEquipo2(c.getEquipo2());
        existente.setFecha(c.getFecha());
        existente.setDeporte(c.getDeporte());
        existente.setGanador(c.getGanador());
        return repo.save(existente);
    }
    @Override
    public void eliminar(String id) {
        repo.deleteById(id);
    }
}
