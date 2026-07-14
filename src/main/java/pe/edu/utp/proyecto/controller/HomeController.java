package pe.edu.utp.proyecto.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.service.interfaces.ApuestaServicio;
import pe.edu.utp.proyecto.service.interfaces.PartidoServicio;
import pe.edu.utp.proyecto.service.interfaces.UsuarioServicio;
import pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final PartidoServicio partidoServicio;
    private final ApuestaServicio apuestaServicio;
    private final UsuarioServicio usuarioServicio;

    public HomeController(PartidoServicio partidoServicio, ApuestaServicio apuestaServicio, UsuarioServicio usuarioServicio) {
        this.partidoServicio = partidoServicio;
        this.apuestaServicio = apuestaServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        List<Partido> partidos = partidoServicio.listar();
        model.addAttribute("listaPartidos", partidos);
        model.addAttribute("todosLosUsuarios", usuarioServicio.listar());
        model.addAttribute("todasLasApuestas", apuestaServicio.listar());
        for (Partido p : partidos) {
            String key = getMatchKey(p.getEquipo1(), p.getEquipo2());
            model.addAttribute(key, p);
        }
        if (usuarioLogueado != null) {
            boolean isAdmin = "ADMIN".equals(usuarioLogueado.getRol());
            model.addAttribute("isAdmin", isAdmin);
            
            if (isAdmin) {
                model.addAttribute("misApuestas", apuestaServicio.listar()); // Admin ve todo
            } else {
                model.addAttribute("misApuestas", apuestaServicio.listarPorUsuario(usuarioLogueado.getId()));
            }
            BitacoraSingleton.getInstancia().registrar((isAdmin ? "Administrador" : "Cliente") + " ingresó al Centro de Apuestas Mundial 2026: " + usuarioLogueado.getCodigo());
        } else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("misApuestas", new ArrayList<>());
        }

        return "index";
    }

    private String getMatchKey(String local, String visitante) {
        return (local + "_" + visitante)
                .replaceAll(" ", "")
                .replaceAll("\\.", "")
                .replaceAll("/", "_");
    }

}
