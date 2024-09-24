<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Order"%>
<%@ page import="java.util.List" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<Order> allOrders = (List<Order>) request.getAttribute("allOrders");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pagina Ordini</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=Management.view">
            Tennis & Padel Store - Amministrazione Ordini
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
<div class="user-management">
    <table class="table-wide">
        <thead>
        <tr>
            <th class="prezzo-column" style="color: #be3636;">Userid cliente</th>
            <th class="nome-column" style="color: #be3636;">Nome e cognome</th>
            <th class="prezzo-column" style="color: #be3636;">Costo</th>
            <th class="nome-column" style="color: #be3636;">Stato</th>
            <th class="prezzo-column" style="color: #be3636;">Data ordine</th>
            <th class="nome-column" style="color: #be3636;">Cellulare</th>
            <th class="nome-column" style="color: #be3636;">Indirizzo</th>
            <th class="prezzo-column"style="color: #be3636;">CAP</th>
            <th class="prezzo-column" style="color: #be3636;">Provincia</th>
            <th class="nome-column" style="color: #be3636;">Citta'</th>
            <th class="prezzo-column" style="color: #be3636;">Metodo di pagamento</th>
            <th class="actions-column" style="color: #be3636;">Modifica stato ordine </th>
        </tr>
        </thead>
        <tbody>

            <% if (allOrders != null) {
            for (Order order : allOrders) {
            %>

        <tr>
            <td class="nome-column"><%= order.getUserId() %></td>
            <td class="nome-column"><%= order.getNomecognome() %></td>
            <td class="prezzo-column"><%= order.getCosto() %></td>
            <td class="prezzo-column"><%= order.getStato() %></td>
            <td class="prezzo-column"><%= order.getDataordine() %></td>
            <td class="prezzo-column"><%= order.getCellulare() %></td>
            <td class="prezzo-column"><%= order.getIndirizzo() %></td>
            <td class="nome-column"><%= order.getCap() %></td>
            <td class="nome-column"><%= order.getProvincia() %></td>
            <td class="nome-column"><%= order.getCitta() %></td>
            <td class="nome-column"><%= order.getMetododipagamento() %></td>
            <td class="actions-column">

            <form action="Dispatcher" method="post" style="display:inline;">
                <input type="hidden" name="controllerAction" value="Management.changeOrderState">
                <input type="hidden" name="OrderId" value="<%= order.getOrderId() %>">

                <!-- Select -->
                <select name="newState">
                    <option value="Preso in carico" <%= order.getStato().equals("Preso in carico") ? "selected" : "" %>>Preso in carico</option>
                    <option value="Magazzino" <%= order.getStato().equals("Magazzino") ? "selected" : "" %>>Magazzino</option>
                    <option value="Affidato al corriere" <%= order.getStato().equals("Affidato al corriere") ? "selected" : "" %>>Affidato al corriere</option>
                    <option value="Spedito" <%= order.getStato().equals("Spedito") ? "selected" : "" %>>Spedito</option>
                    <option value="Consegnato" <%= order.getStato().equals("Consegnato") ? "selected" : "" %>>Consegnato</option>
                </select>

                <button type="submit" value="Salva">Salva</button>
                <input type="reset" value="Annulla">
            </form>
            </td>
        </tr>
                <% }} %>
        </tbody>
    </table>
</div>
</main>
</body>
</html>
