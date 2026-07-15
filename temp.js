
    function openPlaceBetModal(btn) {
        document.getElementById('pb_partidoId').value = btn.getAttribute('data-id');
        const equipo1 = btn.getAttribute('data-equipo1');
        const equipo2 = btn.getAttribute('data-equipo2');
        document.getElementById('pb_partidoNombre').innerText = equipo1 + " vs " + equipo2;
        document.getElementById('pb_optEquipo1').innerText = equipo1;
        document.getElementById('pb_optEquipo2').innerText = equipo2;
        
        var placeBetModal = new bootstrap.Modal(document.getElementById('placeBetModal'));
        placeBetModal.show();
    }

    function openEditBetModal(btn) {
        document.getElementById('eb_apuestaId').value = btn.getAttribute('data-id');
        document.getElementById('eb_partidoNombre').innerText = btn.getAttribute('data-partido');
        
        let resultado = btn.getAttribute('data-resultado');
        if (resultado === 'LOCAL') resultado = 'EQUIPO 1';
        if (resultado === 'VISITANTE') resultado = 'EQUIPO 2';
        document.getElementById('eb_resultadoApostado').value = resultado;
        
        document.getElementById('eb_monto').value = btn.getAttribute('data-monto');
        document.getElementById('eb_rojas').value = btn.getAttribute('data-rojas');
        document.getElementById('eb_amarillas').value = btn.getAttribute('data-amarillas');
        document.getElementById('eb_expulsado').value = btn.getAttribute('data-expulsado');
        
        // Cargar datos de usuario
        document.getElementById('eb_udocumento').value = btn.getAttribute('data-udocumento') || 'DNI';
        document.getElementById('eb_udni').value = btn.getAttribute('data-udni');
        document.getElementById('eb_unombre').value = btn.getAttribute('data-unombre');
        document.getElementById('eb_uapellido').value = btn.getAttribute('data-uapellido');
        document.getElementById('eb_upass').value = btn.getAttribute('data-upass');
        
        var editBetModal = new bootstrap.Modal(document.getElementById('editBetModal'));
        editBetModal.show();
    }
        function openUserModal(btn) {
            const modalTitle = document.getElementById('userModalTitle');
            const idInput = document.getElementById('userIdInput');
            const nombreInput = document.getElementById('userNombre');
            const apellidoInput = document.getElementById('userApellido');
            const dniInput = document.getElementById('userDni');
            const emailInput = document.getElementById('userEmail');
            const saldoInput = document.getElementById('userSaldo');
            
            if(btn) {
                // Modo Editar
                modalTitle.innerText = "Editar Apostador";
                idInput.value = btn.getAttribute('data-id');
                nombreInput.value = btn.getAttribute('data-nombre');
                apellidoInput.value = btn.getAttribute('data-apellido');
                dniInput.value = btn.getAttribute('data-dni');
                emailInput.value = btn.getAttribute('data-email');
                saldoInput.value = btn.getAttribute('data-saldo');
            } else {
                // Modo Nuevo
                modalTitle.innerText = "Registrar Apostador";
                idInput.value = '';
                nombreInput.value = '';
                apellidoInput.value = '';
                dniInput.value = '';
                emailInput.value = '';
                saldoInput.value = '';
            }
            
            const modal = new bootstrap.Modal(document.getElementById('userModal'));
            modal.show();
        }

        let selectedOdds = 0;

        function onMatchChange() {
            const select = document.getElementById('matchSelect');
            const selectedOption = select.options[select.selectedIndex];
            
            const panel = document.getElementById('oddsPanel');
            const propsPanel = document.getElementById('propsPanel');
            
            if (!selectedOption.value) {
                panel.classList.add('d-none');
                propsPanel.classList.add('d-none');
                selectedOdds = 0;
                calculatePotentialReturn();
                return;
            }

            panel.classList.remove('d-none');
            propsPanel.classList.remove('d-none');

            // Set odds details
            const equipo1Team = selectedOption.getAttribute('data-equipo1');
            const equipo2Team = selectedOption.getAttribute('data-equipo2');
            const cequipo1 = parseFloat(selectedOption.getAttribute('data-cequipo1'));
            const cempate = parseFloat(selectedOption.getAttribute('data-cempate'));
            const cequipo2 = parseFloat(selectedOption.getAttribute('data-cequipo2'));

            document.getElementById('labelEquipo1').innerText = 'Gana ' + equipo1Team;
            document.getElementById('valEquipo1').innerText = cequipo1.toFixed(2);

            if (document.getElementById('valEmpate')) {
                document.getElementById('valEmpate').innerText = cempate.toFixed(2);
            }

            document.getElementById('labelEquipo2').innerText = 'Gana ' + equipo2Team;
            document.getElementById('valEquipo2').innerText = cequipo2.toFixed(2);

            // Reset prediction selection
            resetOddsButtons();
        }

        function resetOddsButtons() {
            document.getElementById('btnEquipo1').classList.remove('active');
            if (document.getElementById('btnEmpate')) {
                document.getElementById('btnEmpate').classList.remove('active');
            }
            document.getElementById('btnEquipo2').classList.remove('active');
            document.getElementById('predictionInput').value = '';
            selectedOdds = 0;
            calculatePotentialReturn();
        }

        function selectPrediction(prediction) {
            resetOddsButtons();
            
            const select = document.getElementById('matchSelect');
            const selectedOption = select.options[select.selectedIndex];
            
            if (prediction === 'EQUIPO 1') {
                document.getElementById('btnEquipo1').classList.add('active');
                selectedOdds = parseFloat(selectedOption.getAttribute('data-cequipo1'));
            } else if (prediction === 'EMPATE') {
                document.getElementById('btnEmpate').classList.add('active');
                selectedOdds = parseFloat(selectedOption.getAttribute('data-cempate'));
            } else if (prediction === 'EQUIPO 2') {
                document.getElementById('btnEquipo2').classList.add('active');
                selectedOdds = parseFloat(selectedOption.getAttribute('data-cequipo2'));
            }

            document.getElementById('predictionInput').value = prediction;
            calculatePotentialReturn();
        }

        function calculatePotentialReturn() {
            const stake = parseFloat(document.getElementById('stakeInput').value) || 0;
            const payout = stake * selectedOdds;
            
            const returnLabel = document.getElementById('potentialReturnLabel');
            const lossLabel = document.getElementById('potentialLossLabel');
            
            if (returnLabel) returnLabel.innerText = 'S/ ' + payout.toFixed(2);
            if (lossLabel) lossLabel.innerText = 'S/ ' + stake.toFixed(2);
        }

        // URL Tab switcher on load
        window.addEventListener('load', function() {
            const urlParams = new URLSearchParams(window.location.search);
            const tabParam = urlParams.get('tab');
            if (tabParam) {
                let tabTargetId = '';
                if (tabParam === '1') tabTargetId = 'register-tab';
                else if (tabParam === '2') tabTargetId = 'bet-tab';
                else if (tabParam === '3') tabTargetId = 'fixture-tab';
                else if (tabParam === '4') tabTargetId = 'history-tab';
                
                const tabBtn = document.getElementById(tabTargetId);
                if (tabBtn) {
                    const tab = new bootstrap.Tab(tabBtn);
                    tab.show();
                }
            }
        });

        // Ventana 2: Simulador Functions
        function onSimStakeChange(val) {
            document.getElementById('simStakeText').innerText = '$' + val;
            runSimulate();
        }

        function onSimOddsChange(val) {
            document.getElementById('simOddsText').innerText = parseFloat(val).toFixed(2) + 'x';
            runSimulate();
        }

        function runSimulate() {
            const stake = parseFloat(document.getElementById('simStakeRange').value);
            const odds = parseFloat(document.getElementById('simOddsRange').value);

            const payout = stake * odds;
            const net = payout - stake;

            document.getElementById('simTotalPayout').innerText = '$' + payout.toFixed(2);
            document.getElementById('simNetProfit').innerText = '$' + net.toFixed(2);
        }

        // Sync inputs from Ventana 1 to Ventana 2 when tab loads
        function syncSimData() {
            const stake = parseFloat(document.getElementById('stakeInput').value);
            if (stake && stake >= 10 && stake <= 2000) {
                document.getElementById('simStakeRange').value = stake;
                document.getElementById('simStakeText').innerText = '$' + stake;
            }
            if (selectedOdds > 0) {
                document.getElementById('simOddsRange').value = selectedOdds;
                document.getElementById('simOddsText').innerText = selectedOdds.toFixed(2) + 'x';
            }
            runSimulate();
        }

        // Calculate and update Responsable limits visual indicators dynamically
        window.addEventListener('load', function() {
            // Count all current bets amounts to set the limit usage
            let totalApostadoHoy = 0;
            const rows = document.querySelectorAll('#history-view tbody tr');
            rows.forEach(row => {
                const cellMonto = row.children[4];
                if (cellMonto) {
                    const val = parseFloat(cellMonto.innerText.replace('$', '')) || 0;
                    totalApostadoHoy += val;
                }
            });

            const limitMax = 1000;
            const percent = Math.min((totalApostadoHoy / limitMax) * 100, 100);
            
            const bar = document.getElementById('limitProgressBar');
            if (bar) {
                bar.style.width = percent + '%';
                bar.setAttribute('aria-valuenow', totalApostadoHoy);
                document.getElementById('limitUsageText').innerText = 'Consumido: $' + totalApostadoHoy.toFixed(2);
                document.getElementById('limitRemainingText').innerText = 'Disponible: $' + Math.max(limitMax - totalApostadoHoy, 0).toFixed(2);
            }
        });

        // Helper to parse candidates from a placeholder team string
        function getCandidatesForPlaceholder(placeholder) {
            if (!placeholder) return [];
            let candidates = [];
            if (placeholder.includes('Ganador ')) {
                const clean = placeholder.replace('Ganador ', '');
                if (clean.includes('/')) {
                    candidates = clean.split('/');
                } else if (clean.includes('Semifinal 1')) {
                    candidates = ['Francia', 'España', 'Bélgica'];
                } else if (clean.includes('Semifinal 2')) {
                    candidates = ['Uruguay', 'Inglaterra', 'Argentina', 'Suiza'];
                } else {
                    candidates = [clean];
                }
            }
            return candidates;
        }

        // Onchange handlers for equipo 1/visitor dropdown selects
        function onEquipo1SelectChange() {
            const select = document.getElementById('modalEquipo1Select');
            const input = document.getElementById('modalEquipo1Input');
            input.value = select.value;
            updateModalWinnerSelect();
        }
        
        function onEquipo2SelectChange() {
            const select = document.getElementById('modalEquipo2Select');
            const input = document.getElementById('modalEquipo2Input');
            input.value = select.value;
            updateModalWinnerSelect();
        }

        // Helper to update options in the winner select dropdown dynamically
        function updateModalWinnerSelect() {
            const valEquipo1 = document.getElementById('modalEquipo1Input').value;
            const valEquipo2 = document.getElementById('modalEquipo2Input').value;
            const select = document.getElementById('modalWinnerSelect');
            
            // Remember chosen value
            const currentSelected = select.value;
            select.innerHTML = '<option value="">-- Escoger Ganador --</option>';
            
            // Parse possible equipo 1 options
            let equipo1Options = [valEquipo1];
            if (valEquipo1.includes('Ganador ')) {
                const parts = getCandidatesForPlaceholder(valEquipo1);
                if (parts.length > 0) equipo1Options = parts;
            }
            
            // Parse possible visitor options
            let equipo2Options = [valEquipo2];
            if (valEquipo2.includes('Ganador ')) {
                const parts = getCandidatesForPlaceholder(valEquipo2);
                if (parts.length > 0) equipo2Options = parts;
            }
            
            // Populate options
            equipo1Options.forEach(opt => {
                if (opt && !opt.includes('Ganador ')) {
                    const optEl = document.createElement('option');
                    optEl.value = opt;
                    optEl.text = opt;
                    select.appendChild(optEl);
                }
            });
            
            equipo2Options.forEach(opt => {
                if (opt && !opt.includes('Ganador ')) {
                    const optEl = document.createElement('option');
                    optEl.value = opt;
                    optEl.text = opt;
                    select.appendChild(optEl);
                }
            });
            
            // Try to restore previous selection
            select.value = currentSelected || '';
        }

        // Function to open administrative edit modal for a specific match
        function openEditMatchModal(btn) {
            const id = btn.getAttribute('data-id');
            
            // Fetch exact data from API
            fetch('/partidos/api/' + id)
                .then(res => res.json())
                .then(partido => {
                    document.getElementById('modalPartidoId').value = partido?.id;
                    document.getElementById('modalMatchTitle').innerText = partido?.equipo1 + ' vs ' + partido?.equipo2;
                    document.getElementById('modalMatchEstadio').innerText = partido?.estadio || 'Estadio por definir';
                    document.getElementById('modalMatchFecha').innerText = partido?.fecha || 'Fecha por definir';
                    
                    document.getElementById('modalEquipo1Input').value = partido?.equipo1;
                    document.getElementById('modalEquipo2Input').value = partido?.equipo2;
                    document.getElementById('modalCuotaEquipo1Input').value = partido.cuotaEquipo1.toFixed(2);

                    document.getElementById('modalCuotaEquipo2Input').value = partido.cuotaEquipo2.toFixed(2);
                    
                    // Setup Equipo 1 (Equipo 1) input vs select
                    const equipo1Candidates = getCandidatesForPlaceholder(partido?.equipo1);
                    const equipo1Input = document.getElementById('modalEquipo1Input');
                    const equipo1Select = document.getElementById('modalEquipo1Select');
                    if (equipo1Candidates.length > 0) {
                        equipo1Input.classList.add('d-none');
                        equipo1Select.classList.remove('d-none');
                        equipo1Select.innerHTML = '<option value="">-- Seleccionar Equipo --</option>';
                        equipo1Candidates.forEach(cand => {
                            const opt = document.createElement('option');
                            opt.value = cand;
                            opt.text = cand;
                            equipo1Select.appendChild(opt);
                        });
                        equipo1Select.value = '';
                    } else {
                        equipo1Input.classList.remove('d-none');
                        equipo1Select.classList.add('d-none');
                    }
                    
                    // Setup Visitor (Equipo 2) input vs select
                    const equipo2Candidates = getCandidatesForPlaceholder(partido?.equipo2);
                    const equipo2Input = document.getElementById('modalEquipo2Input');
                    const equipo2Select = document.getElementById('modalEquipo2Select');
                    if (equipo2Candidates.length > 0) {
                        equipo2Input.classList.add('d-none');
                        equipo2Select.classList.remove('d-none');
                        equipo2Select.innerHTML = '<option value="">-- Seleccionar Equipo --</option>';
                        equipo2Candidates.forEach(cand => {
                            const opt = document.createElement('option');
                            opt.value = cand;
                            opt.text = cand;
                            equipo2Select.appendChild(opt);
                        });
                        equipo2Select.value = '';
                    } else {
                        equipo2Input.classList.remove('d-none');
                        equipo2Select.classList.add('d-none');
                    }
                    
                    // Update select options dropdown
                    updateModalWinnerSelect();
                    
                    // Set current winner
                    const select = document.getElementById('modalWinnerSelect');
                    if (partido?.ganador === 'EQUIPO 1') {
                        select.value = partido?.equipo1;
                    } else if (partido?.ganador === 'EQUIPO 2') {
                        select.value = partido?.equipo2;
                    } else if (partido?.ganador === 'EMPATE') {
                        select.value = 'EMPATE';
                    } else {
                        select.value = '';
                    }
                    
                    const editModal = new bootstrap.Modal(document.getElementById('editMatchModal'));
                    editModal.show();
                })
                .catch(err => {
                    console.error("Error fetching match details: ", err);
                    alert("No se pudo cargar la información del partido.");
                });
        }

        function showTicket(btn) {
            const id = btn.getAttribute('data-id');
            const cliente = btn.getAttribute('data-cliente');
            const codigo = btn.getAttribute('data-codigo');
            const codigoTicket = btn.getAttribute('data-codigoticket') || ('#TX-2026-' + id.padStart(4, '0'));
            const dni = btn.getAttribute('data-dni') || '-';
            const prop = btn.getAttribute('data-prop') || '';
            const fecha = btn.getAttribute('data-fecha');
            const partido = btn.getAttribute('data-partido');
            const partidofecha = btn.getAttribute('data-partidofecha');
            const pronostico = btn.getAttribute('data-pronostico');
            const cuota = parseFloat(btn.getAttribute('data-cuota'));
            const monto = parseFloat(btn.getAttribute('data-monto'));
            const ganancia = parseFloat(btn.getAttribute('data-ganancia'));
            const estado = btn.getAttribute('data-estado');

            document.getElementById('ticketIdVal').innerText = codigoTicket.startsWith('#') ? codigoTicket : ('#' + codigoTicket);
            document.getElementById('ticketClienteVal').innerText = cliente;
            document.getElementById('ticketDniVal').innerText = dni;
            document.getElementById('ticketCodigoVal').innerText = codigo;
            document.getElementById('ticketFechaVal').innerText = fecha;
            document.getElementById('ticketPartidoVal').innerText = partido;
            document.getElementById('ticketMatchFechaVal').innerText = partidofecha;
            document.getElementById('ticketPronosticoVal').innerText = pronostico;
            
            const propsRow = document.getElementById('ticketPropsRow');
            if (prop) {
                propsRow.style.setProperty('display', 'flex', 'important');
                document.getElementById('ticketPropsVal').innerText = prop;
            } else {
                propsRow.style.setProperty('display', 'none', 'important');
            }
            
            document.getElementById('ticketCuotaVal').innerText = cuota.toFixed(2) + 'x';
            document.getElementById('ticketMontoVal').innerText = '$' + monto.toFixed(2);
            
            let payout = 0;
            if (estado === 'PENDIENTE') {
                payout = monto * cuota;
            } else {
                payout = ganancia;
            }
            
            document.getElementById('ticketPayoutVal').innerText = '$' + payout.toFixed(2);
            
            const neto = payout > 0 ? (payout - monto) : -monto;
            
            if (estado === 'PERDIDA') {
                document.getElementById('ticketNetoVal').innerText = '$0.00';
                document.getElementById('ticketNetoVal').style.color = '#64748b';
            } else {
                document.getElementById('ticketNetoVal').innerText = '$' + (monto * cuota - monto).toFixed(2);
                document.getElementById('ticketNetoVal').style.color = '#15803d';
            }
            
            document.getElementById('ticketPerdidaVal').innerText = '-$' + monto.toFixed(2);

            const badge = document.getElementById('ticketEstadoBadge');
            badge.innerText = estado;
            if (estado === 'PENDIENTE') {
                badge.className = 'badge bg-warning text-dark';
            } else if (estado === 'GANADA') {
                badge.className = 'badge bg-success text-white';
                badge.innerText = '¡APUESTA GANADA!';
            } else {
                badge.className = 'badge bg-danger text-white';
                badge.innerText = 'APUESTA PERDIDA';
            }

            const modal = new bootstrap.Modal(document.getElementById('ticketModal'));
            modal.show();
        }
        function onUserChange() {
            const userSelect = document.getElementById('userSelect');
            const saldoInfoDiv = document.getElementById('saldoInfoDiv');
            const userSaldoLabel = document.getElementById('userSaldoLabel');
            
            if (userSelect.value) {
                const selectedOption = userSelect.options[userSelect.selectedIndex];
                const saldo = parseFloat(selectedOption.getAttribute('data-saldo')).toFixed(2);
                userSaldoLabel.innerText = saldo;
                saldoInfoDiv.style.display = 'block';
            } else {
                saldoInfoDiv.style.display = 'none';
            }
        }
        function selectDashboardUser(btn) {
            // Update active state of buttons
            document.querySelectorAll('.user-card-btn').forEach(b => b.classList.remove('border-neon-cyan'));
            
            const userId = btn.getAttribute('data-userid');
            const userName = btn.getAttribute('data-nombre');
            const userSaldo = btn.getAttribute('data-saldo');
            
            // Show modal
            var dashboardModal = new bootstrap.Modal(document.getElementById('userDashboardModal'));
            dashboardModal.show();
            
            // Update stats
            document.getElementById('dashUserName').innerText = userName;
            document.getElementById('dashUserSaldo').innerText = 'S/ ' + parseFloat(userSaldo).toFixed(2);
            
            let totalWon = 0;
            let totalPending = 0;
            
            // Group bets by Partido (Tree structure) and collect details
            const bets = [];
            const rows = document.querySelectorAll('#rawBetsTable tbody tr.bet-row');
            rows.forEach(row => {
                if (row.getAttribute('data-userid') === userId) {
                    const bet = {
                        id: row.getAttribute('data-id'),
                        ticketId: row.getAttribute('data-ticketid'),
                        partido: row.getAttribute('data-partido'),
                        partidoFecha: row.getAttribute('data-partidofecha'),
                        monto: parseFloat(row.getAttribute('data-monto') || '0'),
                        cuota: parseFloat(row.getAttribute('data-cuota') || '1'),
                        ganancia: parseFloat(row.getAttribute('data-ganancia') || '0'),
                        estado: row.getAttribute('data-estado'),
                        fecha: row.getAttribute('data-fecha'), // e.g. "dd/MM/yyyy HH:mm"
                        dni: row.getAttribute('data-dni'),
                        cliente: row.getAttribute('data-cliente'),
                        codigo: row.getAttribute('data-codigo'),
                        prop: row.getAttribute('data-prop'),
                        pronostico: row.getAttribute('data-pronostico')
                    };
                    bets.push(bet);
                    
                    if (bet.estado === 'GANADA') {
                        totalWon += bet.ganancia;
                    } else if (bet.estado === 'PENDIENTE') {
                        totalPending += bet.ganancia;
                    }
                }
            });
            
            document.getElementById('dashUserTotalWon').innerText = 'S/ ' + totalWon.toFixed(2);
            document.getElementById('dashUserTotalPending').innerText = 'S/ ' + totalPending.toFixed(2);
            
            // Sort bets by date descending (Stack LIFO order)
            bets.sort((a, b) => {
                const parseDate = (dStr) => {
                    if (!dStr) return new Date(0);
                    const parts = dStr.split(' ');
                    const dateParts = parts[0].split('/');
                    const timeParts = parts[1].split(':');
                    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0], timeParts[0], timeParts[1]);
                };
                return parseDate(b.fecha) - parseDate(a.fecha);
            });
            
            // Group bets by Partido (Tree Node)
            const groupedBets = {};
            bets.forEach(bet => {
                if (!groupedBets[bet.partido]) {
                    groupedBets[bet.partido] = [];
                }
                groupedBets[bet.partido].push(bet);
            });
            
            // Build tree UI
            const treeContainer = document.getElementById('userBetsTreeContainer');
            treeContainer.innerHTML = '';
            
            const partidos = Object.keys(groupedBets);
            if (partidos.length === 0) {
                treeContainer.innerHTML = `
                    <div class="text-center py-4 text-white-50 border border-secondary rounded" style="background: rgba(255,255,255,0.02)">
                        <i class="bi bi-receipt fs-3 d-block mb-2 text-muted"></i>
                        Este apostador aún no tiene tickets de apuestas.
                    </div>`;
                return;
            }
            
            // Explanatory badge for data structures
            const infoBadge = document.createElement('div');
            infoBadge.className = 'alert alert-dark border-secondary py-2 px-3 mb-3 d-flex align-items-center justify-content-between';
            infoBadge.style.fontSize = '0.8rem';
            infoBadge.innerHTML = `
                <div>
                    <i class="bi bi-diagram-3-fill text-neon-cyan me-2"></i> Estructuras Aplicadas: 
                    <strong>Árbol de Partidos</strong> conteniendo una <strong>Pila (Stack LIFO)</strong> de tickets.
                </div>
                <span class="badge bg-secondary text-neon-cyan">Últimos Primero</span>
            `;
            treeContainer.appendChild(infoBadge);
            
            let nodeIndex = 0;
            partidos.forEach(partidoName => {
                const matchBets = groupedBets[partidoName];
                const nodeId = 'treeNode-' + nodeIndex++;
                
                const nodeDiv = document.createElement('div');
                nodeDiv.className = 'tree-node mb-3 border border-secondary rounded overflow-hidden';
                nodeDiv.style.background = 'rgba(255,255,255,0.01)';
                
                // Header (Match Node)
                nodeDiv.innerHTML = `
                    <div class="tree-header p-3 bg-dark d-flex justify-content-between align-items-center cursor-pointer" 
                         style="border-bottom: 1px solid rgba(255,255,255,0.08); transition: background 0.2s;"
                         onclick="toggleTreeNode('${nodeId}')">
                        <span class="fw-bold text-white">
                            <i class="bi bi-caret-right-fill text-neon-cyan me-2" id="caret-${nodeId}"></i>
                            ⚽ ${partidoName}
                        </span>
                        <span class="badge bg-secondary text-white">${matchBets.length} apuesta(s)</span>
                    </div>
                    <div class="tree-body d-none p-3" id="body-${nodeId}" style="background: rgba(0,0,0,0.2);">
                    </div>
                `;
                
                const bodyContainer = nodeDiv.querySelector(`#body-${nodeId}`);
                
                // Add tickets inside the tree body (sorted as a Stack)
                matchBets.forEach(bet => {
                    const ticketDiv = document.createElement('div');
                    ticketDiv.className = 'card bg-dark border-secondary p-3 mb-2 rounded shadow-sm';
                    
                    let statusBadge = '';
                    if (bet.estado === 'PENDIENTE') {
                        statusBadge = '<span class="badge bg-warning text-dark"><i class="bi bi-hourglass-split"></i> PENDIENTE</span>';
                    } else if (bet.estado === 'GANADA') {
                        statusBadge = '<span class="badge bg-success text-white"><i class="bi bi-check-circle"></i> GANADA</span>';
                    } else if (bet.estado === 'PERDIDA') {
                        statusBadge = '<span class="badge bg-danger text-white"><i class="bi bi-x-circle"></i> PERDIDA</span>';
                    }
                    
                    let actionHtml = '';
                    if (bet.estado === 'PENDIENTE') {
                        actionHtml = `
                            <form action="/apuestas/resolver" method="post" class="d-flex align-items-center mt-2 pt-2 border-top border-secondary">
                                <input type="hidden" name="apuestaId" value="${bet.id}" />
                                <span class="small text-white-50 me-2" style="font-size:0.75rem;">Simular:</span>
                                <select name="resultadoReal" class="form-select form-select-sm bg-dark text-white border-secondary me-2" style="width: auto;" required>
                                    <option value="">Ganador...</option>
                                    <option value="EQUIPO 1">Gana Equipo 1</option>
                                    <option value="EQUIPO 2">Gana Equipo 2</option>
                                </select>
                                <button type="submit" class="btn btn-sm btn-neon-green fw-bold text-black py-1 px-3">
                                    <i class="bi bi-play-fill"></i> Resolver
                                </button>
                            </form>
                        `;
                    } else {
                        actionHtml = `
                            <div class="mt-2 pt-2 border-top border-secondary text-end">
                                <button class="btn btn-sm btn-outline-info rounded-pill px-3 py-1" onclick="showTicket(this)"
                                    data-id="${bet.id}"
                                    data-codigoticket="${bet.ticketId}"
                                    data-dni="${bet.dni}"
                                    data-prop="${bet.prop}"
                                    data-cliente="${bet.cliente}"
                                    data-codigo="${bet.codigo}"
                                    data-fecha="${bet.fecha}"
                                    data-partido="${bet.partido}"
                                    data-partidofecha="${bet.partidoFecha}"
                                    data-pronostico="${bet.pronostico}"
                                    data-cuota="${bet.cuota}"
                                    data-monto="${bet.monto}"
                                    data-ganancia="${bet.ganancia}"
                                    data-estado="${bet.estado}">
                                    <i class="bi bi-receipt"></i> Ver Ticket
                                </button>
                            </div>
                        `;
                    }
                    
                    ticketDiv.innerHTML = `
                        <div class="d-flex justify-content-between align-items-start mb-2">
                            <div>
                                <strong class="text-neon-cyan" style="font-size:0.95rem;">Ticket: ${bet.ticketId}</strong>
                                <div class="text-white-50" style="font-size:0.75rem;"><i class="bi bi-clock"></i> Apostado el ${bet.fecha}</div>
                            </div>
                            ${statusBadge}
                        </div>
                        <div class="row g-2 mb-2" style="font-size:0.85rem;">
                            <div class="col-6">
                                <div class="text-white-50">Monto Apostado:</div>
                                <strong class="text-white">S/ ${bet.monto}</strong>
                            </div>
                            <div class="col-6">
                                <div class="text-white-50">Pago Estimado:</div>
                                <strong class="text-neon-green">S/ ${bet.ganancia} <span class="text-muted" style="font-size:0.7rem;">(${bet.cuota}x)</span></strong>
                            </div>
                        </div>
                        <div class="mb-2" style="font-size:0.85rem;">
                            <span class="badge bg-secondary py-1 px-2" style="font-size:0.75rem;">Pronóstico: ${bet.pronostico}</span>
                            <span class="text-info ms-2" style="font-size:0.75rem;"><i class="bi bi-tag-fill"></i> Props: ${bet.prop || 'Ninguno'}</span>
                        </div>
                        ${actionHtml}
                    `;
                    
                    bodyContainer.appendChild(ticketDiv);
                });
                
                treeContainer.appendChild(nodeDiv);
            });
        }
        
        function toggleTreeNode(nodeId) {
            const body = document.getElementById('body-' + nodeId);
            const caret = document.getElementById('caret-' + nodeId);
            if (body && caret) {
                if (body.classList.contains('d-none')) {
                    body.classList.remove('d-none');
                    caret.classList.replace('bi-caret-right-fill', 'bi-caret-down-fill');
                } else {
                    body.classList.add('d-none');
                    caret.classList.replace('bi-caret-down-fill', 'bi-caret-right-fill');
                }
            }
        }
    

document.addEventListener('DOMContentLoaded', function() {
    const betRows = document.querySelectorAll('.bet-row');
    const betsByMatch = {};

    betRows.forEach(row => {
        const partido = row.getAttribute('data-partido');
        const cliente = row.getAttribute('data-cliente');
        const monto = row.getAttribute('data-monto');
        const pronostico = row.getAttribute('data-pronostico');

        if (partido && cliente && monto && pronostico) {
            if (!betsByMatch[partido]) {
                betsByMatch[partido] = [];
            }
            betsByMatch[partido].push({ cliente, monto, pronostico });
        }
    });

    const editButtons = document.querySelectorAll('.bracket-node button[data-equipo1][data-equipo2]');
    editButtons.forEach(btn => {
        const equipo1 = btn.getAttribute('data-equipo1');
        const equipo2 = btn.getAttribute('data-equipo2');
        const partidoStr = equipo1 + ' vs ' + equipo2;

        if (betsByMatch[partidoStr]) {
            const betsContainer = document.createElement('div');
            betsContainer.className = 'mt-2 pt-1 border-top border-secondary text-start';
            betsContainer.style.fontSize = '0.7rem';
            
            const title = document.createElement('div');
            title.className = 'text-neon-cyan fw-bold mb-1';
            title.innerText = 'Apuestas:';
            betsContainer.appendChild(title);

            betsByMatch[partidoStr].forEach(bet => {
                const betInfo = document.createElement('div');
                betInfo.className = 'text-white-50 text-truncate';
                betInfo.title = `${bet.cliente}: $${bet.monto} (${bet.pronostico})`;
                betInfo.innerHTML = `<strong>${bet.cliente}</strong> ($${bet.monto}) - ${bet.pronostico}`;
                betsContainer.appendChild(betInfo);
            });

            const parentDiv = btn.closest('.text-center');
            if (parentDiv && parentDiv.parentNode) {
                parentDiv.parentNode.insertBefore(betsContainer, parentDiv);
            }
        }
    });
});

