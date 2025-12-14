console.log("Carregando cardápio do aluno...");

function formatar(data) {
    return data.toLocaleDateString("pt-BR", {
        day: "2-digit",
        month: "2-digit"
    });
}

function gerarDatasSemana() {
    const hoje = new Date();
    const diaSemana = hoje.getDay(); // 0 = domingo
    const offset = diaSemana === 0 ? -6 : 1 - diaSemana; // levar até segunda

    const segunda = new Date();
    segunda.setDate(hoje.getDate() + offset);

    const ths = document.querySelectorAll("thead th");

    for (let i = 0; i < 5; i++) {
        const dia = new Date(segunda);
        dia.setDate(segunda.getDate() + i);
        ths[i + 1].textContent = formatar(dia);
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

async function buscarCardapioPorData(dataISO) {
    const resp = await fetch(`/api/cardapios/data/${dataISO}`);

    if (!resp.ok) return null;
    return await resp.json();
}

async function buscarItens(idCardapio) {
    const resp = await fetch(`/api/itens-cardapio/${idCardapio}`);
    if (!resp.ok) return [];
    return await resp.json();
}

async function carregarCardapioAluno() {
    const linhas = document.querySelectorAll("tbody tr");

    for (let i = 0; i < datasISO.length; i++) {

        const data = datasISO[i];

        console.log("Buscando cardápio da data:", data);

        const card = await buscarCardapioPorData(data);

        if (!card) {
            console.warn("Nenhum cardápio encontrado para", data);
            continue;
        }

        const itens = await buscarItens(card.idCardapio);
        console.log("Itens encontrados:", itens);

        itens.forEach(item => {
            const index =
                item.tipoItem === "PRATO PRINCIPAL" ? 0 :
                    item.tipoItem === "ACOMPANHAMENTO" ? 1 :
                        item.tipoItem === "GUARNIÇÃO" ? 2 :
                            item.tipoItem === "SALADA" ? 3 :
                                4;

            linhas[index].children[i + 1].textContent = item.nome;
        });
    }
}

carregarCardapioAluno();
