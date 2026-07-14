package pe.edu.utp.proyecto.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("flagUtils")
public class FlagUtils {

    private static final Map<String, String> FLAG_MAP = new HashMap<>();

    static {
        FLAG_MAP.put("Alemania", "de");
        FLAG_MAP.put("Paraguay", "py");
        FLAG_MAP.put("Francia", "fr");
        FLAG_MAP.put("Suecia", "se");
        FLAG_MAP.put("Sudáfrica", "za");
        FLAG_MAP.put("Canadá", "ca");
        FLAG_MAP.put("Países Bajos", "nl");
        FLAG_MAP.put("Marruecos", "ma");
        FLAG_MAP.put("Portugal", "pt");
        FLAG_MAP.put("Croacia", "hr");
        FLAG_MAP.put("España", "es");
        FLAG_MAP.put("Austria", "at");
        FLAG_MAP.put("Estados Unidos", "us");
        FLAG_MAP.put("Bosnia", "ba");
        FLAG_MAP.put("Bélgica", "be");
        FLAG_MAP.put("Senegal", "sn");

        FLAG_MAP.put("Brasil", "br");
        FLAG_MAP.put("Japón", "jp");
        FLAG_MAP.put("Costa de Marfil", "ci");
        FLAG_MAP.put("Noruega", "no");
        FLAG_MAP.put("México", "mx");
        FLAG_MAP.put("Ecuador", "ec");
        FLAG_MAP.put("Inglaterra", "gb-eng");
        FLAG_MAP.put("R.D. Congo", "cd");
        FLAG_MAP.put("Argentina", "ar");
        FLAG_MAP.put("Cabo Verde", "cv");
        FLAG_MAP.put("Australia", "au");
        FLAG_MAP.put("Egipto", "eg");
        FLAG_MAP.put("Suiza", "ch");
        FLAG_MAP.put("Argelia", "dz");
        FLAG_MAP.put("Colombia", "co");
        FLAG_MAP.put("Ghana", "gh");
    }

    public String getCode(String equipo) {
        if (equipo == null) return "un";
        String code = FLAG_MAP.get(equipo);
        return code != null ? code : "un"; // 'un' could map to UN flag or unknown
    }
}
