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
@Document(collection = "partidos")
public class Partido {
    @Id
    private String id;
    private String equipo1;
    private String equipo2;
    private String fecha;
    private String deporte;
    private String ganador;
    private String estadio;

    private double cuotaEquipo1;
    private double cuotaEmpate;
    private double cuotaEquipo2;
    private Integer golesEquipo1;
    private Integer golesEquipo2;

    @DBRef
    private List<Apuesta> apuestas;

    public Partido(String equipo1, String equipo2, String fecha, String deporte, String estadio) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.fecha = fecha;
        this.deporte = deporte;
        this.estadio = estadio;
        this.cuotaEquipo1 = 2.0;
        this.cuotaEmpate = 3.0;
        this.cuotaEquipo2 = 2.5;
    }

    public Partido(String equipo1, String equipo2, String fecha, String deporte, double cuotaEquipo1, double cuotaEmpate, double cuotaEquipo2, String estadio) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.fecha = fecha;
        this.deporte = deporte;
        this.cuotaEquipo1 = cuotaEquipo1;
        this.cuotaEmpate = cuotaEmpate;
        this.cuotaEquipo2 = cuotaEquipo2;
        this.estadio = estadio;
    }
}
