package pe.edu.utp.proyecto.service.impl;
import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.repository.PartidoR;
import pe.edu.utp.proyecto.service.interfaces.PartidoServicio;
import org.springframework.stereotype.Service;
import pe.edu.utp.proyecto.modelo.Apuesta;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.repository.ApuestaR;
import pe.edu.utp.proyecto.repository.UsuarioR;
import pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton;
import java.util.List;
@Service
public class PartidoSI implements PartidoServicio {

    private static final int OCTAVOS_INICIO = 16;
    private static final int OCTAVOS_FIN = 23;
    private static final int CUARTOS_INICIO = 24;
    private static final int CUARTOS_FIN = 27;
    private static final int SEMIS_INICIO = 28;
    private static final int SEMIS_FIN = 29;
    private static final int TERCER_PUESTO_IDX = 30;
    private static final int FINAL_IDX = 31;

    private final PartidoR repo;
    private final ApuestaR apuestaRepo;
    private final UsuarioR usuarioRepo;

    public PartidoSI(PartidoR repo, ApuestaR apuestaRepo, UsuarioR usuarioRepo) {
        this.repo = repo;
        this.apuestaRepo = apuestaRepo;
        this.usuarioRepo = usuarioRepo;
    }
    public List<Partido> listar() {
        return repo.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id"));
    }
    @Override
    public Partido guardar(Partido c){
        return repo.save(c);
    }
    @Override
    public Partido buscarPorId(String id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public Partido actualizar(String id, Partido c){

        Partido existente = repo.findById(id).orElse(null);
        if (existente == null) return null;
        existente.setEquipo1(c.getEquipo1());
        existente.setEquipo2(c.getEquipo2());
        existente.setFecha(c.getFecha());
        existente.setDeporte(c.getDeporte());
        existente.setGanador(c.getGanador());
        return repo.save(existente);
    }
    @Override
    public void eliminar(String id) {
        repo.deleteById(id);
    }

    @Override
    public Usuario resolverYProcesarPartido(Partido partido, String ganadorDeterminado, Usuario usuarioLogueado) {
        if (ganadorDeterminado != null && !ganadorDeterminado.trim().isEmpty() && !ganadorDeterminado.equalsIgnoreCase("Escoger Ganador")) {
            if (ganadorDeterminado.equalsIgnoreCase("EQUIPO 1") || ganadorDeterminado.equalsIgnoreCase("EQUIPO 2") || ganadorDeterminado.equalsIgnoreCase("EMPATE")) {
                usuarioLogueado = resolverYProcesarApuestas(partido, ganadorDeterminado, usuarioLogueado);
                avanzarGanadorAlSiguientePartido(partido, ganadorDeterminado);
            } else {
                usuarioLogueado = resolveBackwards(ganadorDeterminado, partido, usuarioLogueado);
            }
        } else {
            partido.setGanador("");
            actualizar(partido.getId(), partido);
            revertirAvanceAlSiguientePartido(partido);
        }
        return usuarioLogueado;
    }

    private Usuario resolveBackwards(String selectedTeam, Partido partido, Usuario usuarioLogueado) {
        if (selectedTeam == null || selectedTeam.isEmpty() || selectedTeam.equalsIgnoreCase("EMPATE")) {
            return usuarioLogueado;
        }
        if (isTeamOrCandidate(partido.getEquipo1(), selectedTeam)) {
            usuarioLogueado = resolverYProcesarApuestas(partido, "EQUIPO 1", usuarioLogueado);
            avanzarGanadorAlSiguientePartido(partido, "EQUIPO 1");
        } else if (isTeamOrCandidate(partido.getEquipo2(), selectedTeam)) {
            usuarioLogueado = resolverYProcesarApuestas(partido, "EQUIPO 2", usuarioLogueado);
            avanzarGanadorAlSiguientePartido(partido, "EQUIPO 2");
        }
        return usuarioLogueado;
    }

    private boolean isTeamOrCandidate(String slotText, String team) {
        if (slotText == null || team == null) return false;
        return slotText.toLowerCase().contains(team.toLowerCase());
    }

    private Usuario resolverYProcesarApuestas(Partido partido, String ganadorResultado, Usuario usuarioLogueado) {
        partido.setGanador(ganadorResultado);
        actualizar(partido.getId(), partido);
        
        List<Apuesta> apuestas = apuestaRepo.findAll().stream()
                .filter(a -> a.getPartido() != null && a.getPartido().getId().equals(partido.getId()))
                .filter(a -> "PENDIENTE".equalsIgnoreCase(a.getEstado()) || "EN PROCESO".equalsIgnoreCase(a.getEstado()))
                .toList();

        for (Apuesta apuesta : apuestas) {
            if (apuesta.getResultadoApostado().equalsIgnoreCase(ganadorResultado)) {
                apuesta.setEstado("GANADA");
                double gananciaBruta = apuesta.getMonto() * apuesta.getCuota();
                double gananciaNeta = gananciaBruta - apuesta.getMonto();
                apuesta.setGanancia(gananciaBruta);

                if (apuesta.getUsuario() != null) {
                    Usuario user = usuarioRepo.findById(apuesta.getUsuario().getId()).orElse(null);
                    if (user != null) {
                        user.setSaldo(user.getSaldo() + gananciaNeta);
                        usuarioRepo.save(user);
                        if (usuarioLogueado != null && user.getId().equals(usuarioLogueado.getId())) {
                            usuarioLogueado.setSaldo(user.getSaldo());
                        }
                    }
                }
            } else {
                apuesta.setEstado("PERDIDA");
                apuesta.setGanancia(0.0);
                
                if (apuesta.getUsuario() != null) {
                    Usuario user = usuarioRepo.findById(apuesta.getUsuario().getId()).orElse(null);
                    if (user != null) {
                        user.setSaldo(user.getSaldo() - apuesta.getMonto());
                        usuarioRepo.save(user);
                        if (usuarioLogueado != null && user.getId().equals(usuarioLogueado.getId())) {
                            usuarioLogueado.setSaldo(user.getSaldo());
                        }
                    }
                }
            }
            apuestaRepo.save(apuesta);
        }

        BitacoraSingleton.getInstancia().registrar("ADMIN: Partido " + partido.getEquipo1() + " vs " + partido.getEquipo2() + " definido como " + ganadorResultado + ". Apuestas procesadas.");
        return usuarioLogueado;
    }

    private void revertirAvanceAlSiguientePartido(Partido partidoActual) {
        List<Partido> todos = listar();
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
        
        if (indexActual >= 0 && indexActual < OCTAVOS_INICIO) { 
            nextIndex = OCTAVOS_INICIO + (indexActual / 2);
            esEquipo1 = (indexActual % 2 == 0);
        } else if (indexActual >= OCTAVOS_INICIO && indexActual <= OCTAVOS_FIN) { 
            nextIndex = CUARTOS_INICIO + ((indexActual - OCTAVOS_INICIO) / 2);
            esEquipo1 = ((indexActual - OCTAVOS_INICIO) % 2 == 0);
        } else if (indexActual >= CUARTOS_INICIO && indexActual <= CUARTOS_FIN) { 
            nextIndex = SEMIS_INICIO + ((indexActual - CUARTOS_INICIO) / 2);
            esEquipo1 = ((indexActual - CUARTOS_INICIO) % 2 == 0);
        } else if (indexActual >= SEMIS_INICIO && indexActual <= SEMIS_FIN) { 
            esSemifinal = true;
            nextIndex = FINAL_IDX; 
            esEquipo1 = ((indexActual - SEMIS_INICIO) % 2 == 0);
        }

        if (nextIndex != -1 && nextIndex < todos.size()) {
            Partido nextPartido = todos.get(nextIndex);
            if (esEquipo1) {
                nextPartido.setEquipo1("Por confirmar");
            } else {
                nextPartido.setEquipo2("Por confirmar");
            }
            actualizar(nextPartido.getId(), nextPartido);

            if (esSemifinal && todos.size() > TERCER_PUESTO_IDX) {
                Partido tercerPuesto = todos.get(TERCER_PUESTO_IDX);
                if (esEquipo1) {
                    tercerPuesto.setEquipo1("Por confirmar");
                } else {
                    tercerPuesto.setEquipo2("Por confirmar");
                }
                actualizar(tercerPuesto.getId(), tercerPuesto);
            }
        }
    }

    private void avanzarGanadorAlSiguientePartido(Partido partidoActual, String ganadorSeleccionado) {
        String equipoGanador = ganadorSeleccionado.equalsIgnoreCase("EQUIPO 1") ? partidoActual.getEquipo1() : 
                               (ganadorSeleccionado.equalsIgnoreCase("EQUIPO 2") ? partidoActual.getEquipo2() : null);
        
        if (equipoGanador == null) return; 

        List<Partido> todos = listar();
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
        
        if (indexActual >= 0 && indexActual < OCTAVOS_INICIO) { 
            nextIndex = OCTAVOS_INICIO + (indexActual / 2);
            esEquipo1 = (indexActual % 2 == 0);
        } else if (indexActual >= OCTAVOS_INICIO && indexActual <= OCTAVOS_FIN) { 
            nextIndex = CUARTOS_INICIO + ((indexActual - OCTAVOS_INICIO) / 2);
            esEquipo1 = ((indexActual - OCTAVOS_INICIO) % 2 == 0);
        } else if (indexActual >= CUARTOS_INICIO && indexActual <= CUARTOS_FIN) { 
            nextIndex = SEMIS_INICIO + ((indexActual - CUARTOS_INICIO) / 2);
            esEquipo1 = ((indexActual - CUARTOS_INICIO) % 2 == 0);
        } else if (indexActual >= SEMIS_INICIO && indexActual <= SEMIS_FIN) { 
            esSemifinal = true;
            nextIndex = FINAL_IDX; 
            esEquipo1 = ((indexActual - SEMIS_INICIO) % 2 == 0);
        }

        if (nextIndex != -1 && nextIndex < todos.size()) {
            Partido nextPartido = todos.get(nextIndex);
            if (esEquipo1) {
                nextPartido.setEquipo1(equipoGanador);
            } else {
                nextPartido.setEquipo2(equipoGanador);
            }
            actualizar(nextPartido.getId(), nextPartido);

            if (esSemifinal && todos.size() > TERCER_PUESTO_IDX) {
                String equipoPerdedor = ganadorSeleccionado.equalsIgnoreCase("EQUIPO 1") ? partidoActual.getEquipo2() : partidoActual.getEquipo1();
                Partido tercerPuesto = todos.get(TERCER_PUESTO_IDX);
                if (esEquipo1) {
                    tercerPuesto.setEquipo1(equipoPerdedor);
                } else {
                    tercerPuesto.setEquipo2(equipoPerdedor);
                }
                actualizar(tercerPuesto.getId(), tercerPuesto);
            }
        }
    }
}
