<%--PAGINA DI REGISTRAZIONE--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="false"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione - Tennis & Padel Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<!-- Navbar -->
<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=HomeManagement.view">
            Tennis & Padel Store
        </a>
    </div>
</nav>

    <div class="container">
        <h1>Registrazione</h1>
        <form action="Dispatcher" method="post" id="RegistrazioneForm" nome="RegistrazioneForm">
            <input type="hidden" name="controllerAction" value="Registrazione.insert"/>
            <div class="input-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="input-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="input-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" required>
            </div>
            <div class="input-group">
                <label for="cognome">Cognome</label>
                <input type="text" id="cognome" name="cognome" required>
            </div>
            <div class="form-group">
                <button type="submit">Registrati!</button>
            </div>
        </form>
    </div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const form = document.getElementById("RegistrazioneForm");
        form.addEventListener("submit", function(event) {
            // Prevenire l'invio del modulo se la password non è valida
            if (!validatePassword()) {
                event.preventDefault(); // Blocca l'invio del modulo
            }
        });

        function validatePassword() {
            const password = document.getElementById("password").value;
            // Verifica che la password sia almeno di 6 caratteri
            if (password.length < 6) {
                alert("La password deve contenere almeno 6 caratteri.");
                return false;
            }
            return true;
        }
    });
</script>
</body>
</html>
