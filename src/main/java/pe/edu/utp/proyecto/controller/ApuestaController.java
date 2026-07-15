package pe.edu.utp.proyecto.controller;

import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.proyecto.modelo.Apuesta;
import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.service.interfaces.PartidoServicio;
import pe.edu.utp.proyecto.service.interfaces.ApuestaServicio;
import pe.edu.utp.proyecto.service.interfaces.UsuarioServicio;
import pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/apuestas")
public class ApuestaController {

    private final ApuestaServicio apuestaServicio;
    private final UsuarioServicio usuarioServicio;
    private final PartidoServicio partidoServicio;
    private final pe.edu.utp.proyecto.service.patron.facade.ApuestaFacade apuestaFacade;

    public ApuestaController(ApuestaServicio ls, UsuarioServicio os, PartidoServicio cs, pe.edu.utp.proyecto.service.patron.facade.ApuestaFacade apuestaFacade) {
        this.apuestaServicio = ls;
        this.usuarioServicio = os;
        this.partidoServicio = cs;
        this.apuestaFacade = apuestaFacade;
    }

    @GetMapping
    public String listar(Model model) {
        return "redirect:/?tab=2";
    }

    @GetMapping("/nueva")
    public String nuevaApuesta(Model model, HttpSession session) {
        return "redirect:/?tab=2";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Apuesta apuesta, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            apuesta.setUsuario(usuarioLogueado);
        }

        if (apuesta.getId() == null) {
            // Se delega a la Fachada la orquestación del proceso completo
            apuestaFacade.registrarNuevaApuesta(apuesta, usuarioLogueado);
            if (usuarioLogueado != null) {
                // Actualizamos el usuario en sesión con el nuevo saldo
                session.setAttribute("usuarioLogueado", usuarioLogueado);
            }
        } else {
            apuestaServicio.guardar(apuesta);
        }
        return "redirect:/apuestas";
    }
    @PostMapping("/registro/guardar")
    public String guardarRegistro(@RequestParam(required = false) String id,
                                  @RequestParam String tipoDocumento,
                                  @RequestParam String dni,
                                  @RequestParam String nombre,
                                  @RequestParam String apellido,
                                  @RequestParam String fechaNacimiento,
                                  @RequestParam String genero,
                                  @RequestParam String email,
                                  @RequestParam String contrasena,
                                  @RequestParam String direccion,
                                  @RequestParam String departamento,
                                  @RequestParam String provincia,
                                  @RequestParam String distrito,
                                  @RequestParam String telefono,
                                  @RequestParam(required = false, defaultValue = "0.0") double montoAdicional,
                                  @RequestParam(required = false) String partidoId,
                                  @RequestParam(required = false) String resultadoApostado,
                                  @RequestParam(required = false) Double montoApuesta,
                                  @RequestParam(required = false) Integer propTarjetasRojas,
                                  @RequestParam(required = false) Integer propTarjetasAmarillas,
                                  @RequestParam(required = false) String propExpulsado,
                                  HttpSession session) {
        String redirectUrl = "redirect:/";
        if (id != null && !id.isEmpty()) {
            Usuario user = usuarioServicio.buscarPorId(id);
            if (user != null) {
                user.setTipoDocumento(tipoDocumento);
                user.setDni(dni);
                user.setNombre(nombre);
                user.setApellido(apellido);
                user.setFechaNacimiento(fechaNacimiento);
                user.setGenero(genero);
                user.setEmail(email);
                if (contrasena != null && !contrasena.isEmpty()) {
                    user.setContrasena(contrasena);
                }
                user.setDireccion(direccion);
                user.setDepartamento(departamento);
                user.setProvincia(provincia);
                user.setDistrito(distrito);
                user.setTelefono(telefono);
                if (montoAdicional > 0.0) {
                    user.setSaldo(user.getSaldo() + montoAdicional);
                }
                usuarioServicio.actualizar(user.getId(), user);
                
                Usuario loggedIn = (Usuario) session.getAttribute("usuarioLogueado");
                if (loggedIn == null || !"ADMIN".equals(loggedIn.getRol())) {
                    session.setAttribute("usuarioLogueado", user);
                }
                
                redirectUrl = "redirect:/?tab=1&success=perfil_actualizado";
            }
        } else {
            Usuario user = new Usuario();
            user.setTipoDocumento(tipoDocumento);
            user.setDni(dni);
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setFechaNacimiento(fechaNacimiento);
            user.setGenero(genero);
            user.setEmail(email);
            user.setContrasena(contrasena);
            user.setDireccion(direccion);
            user.setDepartamento(departamento);
            user.setProvincia(provincia);
            user.setDistrito(distrito);
            user.setTelefono(telefono);
            
            // 10 soles base + additional amount
            user.setSaldo(10.0 + montoAdicional);
            
            // Short code: [Day][Sequence]
            int day = java.time.LocalDateTime.now().getDayOfMonth();
            long nextSeq = usuarioServicio.listar().size() + 1;
            String shortCode = String.valueOf(day) + nextSeq;
            user.setCodigo(shortCode);
            
            user = usuarioServicio.guardar(user);
            
            Usuario loggedIn = (Usuario) session.getAttribute("usuarioLogueado");
            if (loggedIn == null || !"ADMIN".equals(loggedIn.getRol())) {
                session.setAttribute("usuarioLogueado", user);
            }
            
            BitacoraSingleton.getInstancia().registrar("Nuevo usuario registrado: " + user.getNombre() + " " + user.getApellido() + " (Código: " + user.getCodigo() + ")");
            redirectUrl = "redirect:/?tab=2&success=usuario_registrado";
        }
        
        // Process rapid bet
        if (partidoId != null && !partidoId.isEmpty() && resultadoApostado != null && !resultadoApostado.isEmpty() && montoApuesta != null && montoApuesta > 0) {
            Usuario userForBet = (Usuario) session.getAttribute("usuarioLogueado");
            if (userForBet != null && userForBet.getSaldo() >= montoApuesta) {
                Partido partido = partidoServicio.buscarPorId(partidoId);
                if (partido != null) {
                    double cuota = 2.0;
                    if ("EQUIPO 1".equalsIgnoreCase(resultadoApostado)) cuota = partido.getCuotaEquipo1();
                    else if ("EMPATE".equalsIgnoreCase(resultadoApostado)) cuota = partido.getCuotaEmpate();
                    else if ("EQUIPO 2".equalsIgnoreCase(resultadoApostado)) cuota = partido.getCuotaEquipo2();

                    Apuesta apuesta = new Apuesta();
                    apuesta.setFecha(LocalDateTime.now());
                    apuesta.setMonto(montoApuesta);
                    apuesta.setResultadoApostado(resultadoApostado.toUpperCase());
                    apuesta.setUsuario(userForBet);
                    apuesta.setPartido(partido);
                    apuesta.setApostadorNombreCompleto(userForBet.getNombre() + " " + userForBet.getApellido());
                    apuesta.setApostadorDni(userForBet.getDni());
                    apuesta.setEquipo1(partido.getEquipo1());
                    apuesta.setEquipo2(partido.getEquipo2());
                    apuesta.setCuota(cuota);
                    apuesta.setGanancia(montoApuesta * cuota);
                    apuesta.setEstado("PENDIENTE");
                    apuesta.setTipo("WEB");
                    apuesta.setPropTarjetasRojas(propTarjetasRojas);
                    apuesta.setPropTarjetasAmarillas(propTarjetasAmarillas);
                    apuesta.setPropExpulsado(propExpulsado);
                    int dayBet = LocalDateTime.now().getDayOfMonth();
                    int sequenceBet = apuestaServicio.listar().size() + 1;
                    apuesta.setCodigoTicket(dayBet + "-" + String.format("%03d", sequenceBet));
                    apuestaServicio.iniciarApuesta(apuesta);
                    
                    // No se descuenta por adelantado
                    usuarioServicio.actualizar(userForBet.getId(), userForBet);
                    session.setAttribute("usuarioLogueado", userForBet);
                    
                    BitacoraSingleton.getInstancia().registrar("Apuesta rapida registrada para: " + userForBet.getNombre());
                    redirectUrl = "redirect:/?tab=2&success=apuesta_registrada";
                }
            }
        }
        
        return redirectUrl;
    }

    @PostMapping("/registrar")
    public String registrarApuesta(@RequestParam String partidoId,
                                   @RequestParam String resultadoApostado,
                                   @RequestParam double monto,
                                   @RequestParam(required = false) Integer propTarjetasRojas,
                                   @RequestParam(required = false) Integer propTarjetasAmarillas,
                                   @RequestParam(required = false) String propExpulsado,
                                   @RequestParam(required = false) String apostadorId,
                                   HttpSession session,
                                   Model model) {
        Usuario loggedIn = (Usuario) session.getAttribute("usuarioLogueado");
        if (loggedIn == null) {
            return "redirect:/?tab=1&error=debe_registrarse";
        }

        Usuario user = loggedIn;
        // Si el admin envía un ID específico de apostador, usar ese usuario
        if ("ADMIN".equals(loggedIn.getRol()) && apostadorId != null && !apostadorId.isEmpty()) {
            user = usuarioServicio.buscarPorId(apostadorId);
            if (user == null) {
                return "redirect:/?tab=2&error=usuario_no_encontrado";
            }
        } else if ("ADMIN".equals(loggedIn.getRol())) {
            // El admin no puede apostar para sí mismo
            return "redirect:/?tab=3&error=debe_seleccionar_cliente";
        } else {
            // Cliente normal
            user = usuarioServicio.buscarPorId(user.getId());
        }

        if (user.getSaldo() < monto) {
            return "redirect:/?tab=2&error=saldo_insuficiente";
        }

        Partido partido = partidoServicio.buscarPorId(partidoId);
        if (partido == null) {
            return "redirect:/?tab=2&error=partido_no_encontrado";
        }
        double cuota = 2.0;
        if ("EQUIPO 1".equalsIgnoreCase(resultadoApostado)) {
            cuota = partido.getCuotaEquipo1();
        } else if ("EMPATE".equalsIgnoreCase(resultadoApostado)) {
            cuota = partido.getCuotaEmpate();
        } else if ("EQUIPO 2".equalsIgnoreCase(resultadoApostado)) {
            cuota = partido.getCuotaEquipo2();
        }
        Apuesta apuesta = new Apuesta();
        apuesta.setFecha(LocalDateTime.now());
        apuesta.setMonto(monto);
        apuesta.setResultadoApostado(resultadoApostado.toUpperCase());
        apuesta.setUsuario(user);
        apuesta.setPartido(partido);
        apuesta.setApostadorNombreCompleto(user.getNombre() + " " + user.getApellido());
        apuesta.setApostadorDni(user.getDni());
        apuesta.setEquipo1(partido.getEquipo1());
        apuesta.setEquipo2(partido.getEquipo2());
        apuesta.setCuota(cuota);
        apuesta.setGanancia(monto * cuota);
        apuesta.setEstado("PENDIENTE");
        apuesta.setTipo("WEB");
        apuesta.setPropTarjetasRojas(propTarjetasRojas);
        apuesta.setPropTarjetasAmarillas(propTarjetasAmarillas);
        apuesta.setPropExpulsado(propExpulsado);
        int day = LocalDateTime.now().getDayOfMonth();
        int sequence = apuestaServicio.listar().size() + 1;
        apuesta.setCodigoTicket(day + "-" + String.format("%03d", sequence));

        apuestaServicio.iniciarApuesta(apuesta);
        // No se descuenta por adelantado
        usuarioServicio.actualizar(user.getId(), user);
        
        if (!"ADMIN".equals(loggedIn.getRol())) {
            session.setAttribute("usuarioLogueado", user);
        }

        BitacoraSingleton.getInstancia().registrar("Apuesta registrada a nombre de " + user.getNombre() + " (Ticket: " + apuesta.getCodigoTicket() + "): $" + monto + " en " + partido.getEquipo1() + " vs " + partido.getEquipo2());

        return "redirect:/?tab=4&success=apuesta_registrada";
    }
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
                    
                    // Actualizar el nombre completo en la apuesta también
                    apuesta.setApostadorNombreCompleto(user.getNombre() + " " + user.getApellido());
                    apuesta.setApostadorDni(user.getDni());
                }
            }
            
            // Recalcular ganancia si cambia el pronstico
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

    @PostMapping("/resolver")
    public String resolverApuesta(@RequestParam String apuestaId,
                                  @RequestParam String resultadoReal,
                                  HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        Apuesta apuesta = apuestaServicio.buscarPorId(apuestaId);
        if (apuesta == null || !"PENDIENTE".equalsIgnoreCase(apuesta.getEstado())) {
            return "redirect:/?error=apuesta_invalida";
        }
        Partido partido = apuesta.getPartido();
        partido.setGanador(resultadoReal);
        partidoServicio.actualizar(partido.getId(), partido);
        if (apuesta.getResultadoApostado().equalsIgnoreCase(resultadoReal)) {
            apuesta.setEstado("GANADA");
            double montoGanado = apuesta.getMonto() * apuesta.getCuota();
            apuesta.setGanancia(montoGanado);
            Usuario user = usuarioServicio.buscarPorId(apuesta.getUsuario().getId());
            double nuevoSaldo = user.getSaldo() + montoGanado;
            // No se descuenta por adelantado
            usuarioServicio.actualizar(user.getId(), user);
            if (user.getId().equals(usuarioLogueado.getId())) {
                session.setAttribute("usuarioLogueado", user);
            }

            BitacoraSingleton.getInstancia().registrar("Apuesta ganada! Acreditado $" + montoGanado + " a " + user.getNombre());
        } else {
            apuesta.setEstado("PERDIDA");
            apuesta.setGanancia(0.0);
            BitacoraSingleton.getInstancia().registrar("Apuesta perdida para el usuario " + apuesta.getUsuario().getNombre());
        }

        apuestaServicio.guardar(apuesta);

        return "redirect:/?success=apuesta_resuelta";
    }

    @GetMapping("/contestar/{id}")
    public String contestar(@PathVariable String id) {
        apuestaServicio.marcarEnProgreso(id);
        return "redirect:/apuestas";
    }

    @GetMapping("/finalizar/{id}")
    public String finalizar(@PathVariable String id) {
        apuestaServicio.marcarFinalizada(id);
        return "redirect:/apuestas";
    }
    @GetMapping("/eliminar/{id}")
    public String eliminarApuesta(@PathVariable String id, HttpSession session) {
        Apuesta apuesta = apuestaServicio.buscarPorId(id);
        if (apuesta != null) {
            // Reembolsar solo si esta PENDIENTE
            if ("PENDIENTE".equalsIgnoreCase(apuesta.getEstado())) {
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
            apuestaServicio.eliminar(id);
            pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton.getInstancia().registrar("Apuesta eliminada: " + apuesta.getCodigoTicket());
        }
        return "redirect:/?tab=2&success=apuesta_eliminada";
    }

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
