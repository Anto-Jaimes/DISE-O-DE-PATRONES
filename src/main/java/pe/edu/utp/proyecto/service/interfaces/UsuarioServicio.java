package pe.edu.utp.proyecto.service.interfaces;
import pe.edu.utp.proyecto.modelo.Usuario;
import java.util.List;

/**
 * Servicio para gestionar las operaciones relacionadas con los Usuarios.
 */
public interface UsuarioServicio {
    /** Lista todos los usuarios registrados */
    List<Usuario> listar();
    /** Guarda un nuevo usuario en el sistema */
    Usuario guardar(Usuario o);
    /** Busca un usuario por su identificador único */
    Usuario buscarPorId(String id);
    /** Actualiza los datos de un usuario existente */
    Usuario actualizar(String id , Usuario o);
    /** Elimina un usuario por su identificador */
    void eliminar(String id);
}
