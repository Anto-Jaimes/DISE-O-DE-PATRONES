import re

with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Remove Lombok
content = re.sub(r'import\s+lombok\.extern\.slf4j\.Slf4j;\n?', '', content)
content = re.sub(r'@Slf4j\n?', '', content)

# Extract everything except the last closing brace
# We'll just rfind the last '}' and slice it.
last_brace_idx = content.rfind('}')
if last_brace_idx != -1:
    content = content[:last_brace_idx]

methods = '''
    @GetMapping("/eliminar-liq/{id}")
    public String eliminarApuestaLiq(@PathVariable String id, jakarta.servlet.http.HttpSession session) {
        pe.edu.utp.proyecto.modelo.Apuesta apuesta = apuestaServicio.buscarPorId(id);
        if (apuesta != null) {
            if ("PENDIENTE".equalsIgnoreCase(apuesta.getEstado()) || "EN_PROGRESO".equalsIgnoreCase(apuesta.getEstado())) {
                if (apuesta.getUsuario() != null) {
                    pe.edu.utp.proyecto.modelo.Usuario user = usuarioServicio.buscarPorId(apuesta.getUsuario().getId());
                    if (user != null) {
                        user.setSaldo(user.getSaldo() + apuesta.getMonto());
                        usuarioServicio.actualizar(user.getId(), user);
                        pe.edu.utp.proyecto.modelo.Usuario sessionUser = (pe.edu.utp.proyecto.modelo.Usuario) session.getAttribute("usuarioLogueado");
                        if (sessionUser != null && sessionUser.getId().equals(user.getId())) {
                            session.setAttribute("usuarioLogueado", user);
                        }
                    }
                }
            }
            apuestaServicio.eliminar(id);
            pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton.getInstancia().registrar("Apuesta eliminada desde liquidacion: " + apuesta.getCodigoTicket());
        }
        return "redirect:/?tab=4&success=apuesta_eliminada";
    }

    @PostMapping("/editar-liquidacion")
    public String editarLiquidacion(@RequestParam String id,
                                    @RequestParam String estado,
                                    @RequestParam double ganancia) {
        pe.edu.utp.proyecto.modelo.Apuesta apuesta = apuestaServicio.buscarPorId(id);
        if (apuesta != null) {
            apuesta.setEstado(estado);
            apuesta.setGanancia(ganancia);
            apuestaServicio.guardar(apuesta);
            pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton.getInstancia().registrar("Liquidacion editada: " + apuesta.getCodigoTicket() + " -> " + estado);
        }
        return "redirect:/?tab=4&success=liquidacion_guardada";
    }

    @GetMapping("/boleta/{id}")
    public String verBoleta(@PathVariable String id, org.springframework.ui.Model model) {
        pe.edu.utp.proyecto.modelo.Apuesta apuesta = apuestaServicio.buscarPorId(id);
        if (apuesta == null) {
            return "redirect:/?tab=4&error=apuesta_no_encontrada";
        }
        
        double gananciaNeta = apuesta.getGanancia();
        double gananciaBruta = gananciaNeta / 0.82;
        if (gananciaNeta == 0) {
            gananciaBruta = apuesta.getMonto() * apuesta.getCuota();
        }
        double igv = gananciaBruta * 0.18;
        
        model.addAttribute("apuesta", apuesta);
        model.addAttribute("gananciaBruta", gananciaBruta);
        model.addAttribute("igv", igv);
        model.addAttribute("gananciaNeta", gananciaNeta > 0 ? gananciaNeta : (gananciaBruta - igv));
        
        return "boleta";
    }
}
'''
content += methods

with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'w', encoding='utf-8') as f:
    f.write(content)
