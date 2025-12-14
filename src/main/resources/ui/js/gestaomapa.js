const API = "/api";
let charts = {};

// ---------------- PIZZAS ----------------
async function carregarMapaSatisfacao() {
    const res = await fetch("/api/feedbacks/medias/mapa");
    const dados = await res.json();

    ["campus1", "campus2", "campus3"].forEach(limparChart);

    dados.forEach((item, index) => {
        atualizarPizza(`campus${index + 1}`, item.mediaNotas);
    });
}

function atualizarPizza(canvasId, media) {
    const satisfeito = ((media || 0) / 5) * 100;
    const insatisfeito = 100 - satisfeito;

    const ctx = document.getElementById(canvasId);

    charts[canvasId] = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: ["Satisfeito", "Insatisfeito"],
            datasets: [{
                data: [satisfeito, insatisfeito],
                backgroundColor: ["#4a68f5", "#d3dafb"]
            }]
        },
        options: {
            cutout: "65%",
            plugins: { legend: { display: false } }
        }
    });
}

function limparChart(id) {
    if (charts[id]) {
        charts[id].destroy();
        delete charts[id];
    }
}

// ---------------- GRÁFICO DE NOTAS ----------------
async function carregarGraficoNotas() {
    const idCampus = document.getElementById("selectCampus").value;

    const res = await fetch(`${API}/feedbacks/campus/${idCampus}`);
    const feedbacks = await res.json();

    const contagem = { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };

    feedbacks.forEach(f => contagem[f.nota]++);

    limparChart("graficoNotas");

    charts["graficoNotas"] = new Chart(
        document.getElementById("graficoNotas"),
        {
            type: "bar",
            data: {
                labels: ["1", "2", "3", "4", "5"],
                datasets: [{
                    data: Object.values(contagem),
                    backgroundColor: "rgba(93,118,255,0.8)",
                    borderRadius: 10
                }]
            },
            options: {
                plugins: { legend: { display: false } },
                scales: {
                    y: { beginAtZero: true, ticks: { stepSize: 1 } }
                }
            }
        }
    );
}

// ---------------- MAPA ----------------
async function carregarMapaCampus() {
    const res = await fetch("/api/feedbacks/medias/mapa");
    const dadosMapa = await res.json();

    const mapa = L.map("mapa").setView([-5.355, -49.070], 13);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png")
        .addTo(mapa);

    const campusCoords = [
        { id: 1, coord: [-5.35065, -49.09354] },
        { id: 2, coord: [-5.334914, -49.093061] },
        { id: 3, coord: [-5.36500, -49.02425] }
    ];

    campusCoords.forEach(c => {
        const info = dadosMapa.find(d => d.campus.idCampus === c.id);
        const media = info ? info.mediaNotas.toFixed(1) : "0.0";
        const nome = info ? info.campus.nome : "Campus";

        L.marker(c.coord)
            .addTo(mapa)
            .bindPopup(`<b>${nome}</b><br>Média: ${media} ⭐`);
    });
}

// ---------------- INIT ----------------
document.addEventListener("DOMContentLoaded", () => {
    carregarMapaSatisfacao();
    carregarGraficoNotas();
    carregarMapaCampus();

    document
        .getElementById("selectCampus")
        .addEventListener("change", carregarGraficoNotas);
});
