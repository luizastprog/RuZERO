const btnCalendario = document.getElementById("btnCalendario");
const popupCalendario = document.getElementById("popupCalendario");
const fecharPopup = document.getElementById("fecharPopup");
const inputData = document.getElementById("inputData");
const dataEscolhida = document.getElementById("dataEscolhida");

btnCalendario.onclick = () => {
    popupCalendario.classList.toggle("escondido");
};

fecharPopup.onclick = () => {
    popupCalendario.classList.add("escondido");
};

inputData.onchange = () => {
    const data = new Date(inputData.value);
    dataEscolhida.textContent = "Data selecionada: " + data.toLocaleDateString("pt-BR");
};
new Chart(document.getElementById("graficoHorario"), {
    type: 'line',
    data: {
        labels: ["10h", "11h", "12h", "13h", "14h"],
        datasets: [{
            label: "Insatisfação",
            data: [1, 3, 6, 2, 4],
            borderColor: "#4a5df5",
            borderWidth: 3,
            tension: 0.4
        }]
    },
    options: { plugins: { legend: { display: false } } }
});

new Chart(document.getElementById("graficoPizza"), {
    type: "pie",
    data: {
        labels: ["Proteína", "Salada", "Guarnição", "Acompanhamento"],
        datasets: [{
            data: [30, 15, 20, 35],
            backgroundColor: ["#162447", "#ff8c00", "#ff00dd", "#324eff"]
        }]
    }
});

new Chart(document.getElementById("graficoNotas"), {
    type: "bar",
    data: {
        labels: ["01", "02", "03", "04", "05"],
        datasets: [{
            data: [4, 2, 5, 3, 2],
            backgroundColor: "#4a68f5"
        }]
    },
    options: { plugins: { legend: { display: false } } }
});
