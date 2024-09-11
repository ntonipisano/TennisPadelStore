<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@ page import="java.util.List" %>

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
    <form id="loginForm" action="Dispatcher" method="post">

        <!-- Campo nascosto per tipo di login (utente o admin) -->
        <input type="hidden" id="loginType" name="loginType" value="user"/>
        <!-- Campo nascosto per il controller -->
        <input type="hidden" name="controllerAction" value="Login.login"/>

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
                <input type="text" id="adminkey" name="adminkey">
            </div>
        </div>
        <button type="submit">Login</button>
    </form>
    <button id="adminToggle">Admin Login</button>
    <p>Non hai un account? <a href="Dispatcher?controllerAction=Registrazione.view">Registrati!</a></p>
</div>
<script>
    document.getElementById('adminToggle').addEventListener('click', function() {
        var adminFields = document.getElementById('adminFields');
        if (adminFields.style.display === 'none') {
            adminFields.style.display = 'block';
        } else {
            adminFields.style.display = 'none';
        }
    });
</script>
</body>
</html>
