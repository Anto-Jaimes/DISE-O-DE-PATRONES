package pe.edu.utp.proyecto.service.impl;
import pe.edu.utp.proyecto.service.interfaces.IvrServicioSimulado;
import pe.edu.utp.proyecto.service.patron.strategy.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
@Service
public class IvrServSimuladoImpl implements IvrServicioSimulado {

    private final Map<String, RespuestaStrategy> estrategiaMap;
    private final RespuestaStrategy defaultStrategy;
    private final RespuestaStrategy usuarioStrategy;
    public IvrServSimuladoImpl() {
        estrategiaMap = new HashMap<>();
        estrategiaMap.put("1",new TicketApuestasStrategy());
        estrategiaMap.put("2", new SoporteStrategy());
        this.usuarioStrategy = new RespuestaStrategy() {
            @Override
            public String responder(String opcion) {
                return "Transfiriendo a una usuario. Por favor, espere.";
            }
        };
        estrategiaMap.put("3",usuarioStrategy);
        this.defaultStrategy = new DefaultStrategy();

        estrategiaMap.put("Inicio",new SaludoStrategy());
    }
    @Override
    public String procesarOpcion(String opcion) {
        RespuestaStrategy estrategia = estrategiaMap.get(opcion);
        if (estrategia == null) {
            estrategia = defaultStrategy;
        }
        return estrategia.responder(opcion);
    }
}
