<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String dbkey = (String) request.getAttribute("dbkey");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chiave admin</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        body {
            color: #f9f9f9;
        }
    </style>
</head>
<body>

<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=Management.view">
            Tennis & Padel Store - Amministrazione Utenti
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
<div class="back-link">
    <a href="Dispatcher?controllerAction=Management.view">Torna indietro</a>
</div>
<main>

    <h2>Attuale chiave amministratore</h2>
            <span><%= dbkey %></span>
    <h2>Nuova chiave amministratore</h2>
    <form class="edit-form" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="Management.editAdminkey">
        <label for="newkey">Nuova chiave</label>
        <input type="text" name="newkey" id="newkey" required>
        <button type="submit" style="cursor: pointer">Salva</button>
        <button type="button" style="cursor: pointer">Annulla</button>
    </form>

</main>
</body>
</html>
