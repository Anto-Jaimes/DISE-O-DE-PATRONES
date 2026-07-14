package pe.edu.utp.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "plataformas")
public class Plataforma {

    @Id
    private String id;
    private String mensaje;
    private String codigo;
    
    @DBRef
    private Plataforma padre;
    
    @DBRef
    private List<Plataforma> subOpciones;
}
