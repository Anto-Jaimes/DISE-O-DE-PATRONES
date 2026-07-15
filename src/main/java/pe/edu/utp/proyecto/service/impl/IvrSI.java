package pe.edu.utp.proyecto.service.impl;
import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.modelo.Plataforma;
import pe.edu.utp.proyecto.repository.Plataformarepositorio;
import pe.edu.utp.proyecto.service.interfaces.PlataformaServicio;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class IvrSI implements PlataformaServicio {

    private final Plataformarepositorio repo;
    public IvrSI (Plataformarepositorio repo) {
        this.repo = repo;
    }
    @Override
    public List<Plataforma> listar() {
        return repo.findAll();
    }
    @Override
    public Plataforma guardar(Plataforma i) {
        return repo.save(i);
    }
    @Override
    public Plataforma buscarPorId(String id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public Plataforma actualizar(String id, Plataforma i){

        Plataforma existente = repo.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setMensaje(i.getMensaje());
        existente.setCodigo(i.getCodigo());
        existente.setPadre(i.getPadre());
        return repo.save(existente);
    }
    @Override
    public void eliminar(String id) {
        repo.deleteById(id);
    }
}
