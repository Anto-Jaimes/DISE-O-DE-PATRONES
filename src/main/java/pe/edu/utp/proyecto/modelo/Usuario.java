package pe.edu.utp.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    private String nombre;
    private String apellido;
    private String dni;
    
    @Indexed(unique = true)
    private String codigo;

    @Builder.Default
    private int totalTicketApuestas = 0;
    private String email;
    private String contrasena;
    
    @Builder.Default
    private double saldo = 0.0;
    private String rol;
    
    private String tipoDocumento;
    private String fechaNacimiento;
    private String genero;
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    private String telefono;

    @DBRef
    private List<Apuesta> apuestas;
}
