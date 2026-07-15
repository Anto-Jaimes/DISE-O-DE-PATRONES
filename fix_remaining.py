import os

def fix_partido_controller():
    path = r"src\main\java\pe\edu\utp\proyecto\controller\PartidoController.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Remove unused fields
    content = content.replace("private final ApuestaR apuestaRepo;\n", "")
    content = content.replace("private final UsuarioR usuarioRepo;\n", "")
    content = content.replace("this.apuestaRepo = apuestaRepo;\n", "")
    content = content.replace("this.usuarioRepo = usuarioRepo;\n", "")
    # Fix constructor signature
    content = content.replace("public PartidoController(PartidoServicio servicio, ApuestaR apuestaRepo, UsuarioR usuarioRepo)", "public PartidoController(PartidoServicio servicio)")
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

def fix_ivr_serv_simulado():
    path = r"src\main\java\pe\edu\utp\proyecto\service\impl\IvrServSimuladoImpl.java"
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
        
    content = content.replace("private final RespuestaStrategy usuarioStrategy;\n", "")
    content = content.replace("this.usuarioStrategy = new RespuestaStrategy()", "RespuestaStrategy usuarioStrategy = new RespuestaStrategy()")
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

fix_partido_controller()
fix_ivr_serv_simulado()
print("Fixed remaining warnings.")
