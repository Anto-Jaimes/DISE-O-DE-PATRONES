package pe.edu.utp.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tickets_apuesta")
public class TicketApuesta {

    @Id
    private String id;
    private String tipoServicio;
    private double monto;

    @DBRef
    private Apuesta apuesta;
}
