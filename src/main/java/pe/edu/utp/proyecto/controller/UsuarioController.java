package pe.edu.utp.proyecto.controller;

import pe.edu.utp.proyecto.service.interfaces.UsuarioServicio;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServicio servicio;

    public UsuarioController(UsuarioServicio servicio) {
        this.servicio = servicio;
    }


    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        servicio.eliminar(id);
        return "redirect:/?tab=2&success=usuario_eliminado";
    }
}
