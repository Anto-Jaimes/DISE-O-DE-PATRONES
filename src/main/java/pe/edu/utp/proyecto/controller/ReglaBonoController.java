package pe.edu.utp.proyecto.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.utp.proyecto.modelo.ReglaBono;
import pe.edu.utp.proyecto.service.interfaces.ReglaBonoServicio;
@Controller
@RequestMapping("/reglaBonoes")
public class ReglaBonoController{
    private final ReglaBonoServicio servicio;
    public ReglaBonoController(ReglaBonoServicio servicio){
        this.servicio=servicio;
    }
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("titulo", "GestiÃƒÆ’Ã‚Â³n de ReglaBonoes");
        model.addAttribute("reglaBonoes", servicio.listar());
        return "reglaBonoes/lista";
    }
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ReglaBono reglaBono) {
        servicio.guardar(reglaBono);
        return "redirect:/reglaBonoes";
    }
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        servicio.eliminar(id);
        return "redirect:/reglaBonoes";
    }
}
