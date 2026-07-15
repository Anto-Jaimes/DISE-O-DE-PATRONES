package pe.edu.utp.proyecto.service.patron.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.utp.proyecto.modelo.Usuario;
import java.util.Optional;

public interface UsuarioR extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByDni(String dni);
}
