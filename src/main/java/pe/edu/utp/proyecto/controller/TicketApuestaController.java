package pe.edu.utp.proyecto.controller;
import pe.edu.utp.proyecto.service.interfaces.TicketApuestaServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/tickets")
public class TicketApuestaController {
    private final TicketApuestaServicio ticketServicio;
    public TicketApuestaController(TicketApuestaServicio ticketServicio) {
        this.ticketServicio = ticketServicio;
    }
    @GetMapping
    public String listarTicketApuestas(Model model) {
        model.addAttribute("listaTicketApuestas", ticketServicio.listar());
        model.addAttribute("montoTotal", ticketServicio.calcularTotalTicketApuestas());
        return "tickets/lista";
    }
}
