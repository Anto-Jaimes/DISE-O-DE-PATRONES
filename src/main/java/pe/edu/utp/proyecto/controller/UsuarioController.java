package pe.edu.utp.proyecto.controller;

import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.service.interfaces.UsuarioServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServicio servicio;

    public UsuarioController(UsuarioServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", servicio.listar());
        return "usuarios/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {

        if (usuario.getId() != null) {
            Usuario existente = servicio.buscarPorId(usuario.getId());
            if(existente != null) {
                existente.setNombre(usuario.getNombre());
                existente.setApellido(usuario.getApellido());
                existente.setDni(usuario.getDni());
                existente.setEmail(usuario.getEmail());
                existente.setSaldo(usuario.getSaldo());
                servicio.actualizar(usuario.getId(), existente);
            }
        } else {

            if (usuario.getCodigo() == null || usuario.getCodigo().isEmpty()) {
                usuario.setCodigo(usuario.getNombre().toLowerCase() + (int)(Math.random() * 1000));
            }
            if (usuario.getSaldo() == 0.0) {
                usuario.setSaldo(10.0);
            }
            if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
                usuario.setContrasena("123456");
            }
            servicio.guardar(usuario);
        }
        return "redirect:/?tab=5&success=usuario_guardado";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        Usuario op = servicio.buscarPorId(id);
        if (op == null) return "redirect:/?tab=5";

        model.addAttribute("usuario", op);

        return "usuarios/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        servicio.eliminar(id);
        return "redirect:/?tab=5&success=usuario_eliminado";
    }
}
