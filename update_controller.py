import re

with open("src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java", "r", encoding="utf-8") as f:
    content = f.read()

new_endpoint = """
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
}"""

content = content.replace("    }\n}", "    }\n" + new_endpoint)

with open("src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java", "w", encoding="utf-8") as f:
    f.write(content)

print("Added eliminar-liq endpoint!")
