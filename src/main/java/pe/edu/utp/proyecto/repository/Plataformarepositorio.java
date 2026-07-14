package pe.edu.utp.proyecto.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.utp.proyecto.modelo.Plataforma;
public interface Plataformarepositorio extends MongoRepository<Plataforma, String> {
}
