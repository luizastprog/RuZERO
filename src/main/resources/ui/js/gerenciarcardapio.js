// script responsável pela lógica de gerenciamento do cardápio
// cria o cardápio, adiciona itens e envia os dados
// para o back-end através da API

console.log("Inicializando gerenciar cardápio...");

function formatar(data) {
    return data.toLocaleDateString("pt-BR", { day: "2-digit", month: "2-digit" });
}

function gerarDatasSemana() {
    const hoje = new Date();
    let diaSemana = hoje.getDay(); // 0 domingo

    const offset = diaSemana === 0 ? -6 : 1 - diaSemana;

    const segunda = new Date();
    segunda.setDate(hoje.getDate() + offset);

    for (let i = 0; i < 5; i++) {
        const dia = new Date(segunda);
        dia.setDate(segunda.getDate() + i);
        document.getElementById("d" + (i + 1)).textContent = formatar(dia);
    }

    return Array.from({ length: 5 }).map((_, i) => {
        const d = new Date(segunda);
        d.setDate(segunda.getDate() + i);
        const ano = d.getFullYear();
        const mes = String(d.getMonth() + 1).padStart(2, "0");
        const dia = String(d.getDate()).padStart(2, "0");
        return `${ano}-${mes}-${dia}`;
    });
}

const datasISO = gerarDatasSemana();

async function buscarCardapioPorData(isoData) {
    try {
        const resp = await fetch(`/api/cardapios/data/${isoData}`);
        if (resp.ok) return await resp.json();
        return null;
    } catch (err) {
        console.error("Erro ao buscar cardápio:", err);
        return null;
    }
}

async function criarCardapio(isoData) {
    const body = {
        data: isoData,
        gestao: { idGestao: 1 }
    };

    const resp = await fetch("/api/cardapios", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    if (!resp.ok) {
        const erro = await resp.text();
        console.error("ERRO AO CRIAR CARDÁPIO:", erro);
        throw new Error(erro);
    }

    return await resp.json();
}

async function criarItem(idCardapio, tipoItem, nome) {
    const resp = await fetch("/api/itens-cardapio", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ idCardapio, tipoItem, nome })
    });

    if (!resp.ok) {
        const erro = await resp.text();
        throw new Error("Erro criar item: " + erro);
    }
}

async function salvarAlteracoes() {
    console.log("Salvar alterações iniciado...");

    const linhas = document.querySelectorAll("tbody tr");
    const tipos = ["PRATO PRINCIPAL", "ACOMPANHAMENTO", "GUARNIÇÃO", "SALADA", "SOBREMESA"];

    for (let d = 0; d < 5; d++) {

        const dataISO = datasISO[d];
        console.log("Processando data:", dataISO);

        let cardapio = await buscarCardapioPorData(dataISO);

        if (!cardapio) {
            console.log("Criando cardápio:", dataISO);
            try {
                cardapio = await criarCardapio(dataISO);
            } catch (err) {
                alert("Erro ao criar cardápio!");
                return;
            }
        }

        const idCardapio = cardapio.idCardapio;

        for (let linha = 0; linha < linhas.length; linha++) {
            const input = linhas[linha].children[d + 1].querySelector("input");
            const nome = input.value.trim();

            if (nome === "") continue;

            try {
                await criarItem(idCardapio, tipos[linha], nome);
                console.log(`Item salvo (${tipos[linha]}):`, nome);
            } catch (err) {
                console.error(err);
                alert("Erro ao salvar item.");
                return;
            }
        }
    }

    alert("Cardápio salvo com sucesso!");
}

document.querySelector(".confirmar").addEventListener("click", salvarAlteracoes);

console.log("JS carregado com sucesso!");
