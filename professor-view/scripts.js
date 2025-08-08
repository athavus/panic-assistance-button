const API_BASE = 'http://localhost:8080/api';
let professorAtual = null;
let salaAtual = null;

function mostrarAlerta(message, type = 'success') {
  const container = document.getElementById('alertContainer');
  const alert = document.createElement('div');
  alert.className = `alert alert-${type}`;
  alert.textContent = message;
  container.appendChild(alert);

  setTimeout(() => {
    alert.style.opacity = '0';
    alert.style.transition = 'opacity 0.2s ease';
    setTimeout(() => {
      if (container.contains(alert)) {
        container.removeChild(alert);
      }
    }, 200);
  }, 4000);
}

function mostrarPasso(passo) {
  document.querySelectorAll('.step').forEach(step => {
    step.classList.remove('active');
  });
  document.getElementById(`step${passo}`).classList.add('active');
}

function voltarPasso(passo) {
  mostrarPasso(passo);
}

async function loginProfessor() {
  const matricula = document.getElementById('matricula').value.trim();
  const senha = document.getElementById('senha').value;

  if (!matricula || !senha) {
    mostrarAlerta('Por favor, preencha matrícula e senha', 'error');
    return;
  }

  try {
    const response = await fetch(`${API_BASE}/professores/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ matricula, senha }),
    });

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error('Professor não encontrado');
      } else if (response.status === 401 || response.status === 400) {
        throw new Error('Matrícula ou senha incorretos');
      } else {
        throw new Error('Erro no login');
      }
    }

    professorAtual = await response.json();
    document.getElementById('professorNome').textContent = professorAtual.nome;
    await carregarSalas();
    mostrarPasso(2);
  } catch (err) {
    mostrarAlerta('Erro: ' + err.message, 'error');
  }
}


async function carregarSalas() {
  try {
    const response = await fetch(`${API_BASE}/salas`);
    const salas = await response.json();
    const select = document.getElementById('salaSelect');
    select.innerHTML = '<option value="">Selecione uma sala</option>';
    salas.forEach(sala => {
      const option = document.createElement('option');
      option.value = sala.id;
      option.textContent = `${sala.nome} - Bloco ${sala.bloco}`;
      option.dataset.nome = sala.nome;
      option.dataset.bloco = sala.bloco;
      select.appendChild(option);
    });
  } catch (err) {
    mostrarAlerta('Erro ao carregar salas: ' + err.message, 'error');
  }
}

function selecionarSala() {
  const select = document.getElementById('salaSelect');
  const salaId = select.value;
  if (!salaId) {
    mostrarAlerta('Por favor, selecione uma sala', 'error');
    return;
  }
  const selectedOption = select.options[select.selectedIndex];
  salaAtual = {
    id: salaId,
    nome: selectedOption.dataset.nome,
    bloco: selectedOption.dataset.bloco
  };
  document.getElementById('professorNome2').textContent = professorAtual.nome;
  document.getElementById('salaNome').textContent = `${salaAtual.nome} - Bloco ${salaAtual.bloco}`;
  mostrarPasso(3);
}

async function enviarAlerta(tipo) {
  if (!professorAtual || !salaAtual) {
    mostrarAlerta('Erro: Dados incompletos', 'error');
    return;
  }
  try {
    const response = await fetch(`${API_BASE}/alertas`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        salaId: parseInt(salaAtual.id),
        professorId: professorAtual.id,
        tipo: tipo
      })
    });
    if (!response.ok) throw new Error('Erro ao enviar alerta');
    const alerta = await response.json();
    const tipoTexto = tipo === 'AJUDA' ? 'Pedido de Ajuda' : 'Alerta de Acidente';
    mostrarAlerta(`${tipoTexto} enviado com sucesso! ID: ${alerta.id}`, 'success');
  } catch (err) {
    mostrarAlerta('Erro ao enviar alerta: ' + err.message, 'error');
  }
}

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('matricula').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') loginProfessor();
  });
});