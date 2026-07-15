package pe.edu.utp.proyecto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.utp.proyecto.modelo.Usuario;
import pe.edu.utp.proyecto.modelo.Plataforma;
import pe.edu.utp.proyecto.modelo.Partido;
import pe.edu.utp.proyecto.repository.UsuarioR;
import pe.edu.utp.proyecto.repository.Plataformarepositorio;
import pe.edu.utp.proyecto.repository.PartidoR;

@Component
public class DataInicializador implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataInicializador.class);


    @Autowired
    private UsuarioR usuarioRepo;

    @Autowired
    private Plataformarepositorio plataformaRepo;

    @Autowired
    private PartidoR partidoRepo;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepo.count() == 0) {
            log.info(">>> CREANDO JUGADORES DE PRUEBA...");

            Usuario adminUser = new Usuario();
            adminUser.setNombre("Admin");
            adminUser.setApellido("GOLAZO");
            adminUser.setCodigo("admin");
            adminUser.setEmail("admin@golazo.com");
            adminUser.setContrasena("1 2 3");
            adminUser.setSaldo(5000.00);
            usuarioRepo.save(adminUser);
        }
        
        // BORRAR SIEMPRE PARA REINICIAR Y CARGAR EL NUEVO FIXTURE
        if (partidoRepo.count() < 32) {
            log.info(">>> FALTAN PARTIDOS EN BD. LIMPIANDO Y CREANDO PARTIDOS DEL MUNDIAL 2026...");
            partidoRepo.deleteAll();
            
            // ================= 16AVOS DE FINAL =================
            // Lado Izquierdo
            Partido p1 = new Partido("Alemania", "Paraguay", "2026-06-15 15:00", "Fútbol", 1.5, 3.5, 4.0, "Estadio Azteca");
            p1.setGanador("EQUIPO 2"); p1.setGolesEquipo1(1); p1.setGolesEquipo2(2); partidoRepo.save(p1);
            
            Partido p2 = new Partido("Francia", "Suecia", "2026-06-15 18:00", "Fútbol", 1.8, 3.2, 3.8, "Estadio Azteca");
            p2.setGanador("EQUIPO 1"); p2.setGolesEquipo1(2); p2.setGolesEquipo2(0); partidoRepo.save(p2);
            
            Partido p3 = new Partido("Sudáfrica", "Canadá", "2026-06-16 15:00", "Fútbol", 2.5, 3.0, 2.6, "MetLife Stadium");
            p3.setGanador("EQUIPO 2"); p3.setGolesEquipo1(0); p3.setGolesEquipo2(1); partidoRepo.save(p3);
            
            Partido p4 = new Partido("Países Bajos", "Marruecos", "2026-06-16 18:00", "Fútbol", 1.9, 3.1, 3.5, "MetLife Stadium");
            p4.setGanador("EQUIPO 2"); p4.setGolesEquipo1(1); p4.setGolesEquipo2(2); partidoRepo.save(p4);
            
            Partido p5 = new Partido("Portugal", "Croacia", "2026-06-17 15:00", "Fútbol", 2.0, 3.0, 3.2, "SoFi Stadium");
            p5.setGanador("EQUIPO 1"); p5.setGolesEquipo1(3); p5.setGolesEquipo2(1); partidoRepo.save(p5);
            
            Partido p6 = new Partido("España", "Austria", "2026-06-17 18:00", "Fútbol", 1.6, 3.5, 4.2, "SoFi Stadium");
            p6.setGanador("EQUIPO 1"); p6.setGolesEquipo1(2); p6.setGolesEquipo2(1); partidoRepo.save(p6);
            
            Partido p7 = new Partido("Estados Unidos", "Bosnia", "2026-06-18 15:00", "Fútbol", 2.1, 3.2, 3.1, "AT&T Stadium");
            p7.setGanador("EQUIPO 1"); p7.setGolesEquipo1(1); p7.setGolesEquipo2(0); partidoRepo.save(p7);
            
            Partido p8 = new Partido("Bélgica", "Senegal", "2026-06-18 18:00", "Fútbol", 1.7, 3.3, 4.0, "AT&T Stadium");
            p8.setGanador("EQUIPO 1"); p8.setGolesEquipo1(2); p8.setGolesEquipo2(0); partidoRepo.save(p8);

            // Lado Derecho
            Partido p9 = new Partido("Brasil", "Japón", "2026-06-19 15:00", "Fútbol", 1.4, 3.8, 5.5, "Hard Rock Stadium");
            p9.setGanador("EQUIPO 1"); p9.setGolesEquipo1(3); p9.setGolesEquipo2(1); partidoRepo.save(p9);
            
            Partido p10 = new Partido("Costa de Marfil", "Noruega", "2026-06-19 18:00", "Fútbol", 2.8, 3.0, 2.4, "Hard Rock Stadium");
            p10.setGanador("EQUIPO 2"); p10.setGolesEquipo1(0); p10.setGolesEquipo2(2); partidoRepo.save(p10);
            
            Partido p11 = new Partido("México", "Ecuador", "2026-06-20 15:00", "Fútbol", 2.2, 3.1, 2.9, "Mercedes-Benz Stadium");
            p11.setGanador("EQUIPO 1"); p11.setGolesEquipo1(1); p11.setGolesEquipo2(0); partidoRepo.save(p11);
            
            Partido p12 = new Partido("Inglaterra", "R.D. Congo", "2026-06-20 18:00", "Fútbol", 1.3, 4.0, 7.0, "Mercedes-Benz Stadium");
            p12.setGanador("EQUIPO 1"); p12.setGolesEquipo1(4); p12.setGolesEquipo2(0); partidoRepo.save(p12);
            
            Partido p13 = new Partido("Argentina", "Cabo Verde", "2026-06-21 15:00", "Fútbol", 1.2, 4.5, 9.0, "Gillette Stadium");
            p13.setGanador("EQUIPO 1"); p13.setGolesEquipo1(3); p13.setGolesEquipo2(0); partidoRepo.save(p13);
            
            Partido p14 = new Partido("Australia", "Egipto", "2026-06-21 18:00", "Fútbol", 2.6, 3.0, 2.5, "Gillette Stadium");
            p14.setGanador("EQUIPO 2"); p14.setGolesEquipo1(1); p14.setGolesEquipo2(2); partidoRepo.save(p14);
            
            Partido p15 = new Partido("Suiza", "Argelia", "2026-06-22 15:00", "Fútbol", 1.9, 3.2, 3.6, "NRG Stadium");
            p15.setGanador("EQUIPO 1"); p15.setGolesEquipo1(2); p15.setGolesEquipo2(1); partidoRepo.save(p15);
            
            Partido p16 = new Partido("Colombia", "Ghana", "2026-06-22 18:00", "Fútbol", 2.0, 3.1, 3.4, "NRG Stadium");
            p16.setGanador("EQUIPO 1"); p16.setGolesEquipo1(1); p16.setGolesEquipo2(0); partidoRepo.save(p16);

            // ================= OCTAVOS DE FINAL =================
            Partido o1 = new Partido("Canadá", "Marruecos", "2026-06-26 15:00", "Fútbol", 3.0, 3.2, 1.9, "NRG Stadium");
            o1.setGanador("EQUIPO 2"); o1.setGolesEquipo1(0); o1.setGolesEquipo2(3); partidoRepo.save(o1);

            Partido o2 = new Partido("Paraguay", "Francia", "2026-06-26 18:00", "Fútbol", 2.8, 3.0, 2.1, "Lincoln Financial Field");
            o2.setGanador("EQUIPO 2"); o2.setGolesEquipo1(0); o2.setGolesEquipo2(1); partidoRepo.save(o2);

            Partido o3 = new Partido("Brasil", "Noruega", "2026-06-27 15:00", "Fútbol", 2.6, 3.1, 2.3, "MetLife Stadium");
            o3.setGanador("EQUIPO 2"); o3.setGolesEquipo1(1); o3.setGolesEquipo2(2); partidoRepo.save(o3);

            Partido o4 = new Partido("México", "Inglaterra", "2026-06-27 18:00", "Fútbol", 3.5, 3.3, 1.8, "Estadio Banorte");
            o4.setGanador("EQUIPO 2"); o4.setGolesEquipo1(2); o4.setGolesEquipo2(3); partidoRepo.save(o4);

            Partido o5 = new Partido("Portugal", "España", "2026-06-28 15:00", "Fútbol", 1.6, 3.4, 4.2, "Estadio Dallas");
            o5.setGanador("EQUIPO 2"); o5.setGolesEquipo1(0); o5.setGolesEquipo2(1); partidoRepo.save(o5);

            Partido o6 = new Partido("Estados Unidos", "Bélgica", "2026-06-28 18:00", "Fútbol", 3.2, 3.1, 2.0, "Lumen Field");
            o6.setGanador("EQUIPO 2"); o6.setGolesEquipo1(1); o6.setGolesEquipo2(4); partidoRepo.save(o6);

            Partido o7 = new Partido("Argentina", "Egipto", "2026-06-29 15:00", "Fútbol", 1.5, 3.5, 4.5, "Estadio Atlanta");
            o7.setGanador("EQUIPO 1"); o7.setGolesEquipo1(3); o7.setGolesEquipo2(2); partidoRepo.save(o7);

            Partido o8 = new Partido("Suiza", "Colombia", "2026-06-29 18:00", "Fútbol", 2.4, 3.0, 2.5, "BC Place");
            o8.setGanador("EQUIPO 1"); o8.setGolesEquipo1(0); o8.setGolesEquipo2(0); partidoRepo.save(o8); // Penales 4-3 a favor de Suiza

            // ================= CUARTOS DE FINAL =================
            Partido c1 = new Partido("Francia", "Marruecos", "2026-07-03 15:00", "Fútbol", 2.0, 3.0, 2.0, "Estadio Boston");
            c1.setGanador("EQUIPO 1"); c1.setGolesEquipo1(2); c1.setGolesEquipo2(0); partidoRepo.save(c1);

            Partido c2 = new Partido("España", "Bélgica", "2026-07-03 18:00", "Fútbol", 2.0, 3.0, 2.0, "Estadio Los Ángeles");
            c2.setGanador("EQUIPO 1"); c2.setGolesEquipo1(2); c2.setGolesEquipo2(1); partidoRepo.save(c2);

            Partido c3 = new Partido("Noruega", "Inglaterra", "2026-07-04 15:00", "Fútbol", 2.0, 3.0, 2.0, "Estadio Miami");
            c3.setGanador("EQUIPO 2"); c3.setGolesEquipo1(1); c3.setGolesEquipo2(2); partidoRepo.save(c3);

            Partido c4 = new Partido("Argentina", "Suiza", "2026-07-04 18:00", "Fútbol", 2.0, 3.0, 2.0, "Estadio Kansas City");
            c4.setGanador("EQUIPO 1"); c4.setGolesEquipo1(3); c4.setGolesEquipo2(1); partidoRepo.save(c4);

            // ================= SEMIFINALES =================
            partidoRepo.save(new Partido("Francia", "España", "2026-07-14 14:00", "Fútbol", 2.0, 3.0, 2.0, "Estadio Dallas"));
            partidoRepo.save(new Partido("Inglaterra", "Argentina", "2026-07-15 14:00", "Fútbol", 2.0, 3.0, 2.0, "Estadio Atlanta"));

            // ================= TERCER PUESTO Y FINAL =================
            partidoRepo.save(new Partido("Por confirmar", "Por confirmar", "2026-07-18 16:00", "Fútbol", 2.0, 3.2, 2.8, "Estadio Miami"));
            partidoRepo.save(new Partido("Por confirmar", "Por confirmar", "2026-07-19 14:00", "Fútbol", 2.0, 3.0, 2.0, "MetLife Stadium"));
        }
        
        if (plataformaRepo.count() == 0) {
            log.info(">>> CREANDO ÁRBOL Plataforma...");
            Plataforma tickets = new Plataforma();
            tickets.setCodigo("1");
            tickets.setMensaje("Bienvenido a Mis Apuestas. Espere un momento.");
            plataformaRepo.save(tickets);
            
            Plataforma soporte = new Plataforma();
            soporte.setCodigo("2");
            soporte.setMensaje("Bienvenido a Soporte GOLAZO.");
            plataformaRepo.save(soporte);
            
            Plataforma saludo = new Plataforma();
            saludo.setCodigo("Inicio");
            saludo.setMensaje("Bienvenido a la Terminal GOLAZO. Seleccione una opción.");
            plataformaRepo.save(saludo);
        }
    }
}
