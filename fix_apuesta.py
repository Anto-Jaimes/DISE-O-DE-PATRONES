with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'r', encoding='utf-8') as f:
    lines = f.readlines()

start_idx = -1
for i, line in enumerate(lines):
    if 'apuesta.setPropTarjetasRojas(propTarjetasRojas);' in line:
        start_idx = i
        break

if start_idx != -1:
    closing_lines = [
        '            apuestaServicio.eliminar(id);\n',
        '            pe.edu.utp.proyecto.service.patron.singleton.BitacoraSingleton.getInstancia().registrar("Apuesta eliminada desde liquidacion: " + apuesta.getCodigoTicket());\n',
        '        }\n',
        '        return "redirect:/?tab=4&success=apuesta_eliminada";\n',
        '    }\n'
    ]
    lines[start_idx:start_idx+20] = closing_lines
    
    with open('src/main/java/pe/edu/utp/proyecto/controller/ApuestaController.java', 'w', encoding='utf-8') as f:
        f.writelines(lines)
    print('Fixed!')
else:
    print('Could not find start_idx')
