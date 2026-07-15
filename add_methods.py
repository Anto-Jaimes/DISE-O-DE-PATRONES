with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'r', encoding='utf-8') as f:
    content = f.read()

methods = '''
    @PostMapping("/editar")
    public String editarApuesta(@RequestParam String id,
                                @RequestParam String resultadoApostado,
                                @RequestParam double monto,
                                @RequestParam(required = false) Integer propTarjetasRojas,
                                @RequestParam(required = false) Integer propTarjetasAmarillas,
                                @RequestParam(required = false) String propExpulsado,
                                @RequestParam(required = false) String userTipoDocumento,
                                @RequestParam(required = false) String userDni,
                                @RequestParam(required = false) String userNombre,
                                @RequestParam(required = false) String userApellido,
                                @RequestParam(required = false) String userContrasena) {
        Apuesta apuesta = apuestaServicio.buscarPorId(id);
        if (apuesta != null) {
            apuesta.setResultadoApostado(resultadoApostado);
            apuesta.setMonto(monto);
            apuesta.setPropTarjetasRojas(propTarjetasRojas);
            apuesta.setPropTarjetasAmarillas(propTarjetasAmarillas);
            apuesta.setPropExpulsado(propExpulsado);
            
            if (apuesta.getUsuario() != null) {
                Usuario user = usuarioServicio.buscarPorId(apuesta.getUsuario().getId());
                if (user != null) {
                    if (userTipoDocumento != null) user.setTipoDocumento(userTipoDocumento);
                    if (userDni != null) user.setDni(userDni);
                    if (userNombre != null) user.setNombre(userNombre);
                    if (userApellido != null) user.setApellido(userApellido);
                    if (userContrasena != null) user.setContrasena(userContrasena);
                    usuarioServicio.actualizar(user.getId(), user);
                    
                    // Actualizar el nombre completo en TODAS las apuestas del usuario
                    java.util.List<pe.edu.utp.proyecto.modelo.Apuesta> userBets = apuestaServicio.listarPorUsuario(user.getId());
                    if (userBets != null) {
                        for (pe.edu.utp.proyecto.modelo.Apuesta b : userBets) {
                            b.setApostadorNombreCompleto(user.getNombre() + " " + (user.getApellido() != null ? user.getApellido() : ""));
                            b.setApostadorApellido(user.getApellido());
                            b.setApostadorDni(user.getDni());
                            b.setApostadorContrasena(user.getContrasena());
                            if (!b.getId().equals(apuesta.getId())) {
                                apuestaServicio.guardar(b);
                            } else {
                                apuesta.setApostadorNombreCompleto(b.getApostadorNombreCompleto());
                                apuesta.setApostadorApellido(b.getApostadorApellido());
                                apuesta.setApostadorDni(b.getApostadorDni());
                                apuesta.setApostadorContrasena(b.getApostadorContrasena());
                            }
                        }
                    }
                }
            } else {
                if (userDni != null && !userDni.trim().isEmpty()) apuesta.setApostadorDni(userDni);
                if (userNombre != null) apuesta.setApostadorNombreCompleto(userNombre);
                if (userApellido != null) apuesta.setApostadorApellido(userApellido);
                if (userContrasena != null) apuesta.setApostadorContrasena(userContrasena);
            }
            
            double cuota = 2.0;
            Partido p = apuesta.getPartido();
            if (p != null) {
                if ("EQUIPO 1".equalsIgnoreCase(resultadoApostado)) cuota = p.getCuotaEquipo1();
                else if ("EMPATE".equalsIgnoreCase(resultadoApostado)) cuota = p.getCuotaEmpate();
                else if ("EQUIPO 2".equalsIgnoreCase(resultadoApostado)) cuota = p.getCuotaEquipo2();
            }
            apuesta.setCuota(cuota);
            apuesta.setGanancia(monto * cuota);
            
            apuestaServicio.guardar(apuesta);
            pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton.getInstancia().registrar("Apuesta editada: " + apuesta.getCodigoTicket());
        }
        return "redirect:/?tab=2&success=apuesta_editada";
    }

    @GetMapping("/eliminar-liq/{id}")
    public String eliminarApuestaLiq(@PathVariable String id, HttpSession session) {
        Apuesta apuesta = apuestaServicio.buscarPorId(id);
        if (apuesta != null) {
            if ("PENDIENTE".equalsIgnoreCase(apuesta.getEstado()) || "EN_PROGRESO".equalsIgnoreCase(apuesta.getEstado())) {
                if (apuesta.getUsuario() != null) {
                    Usuario user = usuarioServicio.buscarPorId(apuesta.getUsuario().getId());
                    if (user != null) {
                        user.setSaldo(user.getSaldo() + apuesta.getMonto());
                        usuarioServicio.actualizar(user.getId(), user);
                        Usuario sessionUser = (Usuario) session.getAttribute("usuarioLogueado");
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
        Apuesta apuesta = apuestaServicio.buscarPorId(id);
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
        Apuesta apuesta = apuestaServicio.buscarPorId(id);
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
content = content.rstrip()
while content.endswith('}'):
    content = content[:-1].rstrip()
content += '\n' + methods

import re
content = re.sub(r'import\s+lombok\.extern\.slf4j\.Slf4j;\n?', '', content)
content = re.sub(r'@Slf4j\n?', '', content)

with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'w', encoding='utf-8') as f:
    f.write(content)
