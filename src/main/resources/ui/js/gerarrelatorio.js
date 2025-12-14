const API = "/api";

let graficoHorario = null;
let graficoItens = null;
let graficoNotasRel = null;

const selectCampus = document.getElementById("selectCampus");
const dataEscolhida = document.getElementById("dataEscolhida");
const inputData = document.getElementById("inputData");
const popup = document.getElementById("popupCalendario");


document.getElementById("btnCalendario").onclick = () =>
    popup.classList.remove("escondido");

document.getElementById("fecharPopup").onclick = () => {
    popup.classList.add("escondido");

    if (inputData.value) {
        const p = inputData.value.split("-");
        dataEscolhida.textContent = `${p[2]}/${p[1]}/${p[0]}`;
    } else {
        dataEscolhida.textContent = "Nenhuma data selecionada";
    }

    carregarRelatorio();
};

async function carregarRelatorio() {
    const campusNome = selectCampus.value;
    const idCampus =
        campusNome === "Campus I" ? 1 :
            campusNome === "Campus II" ? 2 : 3;

    const res = await fetch(`${API}/feedbacks/campus/${idCampus}`);
    let feedbacks = await res.json();

    // Filtrar por data
    if (inputData.value) {
        const filtro = inputData.value;

        feedbacks = feedbacks.filter(f => {
            if (!f.dataHora) return false;

            const raw = String(f.dataHora);
            const datePart = raw.includes("T")
                ? raw.split("T")[0]
                : raw.split(" ")[0];

            return datePart === filtro;
        });
    }

    gerarGraficoHorarios(feedbacks);
    gerarGraficoItens(feedbacks);
    gerarGraficoNotas(feedbacks);
}

function gerarGraficoHorarios(lista) {
    const horas = {};

    lista.forEach(f => {
        const h = new Date(f.dataHora).getHours();
        const nota = Number(f.nota);

        if (!horas[h]) horas[h] = { soma: 0, qtd: 0 };
        horas[h].soma += nota;
        horas[h].qtd++;
    });

    const labels = Object.keys(horas).sort((a, b) => a - b);
    const medias = labels.map(h => Math.round(horas[h].soma / horas[h].qtd));

    if (graficoHorario) graficoHorario.destroy();

    graficoHorario = new Chart(document.getElementById("graficoHorario"), {
        type: "line",
        data: {
            labels,
            datasets: [{
                label: "Nota média por horário",
                data: medias,
                borderWidth: 3,
                borderColor: "#3f51ff",
                pointBackgroundColor: "#3f51ff"
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: false,
                    min: 1,
                    max: 5,
                    ticks: {
                        stepSize: 1,
                        callback: v => Number.isInteger(v) ? v : ""
                    }
                }
            }
        }
    });
}

function gerarGraficoItens(lista) {

    const contagem = {};

    lista.forEach(f => {
        const item = f.comentario?.trim() || "Outro";
        if (!contagem[item]) contagem[item] = 0;
        contagem[item]++;
    });

    const labels = Object.keys(contagem);
    const valores = Object.values(contagem);

    const cores = labels.map((_, i) =>
        `hsl(${(i * 60) % 360}, 70%, 60%)`
    );

    if (graficoItens) graficoItens.destroy();

    graficoItens = new Chart(document.getElementById("graficoPizza"), {
        type: "pie",
        data: {
            labels,
            datasets: [{
                data: valores,
                backgroundColor: cores,
                borderColor: "#ffffff",
                borderWidth: 2
            }]
        },
        options: {
            plugins: {
                legend: { position: "top" }
            }
        }
    });
}

function gerarGraficoNotas(lista) {
    const contagem = { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };

    lista.forEach(f => {
        const nota = Number(f.nota);
        if (contagem[nota] !== undefined) contagem[nota]++;
    });

    const labels = ["1", "2", "3", "4", "5"];
    const valores = labels.map(n => contagem[n]);

    if (graficoNotasRel) graficoNotasRel.destroy();

    graficoNotasRel = new Chart(document.getElementById("graficoNotas"), {
        type: "bar",
        data: {
            labels,
            datasets: [{
                label: "Quantidade de notas",
                data: valores,
                backgroundColor: "rgba(93,118,255,0.8)",
                borderRadius: 10
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    precision: 0,
                    ticks: {
                        stepSize: 1,
                        callback: v => Number.isInteger(v) ? v : ""
                    }
                }
            }
        }
    });
}

function limparGraficos() {
    if (graficoHorario) graficoHorario.destroy();
    if (graficoItens) graficoItens.destroy();
    if (graficoNotasRel) graficoNotasRel.destroy();
}

selectCampus.addEventListener("change", carregarRelatorio);
document.addEventListener("DOMContentLoaded", carregarRelatorio);
