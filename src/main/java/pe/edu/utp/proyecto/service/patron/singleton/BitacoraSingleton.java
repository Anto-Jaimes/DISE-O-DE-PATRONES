package pe.edu.utp.proyecto.service.patron.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BitacoraSingleton {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BitacoraSingleton.class);


    private static volatile BitacoraSingleton instancia;
    private List<String> historial;

    private BitacoraSingleton() {
        historial = new java.util.concurrent.CopyOnWriteArrayList<>();
    }

    public static BitacoraSingleton getInstancia() {
        if (instancia == null) {
            synchronized (BitacoraSingleton.class) {
                if (instancia == null) {
                    instancia = new BitacoraSingleton();
                }
            }
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
