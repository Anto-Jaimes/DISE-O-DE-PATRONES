package pe.edu.utp.proyecto.service.patron.facade;

import org.springframework.stereotype.Component;
import pe.edu.utp.proyecto.modelo.Apuesta;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.service.interfaces.ApuestaServicio;
import pe.edu.utp.proyecto.service.interfaces.UsuarioServicio;
import pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton;

@Component
public class ApuestaFacade {
    
    private final ApuestaServicio apuestaServicio;
    private final UsuarioServicio usuarioServicio;

    public ApuestaFacade(ApuestaServicio apuestaServicio, UsuarioServicio usuarioServicio) {
        this.apuestaServicio = apuestaServicio;
        this.usuarioServicio = usuarioServicio;
    }
    public void registrarNuevaApuesta(Apuesta apuesta, Usuario usuario) {
        if (usuario != null) {
            if (usuario.getSaldo() < apuesta.getMonto()) {
                throw new IllegalStateException("Saldo insuficiente para realizar la apuesta.");
            }
            double nuevoSaldo = usuario.getSaldo() - apuesta.getMonto();
            usuario.setSaldo(nuevoSaldo);
            usuarioServicio.actualizar(usuario.getId(), usuario);
        }
        
        apuestaServicio.iniciarApuesta(apuesta);
        
        BitacoraSingleton.getInstancia().registrar("Apuesta registrada vía Facade para: " + (usuario != null ? usuario.getNombre() : "Sistema"));
    }
}
