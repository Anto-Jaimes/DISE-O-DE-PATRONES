package pe.edu.utp.proyecto.controller;
import pe.edu.utp.proyecto.modelo.Plataforma;
import pe.edu.utp.proyecto.service.interfaces.PlataformaServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/plataforma")
public class PlataformaGestionController {
    private final PlataformaServicio crudService;
    public PlataformaGestionController(PlataformaServicio crudService) {
        this.crudService = crudService;
    }
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaOpciones", crudService.listar());
        return "plataformas/lista";
    }
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("plataforma", new Plataforma());
        return "plataformas/form";
    }
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Plataforma plataforma) {
        crudService.guardar(plataforma);
        return "redirect:/plataforma";
    }
    @GetMapping("/simulador")
    public String verSimulador() {
        return "plataformas/telefono";
    }
}
