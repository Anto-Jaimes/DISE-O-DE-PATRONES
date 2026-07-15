package pe.edu.utp.proyecto.controller;

import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.modelo.Apuesta;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.modelo.Usuario;
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
        
    public PartidoController(PartidoServicio servicio) {
        this.servicio = servicio;
                    }



    @GetMapping("/api/{id}")
    @ResponseBody
    public java.util.Map<String, Object> getPartidoApi(@PathVariable String id) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        if (id == null || id.equals("null")) {
            map.put("error", "ID de partido inválido");
            return map;
        }
        
        Partido p = servicio.buscarPorId(id);
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
        } else {
            map.put("error", "Partido no encontrado");
        }
        return map;
    }

    @PostMapping("/resolver")
    public String resolverPartido(
            @RequestParam String partidoId,
            @RequestParam String equipo1,
            @RequestParam String equipo2,
            @RequestParam Double cuotaEquipo1,
            @RequestParam(required = false, defaultValue = "0.0") Double cuotaEmpate,
            @RequestParam Double cuotaEquipo2,
            @RequestParam(required = false) Integer golesEquipo1,
            @RequestParam(required = false) Integer golesEquipo2,
            @RequestParam(required = false) String ganador,
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String estadio,
            HttpSession session) {
        Partido partido = servicio.buscarPorId(partidoId);
        if (partido == null) {
            return "redirect:/?tab=3&error=partido_no_encontrado";
        }

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        partido.setEquipo1(equipo1);
        partido.setEquipo2(equipo2);
        partido.setCuotaEquipo1(cuotaEquipo1);
        partido.setCuotaEmpate(cuotaEmpate);
        partido.setCuotaEquipo2(cuotaEquipo2);
        partido.setGolesEquipo1(golesEquipo1);
        partido.setGolesEquipo2(golesEquipo2);
        if (fecha != null && !fecha.isEmpty()) partido.setFecha(fecha);
        if (estadio != null && !estadio.isEmpty()) partido.setEstadio(estadio);

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
            usuarioLogueado = servicio.resolverYProcesarPartido(partido, ganadorDeterminado, usuarioLogueado);
        } else {
            servicio.actualizar(partido.getId(), partido);
        }

        if (usuarioLogueado != null) {
            session.setAttribute("usuarioLogueado", usuarioLogueado);
        }

        return "redirect:/?tab=3&success=partido_resuelto";
    }


}
