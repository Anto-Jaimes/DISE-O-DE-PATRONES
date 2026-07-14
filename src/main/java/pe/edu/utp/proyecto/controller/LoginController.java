package pe.edu.utp.proyecto.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.service.interfaces.UsuarioServicio;
import pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton;

@Controller
public class LoginController {

    private final UsuarioServicio usuarioServicio;

    public LoginController(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
                            
        // Verificación de Administrador
        if ("admin".equalsIgnoreCase(username) && "admin123".equals(password)) {
            Usuario admin = new Usuario();
            admin.setId("ADMIN");
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setCodigo("ADMIN");
            admin.setRol("ADMIN");
            session.setAttribute("usuarioLogueado", admin);
            BitacoraSingleton.getInstancia().registrar("Administrador inició sesión.");
            return "redirect:/";
        }

        // Verificación de Cliente
        Usuario user = usuarioServicio.listar().stream()
                .filter(u -> (u.getCodigo() != null && u.getCodigo().equalsIgnoreCase(username) || 
                              u.getEmail() != null && u.getEmail().equalsIgnoreCase(username) ||
                              u.getDni() != null && u.getDni().equalsIgnoreCase(username))
                             && u.getContrasena() != null && u.getContrasena().equals(password))
                .findFirst().orElse(null);

        if (user != null) {
            user.setRol("CLIENTE"); // Just in case, to differentiate in views
            session.setAttribute("usuarioLogueado", user);
            BitacoraSingleton.getInstancia().registrar("Cliente inició sesión: " + user.getNombre() + " " + user.getApellido());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Código, DNI o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
        if (user != null) {
            BitacoraSingleton.getInstancia().registrar("Usuario cerró sesión: " + user.getNombre() + " " + user.getApellido());
        }
        session.invalidate();
        return "redirect:/login";
    }
}
