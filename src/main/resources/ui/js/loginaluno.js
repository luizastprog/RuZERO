const API = "http://localhost:4567/api/alunos";

async function cadastrarOuLogarAluno() {
    const nome = document.getElementById("nome").value.trim();
    const email = document.getElementById("email").value.trim();
    const campus = document.getElementById("campus").value;

    if (!nome || !email || !campus) {
        alert("Preencha todos os campos!");
        return;
    }

    const loginResponse = await fetch(`${API}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email })
    });

    if (loginResponse.ok) {
        const aluno = await loginResponse.json();
        salvarLocalStorage(aluno);
        window.location.href = "cardapioaluno.html";
        return;
    }

    const cadastroResponse = await fetch(`${API}/cadastrar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            nome,
            email,
            campus: { idCampus: Number(campus) }
        })
    });

    if (cadastroResponse.ok) {
        const aluno = await cadastroResponse.json();
        salvarLocalStorage(aluno);
        alert("Cadastro realizado com sucesso!");
        window.location.href = "cardapioaluno.html";
    } else {
        alert("Erro ao cadastrar aluno.");
    }
}

function salvarLocalStorage(aluno) {
    localStorage.setItem("alunoId", aluno.idAluno);
    localStorage.setItem("alunoNome", aluno.nome);
    localStorage.setItem("alunoEmail", aluno.email);
    localStorage.setItem("alunoCampusId", aluno.campus.idCampus);
}
