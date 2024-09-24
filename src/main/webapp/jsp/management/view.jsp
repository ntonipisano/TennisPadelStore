<%--PAGINA ADMIN SITO--%>
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
    <title>Pagina amministratori</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=Management.view">
            Tennis & Padel Store - Amministrazione
        </a>
    </div>

    <div class="navbar-right">
        <% if (!loggedOn) { %>
        <a href="Dispatcher?controllerAction=Login.view" class="buttons">Login</a>
        <% } else { %>
        <span>Benvenuto/a, <%= loggedUser.getNome() %>!</span>
        <a href="Dispatcher?controllerAction=Login.logout" class="buttons">Logout</a>
        <% } %>
    </div>
</nav>
<main>
<div class="admin-container">
    <!-- Gestione Utenti -->
    <div class="admin-section">
        <a href="Dispatcher?controllerAction=Management.usermanag">
        <h2><button class="start-button">Gestione Utenti</button></h2>
        </a>
    </div>

    <!-- Gestione Catalogo -->
    <div class="admin-section">
        <a href="Dispatcher?controllerAction=Management.productmanag">
        <h2><button class="start-button">Gestione Catalogo</button></h2>
        </a>
    </div>

    <!-- Gestione Ordini -->
    <div class="admin-section">
        <a href="Dispatcher?controllerAction=Management.ordermanag">
        <h2><button class="start-button">Gestione Ordini</button></h2>
        </a>
    </div>
</div>
</main>
</body>
</html>
