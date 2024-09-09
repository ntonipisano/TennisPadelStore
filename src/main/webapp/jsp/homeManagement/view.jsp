<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Tennis & Padel Shop</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-image: url(/images/background.png); /* Sostituisci con il percorso reale dell'immagine */
            background-size: cover;
            background-position: center;
            margin: 0;
            padding: 0;
        }
        header {
            text-align: center;
            padding: 20px;
            background-color: rgba(0, 0, 0, 0.7);
            color: white;
        }
        main {
            max-width: 1200px;
            margin: 20px auto;
            background-color: rgba(255, 255, 255, 0.9);
            padding: 20px;
            border-radius: 10px;
        }
        h1, h2 {
            text-align: center;
            color: #333;
        }
        form {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 20px 0;
        }
        label, input {
            margin: 5px;
            font-size: 1em;
        }
        input[type="text"], input[type="password"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 250px;
        }
        input[type="submit"] {
            background-color: #007BFF;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        section {
            margin: 30px 0;
        }
        ul {
            list-style: none;
            padding: 0;
        }
        li {
            background-color: #f9f9f9;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        footer {
            text-align: center;
            padding: 20px;
            background-color: rgba(0, 0, 0, 0.7);
            color: white;
        }
        .hidden {
            display: none;
        }
    </style>
    <script>
        function toggleAdminLogin() {
            var adminSection = document.getElementById("adminSection");
            var userSection = document.getElementById("userSection");
            if (adminSection.classList.contains("hidden")) {
                adminSection.classList.remove("hidden");
                userSection.classList.add("hidden");
            } else {
                adminSection.classList.add("hidden");
                userSection.classList.remove("hidden");
            }
        }

        function validateForm() {
            var username = document.getElementById("username").value;
            var password = document.getElementById("password").value;
            if (username == "" || password == "") {
                alert("Entrambi i campi Username e Password devono essere riempiti.");
                return false;
            }
            return true;
        }

        function validateAdminForm() {
            var adminUsername = document.getElementById("adminUsername").value;
            var adminPassword = document.getElementById("adminPassword").value;
            var adminkey = document.getElementById("adminkey").value;
            if (adminUsername == "" || adminPassword == "" || adminkey == "") {
                alert("Tutti i campi sono obbligatori per l'accesso amministratore.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<header>
    <h1>Benvenuto nel nostro negozio di Tennis e Padel</h1>
    <button onclick="toggleAdminLogin()">Switch Login Utente/Amministratore</button>
</header>

<main>
    <!-- Sezione Login Utente -->
    <section id="userSection">
        <h2>Login Utente</h2>
        <form action="Dispatcher" method="post" onsubmit="return validateForm()">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <input type="submit" name="action" value="Login">
        </form>
    </section>

    <!-- Sezione Login Amministratore -->
    <section id="adminSection" class="hidden">
        <h2>Login Amministratore</h2>
        <form action="Dispatcher" method="post" onsubmit="return validateAdminForm()">
            <label for="adminUsername">Username:</label>
            <input type="text" id="adminUsername" name="adminUsername" required>
            <label for="adminPassword">Password:</label>
            <input type="password" id="adminPassword" name="adminPassword" required>
            <label for="adminkey">Chiave segreta:</label>
            <input type="password" id="adminkey" name="adminkey" required>
            <input type="submit" name="action" value="AdminLogin">
        </form>
    </section>

    <!-- Sezione Catalogo Prodotti -->
    <section>
        <h2>Prodotti in Catalogo</h2>
        <ul>
            <c:forEach var="product" items="${products}">
                <li>
                    <strong>${product.nome}</strong> - ${product.descrizione}<br>
                    Prezzo: €${product.prezzo}<br>
                    <img src="path/to/product/image/${product.productid}" alt="${product.nome}" style="width: 100px; height: auto;">
                </li>
            </c:forEach>
        </ul>
    </section>

    <!-- Sezione Prodotti in Vetrina -->
    <section>
        <h2>Prodotti in Vetrina</h2>
        <ul>
            <c:forEach var="product" items="${featuredProducts}">
                <li>
                    <strong>${product.nome}</strong> - ${product.descrizione}<br>
                    Prezzo: €${product.prezzo}<br>
                    <img src="path/to/product/image/${product.productid}" alt="${product.nome}" style="width: 100px; height: auto;">
                </li>
            </c:forEach>
        </ul>
    </section>
</main>

<footer>
    <p>&copy; 2024 Tennis & Padel Shop. Tutti i diritti riservati.</p>
</footer>
</body>
</html>
