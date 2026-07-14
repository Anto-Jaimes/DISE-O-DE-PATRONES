package pe.edu.utp.proyecto.controller;
import pe.edu.utp.proyecto.service.interfaces.IvrServicioSimulado;
import pe.edu.utp.proyecto.dto.IvrRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
@RestController
@RequestMapping("/api/plataforma")
public class ApiIvrController {
    private final IvrServicioSimulado plataformaService;
    public ApiIvrController(IvrServicioSimulado plataformaService) {
        this.plataformaService = plataformaService;
    }
    @PostMapping("/responder")
    public ResponseEntity<Map<String, String>> responder(@RequestBody IvrRequest request) {
        String respuesta = plataformaService.procesarOpcion(request.getOpcion());
        return ResponseEntity.ok(Map.of("respuesta", respuesta));
    }
}
