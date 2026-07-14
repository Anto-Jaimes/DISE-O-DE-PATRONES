package pe.edu.utp.proyecto.service.patron.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitacoraSingleton {

    private static BitacoraSingleton instancia;
    private List<String> historial;

    private BitacoraSingleton() {
        historial = new ArrayList<>();
    }

    public static BitacoraSingleton getInstancia() {
        if (instancia == null) {
            instancia = new BitacoraSingleton();
        }
        return instancia;
    }

    public void registrar(String accion) {
        String entry = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + accion;
        historial.add(entry);
        log.info("BITÃ CORA: {}", entry);
    }

    public List<String> getHistorial() {
        return historial;
    }
}
