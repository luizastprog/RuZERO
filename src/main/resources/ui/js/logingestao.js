document.getElementById("formLoginGestao").addEventListener("submit", fazerLoginGestao);

async function fazerLoginGestao(event) {
    event.preventDefault();

    const login = document.getElementById("login").value;
    const senha = document.getElementById("senha").value;

    const resposta = await fetch("http://localhost:4567/api/gestao/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ login, senha })
    });

    if (resposta.ok) {
        const gestor = await resposta.json();
        console.log("Login OK:", gestor);
        window.location.href = "gestaomapa.html";
    } else {
        alert("Login ou senha incorretos!");
    }
}
