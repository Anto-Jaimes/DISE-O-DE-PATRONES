package pe.edu.utp.proyecto.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.utp.proyecto.modelo.Partido;
public interface PartidoR extends MongoRepository<Partido, String> {
}
