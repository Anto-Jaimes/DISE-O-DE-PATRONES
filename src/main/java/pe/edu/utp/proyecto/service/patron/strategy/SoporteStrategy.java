package pe.edu.utp.proyecto.service.patron.strategy;

public class SoporteStrategy implements RespuestaStrategy{
        @Override
        public String responder(String opcion) {
            return "Ãrea de Soporte TÃ©cnico. Reinicie su mÃ³dem y marque 1 si persiste.";
        }
    }
