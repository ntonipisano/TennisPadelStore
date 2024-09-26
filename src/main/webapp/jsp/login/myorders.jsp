<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Order"%>
<%@ page import="java.util.List" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    List<Order> UserOrders = (List<Order>) request.getAttribute("UserOrders");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I tuoi ordini</title>
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
        <a href="Dispatcher?controllerAction=OrderController.OrdersForUser" class="buttons">I tuoi ordini</a>
        <a href="Dispatcher?controllerAction=Login.logout" class="buttons">Logout</a>
        <% } %>
        <a href="Dispatcher?controllerAction=Cart.view" class="buttons"><img
                src="${pageContext.request.contextPath}/images/carrello.png">
        </a>
    </div>
</nav>
<main>
    <h1 style="text-align: center; color: #115b9a; background-color: rgba(249,249,249,0.7)">Storico ordini</h1>
    <table class="table-wide" style="color: #f9f9f9">
        <thead>
        <tr>
            <th class="prezzo-column">N.ordine</th>
            <th class="nome-column">Nome e Cognome</th>
            <th class="prezzo-column">Totale ordine</th>
            <th class="nome-column">Stato</th>
            <th class="prezzo-column">Data ordine</th>
            <th class="nome-column">Cellulare</th>
            <th class="nome-column">Indirizzo</th>
            <th class="prezzo-column">CAP</th>
            <th class="prezzo-column">Provincia</th>
            <th class="nome-column">Citta'</th>
            <th class="prezzo-column">Metodo di pagamento</th>
        </tr>
        </thead>
        <tbody>

            <% if (UserOrders != null) {
            for (Order order : UserOrders) {
            %>

        <tr>
            <td class="prezzo-column"><%= order.getOrderId() %></td>
            <td class="nome-column"><%= order.getNomecognome() %></td>
            <td class="prezzo-column">&euro;<%= order.getCosto() %></td>
            <td class="prezzo-column"><%= order.getStato() %></td>
            <td class="prezzo-column"><%= order.getDataordine() %></td>
            <td class="prezzo-column"><%= order.getCellulare() %></td>
            <td class="prezzo-column"><%= order.getIndirizzo() %></td>
            <td class="nome-column"><%= order.getCap() %></td>
            <td class="nome-column"><%= order.getProvincia() %></td>
            <td class="nome-column"><%= order.getCitta() %></td>
            <td class="nome-column"><%= order.getMetododipagamento() %></td>

        </tr>
            <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p style="font-size: 20px;">Nessun ordine trovato</p>
    <% } %>
</main>
</body>
</html>
