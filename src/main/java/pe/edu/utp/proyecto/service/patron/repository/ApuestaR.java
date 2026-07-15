package pe.edu.utp.proyecto.service.patron.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.utp.proyecto.modelo.Apuesta;
import java.util.List;

public interface ApuestaR extends MongoRepository<Apuesta, String> {
    List<Apuesta> findByUsuarioId(String usuarioId);
}
