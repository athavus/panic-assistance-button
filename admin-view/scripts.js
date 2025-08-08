const API_BASE = 'http://localhost:8080/api';
let alertasAtivos = [];

// Função para mostrar alertas de feedback
function mostrarFeedback(message, type = 'success') {
    const alert = document.createElement('div');
    alert.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 0.75rem 1rem;
        border-radius: 6px;
        font-weight: 500;
        font-size: 0.875rem;
        z-index: 1001;
        border: 1px solid;
        transition: opacity 0.2s ease;
    `;
    
    if (type === 'success') {
        alert.style.background = '#064e3b';
        alert.style.color = '#d1fae5';
        alert.style.borderColor = '#065f46';
    } else {
        alert.style.background = '#7f1d1d';
        alert.style.color = '#fecaca';
        alert.style.borderColor = '#991b1b';
    }
    
    alert.textContent = message;
    document.body.appendChild(alert);
    
    setTimeout(() => {
        alert.style.opacity = '0';
        setTimeout(() => {
            if (document.body.contains(alert)) {
                document.body.removeChild(alert);
            }
        }, 200);
    }, 3000);
}

// Carregar alertas ativos
async function carregarAlertasAtivos() {
    try {
        const response = await fetch(`${API_BASE}/alertas/ativos`);
        alertasAtivos = await response.json();
        
        const container = document.getElementById('alertasAtivos');
        
        if (alertasAtivos.length === 0) {
            container.innerHTML = '<div class="empty-state">Nenhum alerta ativo no momento</div>';
            return;
        }
        
        container.innerHTML = '';
        
        alertasAtivos.forEach(alerta => {
            const alertDiv = document.createElement('div');
            alertDiv.className = `alert-item alert-${alerta.tipo.toLowerCase()}`;
            
            const dataFormatada = new Date(alerta.dataHora).toLocaleString('pt-BR');
            
            alertDiv.innerHTML = `
                <div style="display: flex; justify-content: space-between; align-items: start;">
                    <div>
                        <strong>${alerta.tipo}</strong><br>
                        <strong>Professor:</strong> ${alerta.professor.nome}<br>
                        <strong>Sala:</strong> ${alerta.sala.nome} - Bloco ${alerta.sala.bloco}<br>
                        <strong>Data/Hora:</strong> ${dataFormatada}
                    </div>
                    <div>
                        <button class="btn btn-success" onclick="resolverAlerta(${alerta.id})">
                            Resolver
                        </button>
                    </div>
                </div>
            `;

            atualizarEstatisticas();
            
            container.appendChild(alertDiv);
        });
        
    } catch (error) {
        document.getElementById('alertasAtivos').innerHTML = 
            '<div class="empty-state">Erro ao carregar alertas</div>';
        mostrarFeedback('Erro ao carregar alertas: ' + error.message, 'error');
    }
}

// Resolver alerta
async function resolverAlerta(alertaId) {
    if (!confirm('Tem certeza que deseja resolver este alerta?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/alertas/${alertaId}/resolver`, {
            method: 'PUT'
        });

        if (!response.ok) {
            throw new Error('Erro ao resolver alerta');
        }

        mostrarFeedback('Alerta resolvido com sucesso!');
        carregarAlertasAtivos();
        atualizarEstatisticas();

    } catch (error) {
        mostrarFeedback('Erro ao resolver alerta: ' + error.message, 'error');
    }
}

// Cadastrar sala
async function cadastrarSala() {
    const nome = document.getElementById('nomeSala').value.trim();
    const bloco = document.getElementById('blocoSala').value.trim();

    if (!nome || !bloco) {
        mostrarFeedback('Por favor, preencha todos os campos', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/salas`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nome, bloco })
        });

        if (!response.ok) {
            throw new Error('Erro ao cadastrar sala');
        }

        mostrarFeedback('Sala cadastrada com sucesso!');
        document.getElementById('nomeSala').value = '';
        document.getElementById('blocoSala').value = '';

    } catch (error) {
        mostrarFeedback('Erro ao cadastrar sala: ' + error.message, 'error');
    }
}

// Cadastrar professor
async function cadastrarProfessor() {
    const nome = document.getElementById('nomeProfessor').value.trim();
    const matricula = document.getElementById('matriculaProfessor').value.trim();
    const senha = document.getElementById('senhaProfessor').value;

    if (!nome || !matricula || !senha) {
        mostrarFeedback('Por favor, preencha todos os campos', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/professores`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nome, matricula, senha })
        });

        if (!response.ok) {
            throw new Error('Erro ao cadastrar professor');
        }

        mostrarFeedback('Professor cadastrado com sucesso!');
        document.getElementById('nomeProfessor').value = '';
        document.getElementById('matriculaProfessor').value = '';
        document.getElementById('senhaProfessor').value = '';

    } catch (error) {
        mostrarFeedback('Erro ao cadastrar professor: ' + error.message, 'error');
    }
}

// Função para desenhar gráfico de pizza
function desenharGraficoPizza() {
    const canvas = document.getElementById('alertChart');
    const ctx = canvas.getContext('2d');
    
    // Limpar canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // Obter dados das estatísticas
    const ajuda = parseInt(document.getElementById('totalAjuda').textContent) || 0;
    const acidente = parseInt(document.getElementById('totalAcidente').textContent) || 0;
    const total = ajuda + acidente;
    
    if (total === 0) {
        // Desenhar estado vazio
        ctx.fillStyle = '#71717a';
        ctx.font = '14px -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif';
        ctx.textAlign = 'center';
        ctx.fillText('Nenhum dado disponível', canvas.width / 2, canvas.height / 2);
        
        document.getElementById('chartAjudaPercent').textContent = '0%';
        document.getElementById('chartAcidentePercent').textContent = '0%';
        return;
    }
    
    // Calcular percentuais
    const ajudaPercent = Math.round((ajuda / total) * 100);
    const acidentePercent = Math.round((acidente / total) * 100);
    
    // Atualizar percentuais na UI
    document.getElementById('chartAjudaPercent').textContent = ajudaPercent + '%';
    document.getElementById('chartAcidentePercent').textContent = acidentePercent + '%';
    
    // Configurações do gráfico
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const radius = 70;
    
    // Calcular ângulos
    const ajudaAngle = (ajuda / total) * 2 * Math.PI;
    const acidenteAngle = (acidente / total) * 2 * Math.PI;
    
    let currentAngle = -Math.PI / 2; // Começar no topo
    
    // Desenhar fatia de Ajuda
    if (ajuda > 0) {
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + ajudaAngle);
        ctx.closePath();
        ctx.fillStyle = '#34d399';
        ctx.fill();
        currentAngle += ajudaAngle;
    }
    
    // Desenhar fatia de Acidentes
    if (acidente > 0) {
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + acidenteAngle);
        ctx.closePath();
        ctx.fillStyle = '#f87171';
        ctx.fill();
    }
    
    // Desenhar borda do gráfico
    ctx.beginPath();
    ctx.arc(centerX, centerY, radius, 0, 2 * Math.PI);
    ctx.strokeStyle = '#27272a';
    ctx.lineWidth = 2;
    ctx.stroke();
}

// Função para desenhar gráfico de barras por bloco
function desenharGraficoBarras(alertasPorBloco) {
    const container = document.getElementById('barChart');
    container.innerHTML = '';
    
    const blocos = ['A', 'B', 'C', 'D', 'E'];
    const valores = blocos.map(bloco => alertasPorBloco[bloco] || 0);
    const maxValor = Math.max(...valores, 1);
    
    blocos.forEach((bloco, index) => {
        const valor = valores[index];
        const altura = (valor / maxValor) * 120 + 3; // altura mínima de 3px
        
        const barWrapper = document.createElement('div');
        barWrapper.style.position = 'relative';
        barWrapper.style.display = 'flex';
        barWrapper.style.alignItems = 'end';
        barWrapper.style.height = '150px';
        
        const bar = document.createElement('div');
        bar.className = 'bar';
        bar.style.height = altura + 'px';
        
        const label = document.createElement('div');
        label.className = 'bar-label';
        label.textContent = bloco;
        
        const valueLabel = document.createElement('div');
        valueLabel.className = 'bar-value';
        valueLabel.textContent = valor;
        
        bar.appendChild(label);
        bar.appendChild(valueLabel);
        barWrapper.appendChild(bar);
        container.appendChild(barWrapper);
    });
}

// Atualizar estatísticas
async function atualizarEstatisticas() {
    try {
        const response = await fetch(`${API_BASE}/alertas`);
        const todoAlertas = await response.json();

        // Totais atuais
        const total = todoAlertas.length;
        const ajuda = todoAlertas.filter(a => a.tipo === 'AJUDA').length;
        const acidente = todoAlertas.filter(a => a.tipo === 'ACIDENTE').length;

        document.getElementById('totalAlertas').textContent = total;
        document.getElementById('totalAjuda').textContent = ajuda;
        document.getElementById('totalAcidente').textContent = acidente;

        // Desenhar gráfico de pizza
        desenharGraficoPizza();

        // --- Alertas ativos por bloco (tabela) ---
        const alertasAtivos = todoAlertas.filter(a => a.ativo);
        const alertasAtivosPorBloco = {};
        alertasAtivos.forEach(alerta => {
            const bloco = alerta.sala?.bloco || 'Desconhecido';
            alertasAtivosPorBloco[bloco] = (alertasAtivosPorBloco[bloco] || 0) + 1;
        });

        const blocosAlvo = ['A', 'B', 'C', 'D', 'E'];
        const tbodyPorBloco = document.getElementById('alertasPorBloco');
        tbodyPorBloco.innerHTML = '';

        blocosAlvo.forEach(bloco => {
            const count = alertasAtivosPorBloco[bloco] || 0;
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>Bloco ${bloco}</td>
                <td>${count}</td>
            `;
            tbodyPorBloco.appendChild(tr);
        });

        // --- Gráfico de barras com todos os alertas por bloco ---
        const todoAlertasPorBloco = {};
        todoAlertas.forEach(alerta => {
            const bloco = alerta.sala?.bloco || 'Desconhecido';
            todoAlertasPorBloco[bloco] = (todoAlertasPorBloco[bloco] || 0) + 1;
        });
        
        desenharGraficoBarras(todoAlertasPorBloco);

        // --- Último alerta recebido ---
        if (todoAlertas.length === 0) {
            document.getElementById('ultimoAlerta').textContent = 'Nenhum alerta recebido ainda.';
        } else {
            const ultimo = todoAlertas
                .slice()
                .sort((a, b) => new Date(b.dataHora) - new Date(a.dataHora))[0];

            const horario = new Date(ultimo.dataHora).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
            const tipo = ultimo.tipo.toUpperCase();
            const sala = ultimo.sala.nome;
            const bloco = ultimo.sala.bloco;

            const icone = tipo === 'AJUDA' ? '🟢' : tipo === 'ACIDENTE' ? '🔴' : '';

            document.getElementById('ultimoAlerta').textContent = `${icone} ${horario} - ${tipo.charAt(0) + tipo.slice(1).toLowerCase()} - ${sala} - Bloco ${bloco}`;
        }

        // --- Alerta mais antigo pendente ---
        if (alertasAtivos.length === 0) {
            document.getElementById('alertaMaisAntigo').textContent = 'Nenhum alerta pendente no momento.';
        } else {
            const maisAntigo = alertasAtivos
                .slice()
                .sort((a, b) => new Date(a.dataHora) - new Date(b.dataHora))[0];

            const horario = new Date(maisAntigo.dataHora).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
            const tipo = maisAntigo.tipo.toUpperCase();
            const sala = maisAntigo.sala.nome;
            const bloco = maisAntigo.sala.bloco;

            // Calcular tempo pendente
            const agora = new Date();
            const dataAlerta = new Date(maisAntigo.dataHora);
            const diffMinutos = Math.floor((agora - dataAlerta) / (1000 * 60));
            
            let tempoPendente = '';
            if (diffMinutos < 60) {
                tempoPendente = `${diffMinutos}min`;
            } else {
                const horas = Math.floor(diffMinutos / 60);
                const mins = diffMinutos % 60;
                tempoPendente = mins > 0 ? `${horas}h ${mins}min` : `${horas}h`;
            }

            const icone = tipo === 'AJUDA' ? '🟡' : tipo === 'ACIDENTE' ? '🔴' : '';

            document.getElementById('alertaMaisAntigo').textContent = `${icone} ${horario} - ${tipo.charAt(0) + tipo.slice(1).toLowerCase()} - ${sala} - Bloco ${bloco} (${tempoPendente} pendente)`;
        }

    } catch (error) {
        mostrarFeedback('Erro ao carregar estatísticas: ' + error.message, 'error');
    }
}

// Carregar histórico
async function carregarHistorico() {
    try {
        const response = await fetch(`${API_BASE}/alertas`);
        const todoAlertas = await response.json();

        const container = document.getElementById('historicoAlertas');

        if (todoAlertas.length === 0) {
            container.innerHTML = '<div class="empty-state">Nenhum alerta registrado</div>';
            return;
        }

        // Ordenar por data (mais recente primeiro)
        todoAlertas.sort((a, b) => new Date(b.dataHora) - new Date(a.dataHora));

        let tableHTML = `
            <table class="table">
                <thead>
                    <tr>
                        <th>Tipo</th>
                        <th>Professor</th>
                        <th>Sala</th>
                        <th>Data/Hora</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
        `;

        todoAlertas.forEach(alerta => {
            const statusIcon = alerta.ativo ? 'Ativo' : 'Resolvido';
            const dataFormatada = new Date(alerta.dataHora).toLocaleString('pt-BR');

            tableHTML += `
                <tr>
                    <td>${alerta.tipo}</td>
                    <td>${alerta.professor.nome}</td>
                    <td>${alerta.sala.nome} - Bloco ${alerta.sala.bloco}</td>
                    <td>${dataFormatada}</td>
                    <td>${statusIcon}</td>
                </tr>
            `;
        });

        tableHTML += '</tbody></table>';
        container.innerHTML = tableHTML;

    } catch (error) {
        document.getElementById('historicoAlertas').innerHTML = 
            '<div class="empty-state">Erro ao carregar histórico</div>';
        mostrarFeedback('Erro ao carregar histórico: ' + error.message, 'error');
    }
}

// Limpar histórico (apenas visual)
function limparHistorico() {
    document.getElementById('historicoAlertas').innerHTML = 
        '<div class="empty-state">Clique em "Carregar Histórico" para ver todos os alertas</div>';
}

function acessarHIFPB() {
    window.open("https://horarios.ifpb.edu.br/campina/", "_blank");
}

// Auto-refresh dos alertas ativos a cada 1 segundo
setInterval(carregarAlertasAtivos, 1000);

// Inicializar página
document.addEventListener('DOMContentLoaded', function() {
    carregarAlertasAtivos();
    atualizarEstatisticas();
    
    // Redesenhar gráficos quando a janela redimensionar
    window.addEventListener('resize', () => {
        setTimeout(() => {
            desenharGraficoPizza();
            atualizarEstatisticas(); // para redesenhar as barras também
        }, 100);
    });
});