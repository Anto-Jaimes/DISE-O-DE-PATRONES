package pe.edu.utp.proyecto.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.utp.proyecto.modelo.TicketApuesta;

public interface TicketApuestaR extends MongoRepository<TicketApuesta, String> {
}
