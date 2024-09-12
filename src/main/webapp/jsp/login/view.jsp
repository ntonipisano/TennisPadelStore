<%-- LOGIN --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";

%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=HomeManagement.view">
            Tennis & Padel Store
        </a>
    </div>

    <div class="navbar-right">
        <% if (!loggedOn) { %>
        <a href="Dispatcher?controllerAction=Login.view" class="buttons">Login</a>
        <% } else { %>
        <span>Benvenuto, <%= loggedUser.getNome() %>!</span>
        <% } %>
        <a href=Dispatcher?controllerAction=Carrello.view" class="buttons"><img src="${pageContext.request.contextPath}/images/carrello.png">
        </a>
    </div>
</nav>

<div class="container">
    <h1>Login</h1>
    <form id="loginForm" name="loginForm" action="Dispatcher" method="post">

        <!-- Campo nascosto per tipo di login (utente o admin) -->
        <input type="hidden" id="loginType" name="loginType" value="user"/>
        <!-- Campo nascosto per il Dispatcher -->
        <input type="hidden" name="controllerAction" value="Login.handleLogin"/>


        <div class="input-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="input-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div id="adminFields" style="display: none;">
            <div class="input-group">
                <label for="adminkey">Chiave segreta admin</label>
                <input type="password" id="adminkey" name="adminkey">
            </div>
        </div>
        <button type="submit">Login</button>
    </form>
    <button id="adminToggle">Admin Login</button>
    <p>Non hai un account? <a href="Dispatcher?controllerAction=Registrazione.view">Registrati!</a></p>

    <% if (applicationMessage != null) { %>
    <div style="color: #b20000;
    background-color: #fdd;
    padding: 10px;
    border-radius: 5px;
    font-weight: bold;
    text-align: center;">
        <%= applicationMessage %>
    </div>
    <% } %>

</div>
<script>
    document.getElementById('adminToggle').addEventListener('click', function() {
        var adminFields = document.getElementById('adminFields');
        var loginType = document.getElementById('loginType');

        if (adminFields.style.display === 'none') {
            adminFields.style.display = 'block';
            loginType.value = 'admin'; // Imposta il loginType come admin
        } else {
            adminFields.style.display = 'none';
            loginType.value = 'user'; // Ritorna a user
        }
    });
</script>
</body>
</html>
