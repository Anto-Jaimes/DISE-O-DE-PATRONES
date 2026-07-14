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
@Document(collection = "reglas_bono")
public class ReglaBono {

    @Id
    private String id;
    private double porcentaje;
    private double totalReglaBono;

    @DBRef
    private Usuario usuario;
}
