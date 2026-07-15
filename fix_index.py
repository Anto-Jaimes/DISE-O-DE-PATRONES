with open('src/main/resources/templates/index.html', 'r', encoding='utf-8') as f:
    html = f.read()

# Fix space in IDs in JS
html = html.replace("document.getElementById('modalEquipo 1Select')", "document.getElementById('modalEquipo1Select')")
html = html.replace("document.getElementById('modalEquipo 2Select')", "document.getElementById('modalEquipo2Select')")

# Fix space in IDs in HTML
html = html.replace('id="modalEquipo 1Select"', 'id="modalEquipo1Select"')
html = html.replace('id="modalEquipo 2Select"', 'id="modalEquipo2Select"')

# Fix fallback user data in tab 2 'Editar' button
old_button_data = """th:data-udocumento="${apuesta.usuario?.tipoDocumento}"
                                            th:data-udni="${apuesta.usuario?.dni}"
                                            th:data-unombre="${apuesta.usuario?.nombre}"
                                            th:data-uapellido="${apuesta.usuario?.apellido}"
                                            th:data-upass="${apuesta.usuario?.contrasena}"
                                            onclick="openEditBetModal(this)">"""

new_button_data = """th:data-udocumento="${apuesta.usuario != null ? apuesta.usuario.tipoDocumento : 'DNI'}"
                                            th:data-udni="${apuesta.usuario != null ? apuesta.usuario.dni : apuesta.apostadorDni}"
                                            th:data-unombre="${apuesta.usuario != null ? apuesta.usuario.nombre : apuesta.apostadorNombreCompleto}"
                                            th:data-uapellido="${apuesta.usuario != null ? apuesta.usuario.apellido : apuesta.apostadorApellido}"
                                            th:data-upass="${apuesta.usuario != null ? apuesta.usuario.contrasena : apuesta.apostadorContrasena}"
                                            onclick="openEditBetModal(this)">"""

html = html.replace(old_button_data, new_button_data)

# Also fix getAttribute in openEditBetModal so that nulls don't become "null" string
old_js = """        document.getElementById('eb_udni').value = btn.getAttribute('data-udni');
        document.getElementById('eb_unombre').value = btn.getAttribute('data-unombre');
        document.getElementById('eb_uapellido').value = btn.getAttribute('data-uapellido');
        document.getElementById('eb_upass').value = btn.getAttribute('data-upass');"""

new_js = """        document.getElementById('eb_udni').value = btn.getAttribute('data-udni') || '';
        document.getElementById('eb_unombre').value = btn.getAttribute('data-unombre') || '';
        document.getElementById('eb_uapellido').value = btn.getAttribute('data-uapellido') || '';
        document.getElementById('eb_upass').value = btn.getAttribute('data-upass') || '';"""

html = html.replace(old_js, new_js)

with open('src/main/resources/templates/index.html', 'w', encoding='utf-8') as f:
    f.write(html)
print('Fixed index.html!')
