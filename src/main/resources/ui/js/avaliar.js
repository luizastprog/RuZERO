async function enviarFeedback() {

    const nota = document.querySelector('input[name="nota"]:checked')?.value;
    const comentario = document.getElementById("ruim").value;

    const idAluno = localStorage.getItem("alunoId");
    const idCampus = localStorage.getItem("alunoCampusId");

    if (!nota) {
        alert("Selecione uma nota!");
        return;
    }

    if (!idAluno || !idCampus) {
        alert("Erro: dados do aluno não encontrados. Faça login novamente.");
        return;
    }

    const feedback = {
        nota: nota,
        comentario: comentario,
        alunoMatricula: parseInt(idAluno),
        campus: {
            idCampus: parseInt(idCampus)
        }
    };

    console.log("Enviando feedback para API:", feedback);

    try {
        const response = await fetch("/api/feedbacks", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(feedback)
        });

        const body = await response.json();

        if (!response.ok) {
            console.error(body);
            alert("Erro ao enviar feedback: " + (body.message || "Erro desconhecido"));
            return;
        }

        alert("Avaliação enviada com sucesso!");
        window.location.href = "cardapioaluno.html";

    } catch (e) {
        console.error("Erro de comunicação:", e);
        alert("Erro ao conectar com o servidor.");
    }
}
