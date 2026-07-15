import os
import re

def fix_data_inicializador():
    path = r"src\main\java\pe\edu\utp\proyecto\config\DataInicializador.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    content = re.sub(r'@Autowired\s+private UsuarioR usuarioRepo;', r'private final UsuarioR usuarioRepo;', content)
    content = re.sub(r'@Autowired\s+private Plataformarepositorio plataformaRepo;', r'private final Plataformarepositorio plataformaRepo;', content)
    content = re.sub(r'@Autowired\s+private PartidoR partidoRepo;', r'private final PartidoR partidoRepo;', content)
    
    if "public DataInicializador(UsuarioR usuarioRepo" not in content:
        constructor = """
    public DataInicializador(UsuarioR usuarioRepo, Plataformarepositorio plataformaRepo, PartidoR partidoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.plataformaRepo = plataformaRepo;
        this.partidoRepo = partidoRepo;
    }
"""
        content = re.sub(r'public class DataInicializador implements CommandLineRunner \{', r'public class DataInicializador implements CommandLineRunner {\n' + constructor, content)

    if "@NonNull String... args" not in content:
        content = content.replace('public void run(String... args)', 'public void run(@org.springframework.lang.NonNull String... args)')
        
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

def fix_ivrsi():
    path = r"src\main\java\pe\edu\utp\proyecto\service\impl\IvrSI.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    lines = content.split('\n')
    filtered = [l for l in lines if not l.startswith('import pe.edu.utp.proyecto.modelo.Partido;') and 
                not l.startswith('import java.util.List;')]
    with open(path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(filtered))

def fix_home_controller():
    path = r"src\main\java\pe\edu\utp\proyecto\controller\HomeController.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    content = content.replace('.replaceAll(', '.replace(')
    lines = content.split('\n')
    filtered = [l for l in lines if not l.startswith('import java.util.ArrayList;')]
    with open(path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(filtered))

def fix_bitacora():
    path = r"src\main\java\pe\edu\utp\proyecto\service\patron\singleton\BitacoraSingleton.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace('private List<String> historial = new ArrayList<>();', 'private final List<String> historial = new ArrayList<>();')
    lines = content.split('\n')
    filtered = [l for l in lines if not l.startswith('import java.util.ArrayList;')]
    with open(path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(filtered))

def fix_ticket_event():
    path = r"src\main\java\pe\edu\utp\proyecto\service\patron\observer\TicketApuestaEvent.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace('private String mensaje;', 'private final String mensaje;')
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

def fix_usuario_controller():
    path = r"src\main\java\pe\edu\utp\proyecto\controller\UsuarioController.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    lines = content.split('\n')
    filtered = [l for l in lines if not l.startswith('import org.springframework.ui.Model;') and not l.startswith('import pe.edu.utp.proyecto.modelo.Usuario;')]
    with open(path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(filtered))

def fix_login_interceptor():
    path = r"src\main\java\pe\edu\utp\proyecto\config\LoginInterceptor.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace('HttpServletRequest request', '@org.springframework.lang.NonNull HttpServletRequest request')
    content = content.replace('HttpServletResponse response', '@org.springframework.lang.NonNull HttpServletResponse response')
    content = content.replace('Object handler', '@org.springframework.lang.NonNull Object handler')
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

def fix_config_web():
    path = r"src\main\java\pe\edu\utp\proyecto\config\configWeb.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace('InterceptorRegistry registry', '@org.springframework.lang.NonNull InterceptorRegistry registry')
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

fix_data_inicializador()
fix_ivrsi()
fix_home_controller()
fix_bitacora()
fix_ticket_event()
fix_usuario_controller()
fix_login_interceptor()
fix_config_web()
print("Fixes applied.")
