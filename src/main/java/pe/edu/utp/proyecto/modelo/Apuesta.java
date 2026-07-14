package pe.edu.utp.proyecto.modelo;

import java.time.LocalDateTime;
import pe.edu.utp.proyecto.service.patron.state.EstadoApuesta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "apuestas")
public class Apuesta {

    @Id
    private String id;

    private String tipo;
    private LocalDateTime fecha;
    private String estado;
    
    @Builder.Default
    private boolean huboTicketApuesta = false;
    private double monto;
    private String resultadoApostado;
    
    @Builder.Default
    private double cuota = 1.0;
    
    @Builder.Default
    private double ganancia = 0.0;
    private String codigoTicket;
    private Integer propTarjetasRojas;
    private Integer propTarjetasAmarillas;
    private String propExpulsado;

    @Transient
    private EstadoApuesta estadoApuesta;

    private String apostadorNombreCompleto;
    private String apostadorDni;
    private String equipo1;
    private String equipo2;

    @DBRef
    private Usuario usuario;

    @DBRef
    private Partido partido;

    @DBRef
    private Plataforma plataformaActual;

    public void setEstadoApuesta(EstadoApuesta estadoApuesta) {
        this.estadoApuesta = estadoApuesta;
        if (estadoApuesta != null) {
            this.estado = estadoApuesta.getNombre();
        }
    }
}
