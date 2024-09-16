<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@ page import="java.util.List" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    /*Ottengo dal controller la lista di tutti gli utenti*/
    List<User> allUsers = (List<User>) request.getAttribute("allUsers");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pagina degli utenti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
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

<div class="user-management">
    <table class="table-wide">
        <thead>
        <tr>
            <th class="nome-column">Nome</th>
            <th class="cognome-column">Cognome</th>
            <th class="username-column">Username</th>
            <th class="admin-column">Amministratore</th>
            <th class="actions-column">Azioni</th>
        </tr>
        </thead>
        <tbody>
        <% if (allUsers != null) {
            for (User user : allUsers) { %>
        <tr>
            <td class="nome-column"><%= user.getNome() %></td>
            <td class="cognome-column"><%= user.getCognome() %></td>
            <td class="username-column"><%= user.getUsername() %></td>
            <td class="admin-column"><%= user.isAdmin() ? "Si" : "No" %></td>
            <td class="actions-column">
                <form action="Dispatcher" method="post" style="display:inline;">
                    <input type="hidden" name="controllerAction" value="Management.deleteUser">
                    <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                    <button type="submit" style="font-family:Anek Bangla,sans-serif">Elimina Utente</button>
                </form>

                <form action="Dispatcher" method="post" style="display:inline;">
                    <input type="hidden" name="controllerAction" value="Management.makeAdmin">
                    <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                    <button type="submit" style="font-family:Anek Bangla,sans-serif">Promuovi Amministratore</button>
                </form>

                <form action="Dispatcher" method="post" style="display:inline;">
                    <input type="hidden" name="controllerAction" value="Management.removeAdmin">
                    <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                    <button type="submit" style="font-family:Anek Bangla,sans-serif">Rimuovi Amministratore</button>
                </form>
            </td>
        </tr>
        <% }} %>
        </tbody>
    </table>
</div>
</body>
</html>
