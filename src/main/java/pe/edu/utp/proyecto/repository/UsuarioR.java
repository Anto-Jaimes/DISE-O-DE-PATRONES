package pe.edu.utp.proyecto.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.utp.proyecto.modelo.Usuario;
public interface UsuarioR extends MongoRepository<Usuario, String> {
}
