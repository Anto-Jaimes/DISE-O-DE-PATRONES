package pe.edu.utp.proyecto.service.impl;

import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.repository.UsuarioR;
import pe.edu.utp.proyecto.service.interfaces.UsuarioServicio;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioSI implements UsuarioServicio {

    private final UsuarioR repo;

    public UsuarioSI(UsuarioR repo) {
        this.repo = repo;
    }

    @Override
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @Override
    public Usuario guardar(Usuario o) {
        return repo.save(o);
    }

    @Override
    public Usuario buscarPorId(String id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Usuario actualizar(String id, Usuario o) {
        Usuario existente = repo.findById(id).orElse(null);
        if (existente == null) return null;

        existente.setTipoDocumento(o.getTipoDocumento());
        existente.setDni(o.getDni());
        existente.setNombre(o.getNombre());
        existente.setApellido(o.getApellido());
        existente.setFechaNacimiento(o.getFechaNacimiento());
        existente.setGenero(o.getGenero());
        existente.setDireccion(o.getDireccion());
        existente.setDepartamento(o.getDepartamento());
        existente.setProvincia(o.getProvincia());
        existente.setDistrito(o.getDistrito());
        existente.setTelefono(o.getTelefono());
        existente.setCodigo(o.getCodigo());
        existente.setTotalTicketApuestas(o.getTotalTicketApuestas());
        existente.setEmail(o.getEmail());
        if (o.getContrasena() != null && !o.getContrasena().isEmpty()) {
            existente.setContrasena(o.getContrasena());
        }
        existente.setSaldo(o.getSaldo());

        return repo.save(existente);
    }

    @Override
    public void eliminar(String id) {
        repo.deleteById(id);
    }

    @Override
    public Usuario buscarPorDni(String dni) {
        return repo.findByDni(dni).orElse(null);
    }
}
