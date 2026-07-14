package pe.edu.utp.proyecto.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.utp.proyecto.modelo.ReglaBono;
public interface ReglaBonoR extends MongoRepository<ReglaBono, String> {
}
