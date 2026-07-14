package pe.edu.utp.proyecto.service.impl;
import pe.edu.utp.proyecto.modelo.ReglaBono;
import pe.edu.utp.proyecto.repository.ReglaBonoR;
import pe.edu.utp.proyecto.service.interfaces.ReglaBonoServicio;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ReglaBonoSI implements ReglaBonoServicio{
    private final ReglaBonoR repo;
    public ReglaBonoSI(ReglaBonoR repo) { this.repo =repo;}
    @Override
    public List<ReglaBono> listar() {
        return repo.findAll();
    }
    @Override
    public ReglaBono guardar(ReglaBono m) {
        return repo.save(m);
    }
    @Override
    public ReglaBono buscarPorId(String id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public ReglaBono actualizar(String id, ReglaBono m){

        ReglaBono existente = repo.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setPorcentaje(m.getPorcentaje());
        existente.setTotalReglaBono(m.getTotalReglaBono());
        existente.setUsuario(m.getUsuario());
        return repo.save(existente);
    }
    @Override
    public void eliminar(String id) {
        repo.deleteById(id);
    }
}
