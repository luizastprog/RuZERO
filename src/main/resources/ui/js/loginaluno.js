// script responsável pelo login e cadastro automático do aluno
// primeiro tenta realizar login pelo email
// se não encontrar o aluno, realiza o cadastro
// armazena os dados do aluno para uso posterior

const API = "http://localhost:4567/api/alunos";

async function cadastrarOuLogarAluno() {

    const nome = document.getElementById("nome").value.trim();
    const email = document.getElementById("email").value.trim();
    const campus = document.getElementById("campus").value;

    if (!email) {
        alert("Informe o email!");
        return;
    }

    try {
        const loginResp = await fetch(`${API}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email })
        });

        if (loginResp.ok) {
            const aluno = await loginResp.json();
            salvarLocalStorage(aluno);
            window.location.href = "cardapioaluno.html";
            return;
        }

        if (!nome || !campus) {
            alert("Aluno não encontrado. Preencha nome e campus para cadastro.");
            return;
        }

        const cadastroResp = await fetch(`${API}/cadastrar`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                nome: nome,
                email: email,
                idCampus: Number(campus)   // ✅ CORRETO
            })
        });

        if (!cadastroResp.ok) {
            const erro = await cadastroResp.json();
            alert(erro.mensagem || "Erro ao cadastrar aluno.");
            return;
        }

        const aluno = await cadastroResp.json();
        salvarLocalStorage(aluno);
        alert("Cadastro realizado com sucesso!");
        window.location.href = "cardapioaluno.html";

    } catch (erro) {
        console.error("Erro no login/cadastro:", erro);
        alert("Erro de conexão com o servidor.");
    }
}

function salvarLocalStorage(aluno) {
    localStorage.setItem("alunoId", aluno.idAluno);
    localStorage.setItem("alunoNome", aluno.nome);
    localStorage.setItem("alunoEmail", aluno.email);
    localStorage.setItem("alunoCampusId", aluno.idCampus); // ✅ CORRETO
}
