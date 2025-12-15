// script responsável pelo envio de avaliações
// coleta nota,"comentário", aluno e campus,
// envia os dados para a API de feedback
// e trata respostas de sucesso ou erro

async function enviarFeedback() {

    const nota = document.querySelector('input[name="nota"]:checked')?.value;
    const comentario = document.getElementById("ruim").value;

    const idAluno = Number(localStorage.getItem("alunoId"));
    const idCampus = Number(localStorage.getItem("alunoCampusId"));

    if (!nota) {
        alert("Selecione uma nota!");
        return;
    }

    if (!idAluno || !idCampus) {
        alert("Sessão expirada. Faça login novamente.");
        window.location.href = "login.html";
        return;
    }

    const feedback = {
        nota: nota,
        comentario: comentario,
        idAluno: idAluno,     // ✅ CORRETO
        idCampus: idCampus   // ✅ CORRETO
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
            alert("Erro ao enviar feedback: " + (body.mensagem || "Erro desconhecido"));
            return;
        }

        alert("Avaliação enviada com sucesso!");
        window.location.href = "cardapioaluno.html";

    } catch (e) {
        console.error("Erro de comunicação:", e);
        alert("Erro ao conectar com o servidor.");
    }
}
