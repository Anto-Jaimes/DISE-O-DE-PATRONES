package pe.edu.utp.proyecto.controller;

import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.modelo.Apuesta;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.service.patron.factory.PartidoFactory;
import pe.edu.utp.proyecto.service.interfaces.PartidoServicio;
import pe.edu.utp.proyecto.repository.ApuestaR;
import pe.edu.utp.proyecto.repository.UsuarioR;
import pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/partidos")
public class PartidoController {

    private final PartidoServicio servicio;
    private final PartidoFactory factory;
    private final ApuestaR apuestaRepo;
    private final UsuarioR usuarioRepo;

    public PartidoController(PartidoServicio servicio, PartidoFactory factory, ApuestaR apuestaRepo, UsuarioR usuarioRepo) {
        this.servicio = servicio;
        this.factory = factory;
        this.apuestaRepo = apuestaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("partidos", servicio.listar());
        return "partidos/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("partido", new Partido());
        return "partidos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Partido partidoDesdeFormulario) {

        if (partidoDesdeFormulario.getDeporte() == null || partidoDesdeFormulario.getDeporte().isEmpty()) {
            partidoDesdeFormulario.setDeporte("Fútbol");
        }
        servicio.guardar(partidoDesdeFormulario);
        return "redirect:/partidos";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public java.util.Map<String, Object> getPartidoApi(@PathVariable String id) {
        Partido p = servicio.buscarPorId(id);
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        if (p != null) {
            map.put("id", p.getId());
            map.put("equipo1", p.getEquipo1());
            map.put("equipo2", p.getEquipo2());
            map.put("cuotaEquipo1", p.getCuotaEquipo1());
            map.put("cuotaEmpate", p.getCuotaEmpate());
            map.put("cuotaEquipo2", p.getCuotaEquipo2());
            map.put("ganador", p.getGanador());
            map.put("estadio", p.getEstadio());
            map.put("fecha", p.getFecha());
        }
        return map;
    }

    @PostMapping("/resolver")
    public String resolverPartido(
            @RequestParam String partidoId,
            @RequestParam String equipo1,
            @RequestParam String equipo2,
            @RequestParam Double cuotaEquipo1,
            @RequestParam Double cuotaEmpate,
            @RequestParam Double cuotaEquipo2,
            @RequestParam(required = false) Integer golesEquipo1,
            @RequestParam(required = false) Integer golesEquipo2,
            @RequestParam(required = false) String ganador,
            HttpSession session) {
        Partido partido = servicio.buscarPorId(partidoId);
        if (partido == null) {
            return "redirect:/?error=partido_no_encontrado";
        }

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        partido.setEquipo1(equipo1);
        partido.setEquipo2(equipo2);
        partido.setCuotaEquipo1(cuotaEquipo1);
        partido.setCuotaEmpate(cuotaEmpate);
        partido.setCuotaEquipo2(cuotaEquipo2);
        partido.setGolesEquipo1(golesEquipo1);
        partido.setGolesEquipo2(golesEquipo2);

        String ganadorDeterminado = ganador;
        // Si no mandaron ganador explicitamente pero hay goles definidos
        if (golesEquipo1 != null && golesEquipo2 != null) {
            if (golesEquipo1 > golesEquipo2) {
                ganadorDeterminado = "EQUIPO 1";
            } else if (golesEquipo2 > golesEquipo1) {
                ganadorDeterminado = "EQUIPO 2";
            } else {
                ganadorDeterminado = "EMPATE";
            }
        }

        if (ganadorDeterminado != null && !ganadorDeterminado.trim().isEmpty()) {
            if (ganadorDeterminado.equalsIgnoreCase("EQUIPO 1") || ganadorDeterminado.equalsIgnoreCase("EQUIPO 2") || ganadorDeterminado.equalsIgnoreCase("EMPATE")) {
                resolverYProcesarApuestas(partido, ganadorDeterminado, session, usuarioLogueado);
                avanzarGanadorAlSiguientePartido(partido, ganadorDeterminado, session, usuarioLogueado);
            } else {
                resolveBackwards(ganadorDeterminado, partido, session, usuarioLogueado);
            }
        } else {
            servicio.actualizar(partido.getId(), partido);
        }

        if (usuarioLogueado != null) {
            session.setAttribute("usuarioLogueado", usuarioLogueado);
        }

        return "redirect:/?success=partido_resuelto";
    }

    private void avanzarGanadorAlSiguientePartido(Partido partidoActual, String ganadorSeleccionado, HttpSession session, Usuario usuarioLogueado) {
        String equipoGanador = ganadorSeleccionado.equalsIgnoreCase("EQUIPO 1") ? partidoActual.getEquipo1() : 
                               (ganadorSeleccionado.equalsIgnoreCase("EQUIPO 2") ? partidoActual.getEquipo2() : null);
        
        if (equipoGanador == null) return; // Si es empate, no se avanza

        List<Partido> todos = servicio.listar();
        int indexActual = -1;
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getId().equals(partidoActual.getId())) {
                indexActual = i;
                break;
            }
        }

        if (indexActual == -1) return;

        int nextIndex = -1;
        boolean esEquipo1 = true;
        boolean esSemifinal = false;
        
        if (indexActual >= 0 && indexActual <= 15) { // 16avos -> 8vos
            nextIndex = 16 + (indexActual / 2);
            esEquipo1 = (indexActual % 2 == 0);
        } else if (indexActual >= 16 && indexActual <= 23) { // 8vos -> 4tos
            nextIndex = 24 + ((indexActual - 16) / 2);
            esEquipo1 = ((indexActual - 16) % 2 == 0);
        } else if (indexActual >= 24 && indexActual <= 27) { // 4tos -> Semis
            nextIndex = 28 + ((indexActual - 24) / 2);
            esEquipo1 = ((indexActual - 24) % 2 == 0);
        } else if (indexActual >= 28 && indexActual <= 29) { // Semis -> Final
            esSemifinal = true;
            nextIndex = 31; // Partido de la Final (idx 31)
            esEquipo1 = ((indexActual - 28) % 2 == 0);
        }

        if (nextIndex != -1 && nextIndex < todos.size()) {
            Partido nextPartido = todos.get(nextIndex);
            if (esEquipo1) {
                nextPartido.setEquipo1(equipoGanador);
            } else {
                nextPartido.setEquipo2(equipoGanador);
            }
            servicio.actualizar(nextPartido.getId(), nextPartido);

            if (esSemifinal && todos.size() > 30) {
                String equipoPerdedor = ganadorSeleccionado.equalsIgnoreCase("EQUIPO 1") ? partidoActual.getEquipo2() : partidoActual.getEquipo1();
                Partido tercerPuesto = todos.get(30); // 3er Puesto (idx 30)
                if (esEquipo1) {
                    tercerPuesto.setEquipo1(equipoPerdedor);
                } else {
                    tercerPuesto.setEquipo2(equipoPerdedor);
                }
                servicio.actualizar(tercerPuesto.getId(), tercerPuesto);
            }
        }
    }

    private void resolveBackwards(String selectedTeam, Partido partido, HttpSession session, Usuario usuarioLogueado) {
        if (selectedTeam == null || selectedTeam.isEmpty() || selectedTeam.equalsIgnoreCase("EMPATE")) {
            return;
        }
        if (isTeamOrCandidate(partido.getEquipo1(), selectedTeam)) {
            resolverYProcesarApuestas(partido, "EQUIPO 1", session, usuarioLogueado);
            avanzarGanadorAlSiguientePartido(partido, "EQUIPO 1", session, usuarioLogueado);
        } 
        else if (isTeamOrCandidate(partido.getEquipo2(), selectedTeam)) {
            resolverYProcesarApuestas(partido, "EQUIPO 2", session, usuarioLogueado);
            avanzarGanadorAlSiguientePartido(partido, "EQUIPO 2", session, usuarioLogueado);
        }
    }

    private boolean isTeamOrCandidate(String slotText, String team) {
        if (slotText == null || team == null) return false;
        String slotTextLower = slotText.toLowerCase();
        String teamLower = team.toLowerCase();
        return slotTextLower.contains(teamLower);
    }

    private void resolverYProcesarApuestas(Partido partido, String ganadorResultado, HttpSession session, Usuario usuarioLogueado) {
        partido.setGanador(ganadorResultado);
        servicio.actualizar(partido.getId(), partido);
        List<Apuesta> apuestas = apuestaRepo.findAll().stream()
                .filter(a -> a.getPartido().getId().equals(partido.getId()))
                .filter(a -> "PENDIENTE".equalsIgnoreCase(a.getEstado()))
                .toList();

        for (Apuesta apuesta : apuestas) {
            if (apuesta.getResultadoApostado().equalsIgnoreCase(ganadorResultado)) {
                apuesta.setEstado("GANADA");
                double gananciaBruta = apuesta.getMonto() * apuesta.getCuota();
                double gananciaNeta = gananciaBruta - apuesta.getMonto();
                apuesta.setGanancia(gananciaBruta);

                Usuario user = usuarioRepo.findById(apuesta.getUsuario().getId()).orElse(null);
                if (user != null) {
                    user.setSaldo(user.getSaldo() + gananciaNeta);
                    usuarioRepo.save(user);
                    if (usuarioLogueado != null && user.getId().equals(usuarioLogueado.getId())) {
                        usuarioLogueado.setSaldo(user.getSaldo());
                    }
                }
            } else {
                apuesta.setEstado("PERDIDA");
                apuesta.setGanancia(0.0);
                
                Usuario user = usuarioRepo.findById(apuesta.getUsuario().getId()).orElse(null);
                if (user != null) {
                    user.setSaldo(user.getSaldo() - apuesta.getMonto());
                    usuarioRepo.save(user);
                    if (usuarioLogueado != null && user.getId().equals(usuarioLogueado.getId())) {
                        usuarioLogueado.setSaldo(user.getSaldo());
                    }
                }
            }
            apuestaRepo.save(apuesta);
        }

        BitacoraSingleton.getInstancia().registrar("ADMIN: Partido " + partido.getEquipo1() + " vs " + partido.getEquipo2() + " definido como " + ganadorResultado + ". Apuestas procesadas.");
    }
}
