<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String orderid = (String) request.getAttribute("orderid");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conferma Ordine</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=HomeManagement.view">
            Tennis & Padel Store
        </a>
    </div>

    <div class="search-bar">
        <form action="Dispatcher" method="post">
            <input type="hidden" name="controllerAction" value="Shop.view"/>
            <button type="submit" class="buttons">Vai allo Shop</button>
        </form>
    </div>

    <div class="navbar-right">
        <% if (!loggedOn) { %>
        <a href="Dispatcher?controllerAction=Login.view" class="buttons">Login</a>
        <% } else { %>
        <span>Benvenuto/a, <%= loggedUser.getNome() %>!</span>
        <a href="Dispatcher?controllerAction=Login.logout" class="buttons">Logout</a>
        <% } %>
        <a href="Dispatcher?controllerAction=Cart.view" class="buttons"><img src="${pageContext.request.contextPath}/images/carrello.png">
        </a>
    </div>
</nav>
<!-- Messaggio Applicativo -->
<% if (applicationMessage != null) { %>
<div class="alert">
    <%= applicationMessage %>
</div>
<% } %>
<main>
<div class="container">
    <h1> Ordine confermato! </h1>
    <h2> Abbiamo ricevuto il tuo ordine</h2>
    <h3>Appena sara' spedito riceverai un SMS al numero di cellulare indicato con la data di consegna stimata</h3>
    <h4>Numero ordine: <%=orderid%> </h4>
    <span> Grazie per aver scelto Tennis & Padel Store! </span>
</div>
</main>
</body>
</html>
