package pe.edu.utp.proyecto.service.interfaces;

/**
 * Servicio para simular el comportamiento de un IVR (Interactive Voice Response).
 */
public interface IvrServicioSimulado {

    /**
     * Procesa una opción seleccionada por el usuario en el IVR.
     * @param Opcion La opción seleccionada (por ejemplo, "1", "2", "Inicio")
     * @return El mensaje de respuesta del IVR
     */
    String procesarOpcion (String Opcion);
}
