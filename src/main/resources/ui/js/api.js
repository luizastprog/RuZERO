const API_BASE_URL = "http://localhost:4567/api";

async function enviarFeedback() {

    const nota = document.querySelector('input[name="nota"]:checked');
    const comentario = document.getElementById("ruim").value;

    if (!nota || comentario === "") {
        alert("Selecione a nota e o item ruim.");
        return;
    }

    const idAluno = localStorage.getItem("alunoId");
    const idCampus = localStorage.getItem("alunoCampusId");

    if (!idAluno || !idCampus) {
        alert("Erro: dados do aluno não encontrados. Faça login novamente.");
        return;
    }

    const feedback = {
        nota: nota.value,
        comentario: comentario,
        alunoMatricula: parseInt(idAluno),
        campus: {
            idCampus: parseInt(idCampus)
        }
    };

    console.log("Enviando feedback para API:", feedback);

    try {
        const response = await fetch(`${API_BASE_URL}/feedbacks`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(feedback)
        });

        const result = await response.json();
        console.log("Resposta da API:", result);

        if (!response.ok) {
            alert("Erro ao enviar feedback: " + (result.message || "Erro desconhecido"));
            return;
        }

        alert("Avaliação enviada com sucesso!");
        window.location.href = "cardapioaluno.html";

    } catch (e) {
        console.error("Erro ao enviar feedback:", e);
        alert("Falha ao conectar com o servidor.");
    }
}
