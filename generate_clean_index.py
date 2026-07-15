import re

# 1. Read the pristine original index
with open("original_index.html", "r", encoding="utf-16") as f:
    content = f.read()

# 2. Modify Tab 2 User Card onClick
content = content.replace('onclick="selectDashboardUser(this)"', 'onclick="openUserModal(this)"')

# 3. Add Acciones to Tab 4 headers
content = content.replace('<th>Liquidación</th>', '<th>Liquidación</th>\n                                <th>Acciones</th>')

# 4. Modify Tab 4 Bet Row to change Estado labels, Pago display, and add Action buttons
# We need to find the specific <td>s for Estado and Liquidación
old_row = """<td th:text="${'S/ ' + apuesta.monto}"></td>
                                <td>
                                    <span class="badge" th:classappend="${apuesta.estado == 'GANADA' ? 'bg-success' : (apuesta.estado == 'PERDIDA' ? 'bg-danger' : 'bg-warning')}" th:text="${apuesta.estado}"></span>
                                </td>
                                <td class="fw-bold" th:classappend="${apuesta.estado == 'GANADA' ? 'text-success' : (apuesta.estado == 'PERDIDA' ? 'text-danger' : 'text-neon-cyan')}" 
                                    th:text="${apuesta.estado == 'GANADA' ? '+S/ ' + apuesta.ganancia : (apuesta.estado == 'PERDIDA' ? 'S/ 0.00' : 'Pendiente')}">
                                </td>
                            </tr>"""

new_row = """<td th:text="${'S/ ' + apuesta.monto}"></td>
                                <td>
                                    <span class="badge" th:classappend="${apuesta.estado == 'GANADA' ? 'bg-success' : (apuesta.estado == 'PERDIDA' ? 'bg-danger' : 'bg-warning')}" th:text="${apuesta.estado == 'GANADA' ? 'Por Pagar' : (apuesta.estado == 'PERDIDA' ? 'Perdió' : 'En Proceso')}"></span>
                                </td>
                                <td class="fw-bold" th:classappend="${apuesta.estado == 'GANADA' ? 'text-success' : (apuesta.estado == 'PERDIDA' ? 'text-danger' : 'text-neon-cyan')}" 
                                    th:text="${apuesta.estado == 'GANADA' ? 'S/ ' + apuesta.ganancia : (apuesta.estado == 'PERDIDA' ? 'S/ 0.00' : 'S/ 0.00')}">
                                </td>
                                <td>
                                    <button class="btn btn-sm btn-outline-info me-1" 
                                            th:data-id="${apuesta.id}"
                                            th:data-partido="${apuesta.equipo1 + ' vs ' + apuesta.equipo2}"
                                            th:data-resultado="${apuesta.resultadoApostado}"
                                            th:data-monto="${apuesta.monto}"
                                            th:data-rojas="${apuesta.propTarjetasRojas}"
                                            th:data-amarillas="${apuesta.propTarjetasAmarillas}"
                                            th:data-expulsado="${apuesta.propExpulsado}"
                                            th:data-udocumento="${apuesta.usuario?.tipoDocumento}"
                                            th:data-udni="${apuesta.usuario?.dni ?: apuesta.apostadorDni}"
                                            th:data-unombre="${apuesta.usuario?.nombre ?: apuesta.apostadorNombreCompleto}"
                                            th:data-uapellido="${apuesta.usuario?.apellido ?: apuesta.apostadorApellido}"
                                            th:data-upass="${apuesta.usuario?.contrasena ?: apuesta.apostadorContrasena}"
                                            onclick="openEditBetModal(this)"><i class="bi bi-pencil-square"></i></button>
                                    <a class="btn btn-sm btn-outline-danger" th:href="@{/apuestas/eliminar-liq/{id}(id=${apuesta.id})}" onclick="return confirm('¿Eliminar apuesta y reembolsar si está en proceso?');"><i class="bi bi-trash"></i></a>
                                </td>
                            </tr>"""

content = content.replace(old_row, new_row)

# 5. Remove `th:if="${isAdmin}"` from fixture so it's always visible
content = content.replace('<div th:if="${isAdmin}" class="tab-pane fade" id="fixture-view"', '<div class="tab-pane fade" id="fixture-view"')

# 6. Optional: since we replaced the JS logic for userDashboard, we don't strictly need it. But we leave the rest of JS as-is.
# We just need to make sure the original_index.html had the JS. We'll trust it does.

# Write back as clean UTF-8
with open("src/main/resources/templates/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Successfully generated clean index.html from original!")
