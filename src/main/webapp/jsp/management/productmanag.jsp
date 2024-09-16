<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Product"%>
<%@ page import="java.util.List" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<Product> allProducts = (List<Product>) request.getAttribute("allProducts");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pagina Prodotti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
    <script>
        function showEditForm(productId) {
            // Mostra i campi di input e il bottone "Salva"
            document.getElementById('edit-form-' + productId).style.display = 'block';
            document.getElementById('product-info-' + productId).style.display = 'none';
        }

        function hideEditForm(productId) {
            // Nasconde i campi di input e mostra le informazioni originali
            document.getElementById('edit-form-' + productId).style.display = 'none';
            document.getElementById('product-info-' + productId).style.display = 'table-row';
        }
    </script>
</head>
<body>
<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=Management.view">
            Tennis & Padel Store - Amministrazione Catalogo
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

<div class="back-link">
    <a href="Dispatcher?controllerAction=Management.addProduct">Aggiungi Prodotto</a>
</div>

<div class="user-management">
    <table class="table-wide">
        <thead>
        <tr>
            <th class="nome-column">Nome</th>
            <th class="nome-column">Descrizione</th>
            <th class="prezzo-column">Prezzo</th>
            <th class="prezzo-column">Categoria</th>
            <th class="prezzo-column">Brand</th>
            <th class="prezzo-column">Disponibilita'</th>
            <th class="prezzo-column">Vetrina</th>
            <th class="immagine-column">Immagine</th>
            <th class="actions-column"> </th>
        </tr>
        </thead>
        <tbody>
        <% if (allProducts != null) {
            for (Product product : allProducts) { %>
        <tr>
            <td class="nome-column"><%= product.getNome() %></td>
            <td class="nome-column"><%= product.getDescrizione() %></td>
            <td class="prezzo-column"><%= product.getPrezzo() %></td>
            <td class="prezzo-column"><%= product.getCategoria() %></td>
            <td class="prezzo-column"><%= product.getBrand() %></td>
            <td class="prezzo-column"><%= product.getDisponibilita() %></td>
            <td class="prezzo-column"><%= product.getVetrina() ? "Si" : "No" %></td>
            <td class="immagine-column"><%= product.getImage() %></td>
            <td class="actions-column">
                <form action="Dispatcher" method="post" style="display:inline;">
                    <input type="hidden" name="controllerAction" value="Management.deleteProduct">
                    <input type="hidden" name="productId" value="<%= product.getProductid() %>">
                    <input type="hidden" name="productId" value="<%= product.getProductid() %>">
                    <button type="submit" style="font-family:Anek Bangla,sans-serif">Elimina Prodotto</button>
                </form>
                <!--
                <form action="Dispatcher" method="post" style="display:inline;">
                    <input type="hidden" name="controllerAction" value="Management.editProduct">
                    <input type="hidden" name="userId" value="<%= product.getProductid() %>">
                    <button type="submit" style="font-family:Anek Bangla,sans-serif">Modifica Prodotto</button>
                </form>
                -->
                <button style="font-family:Anek Bangla,sans-serif" onclick="showEditForm(<%= product.getProductid() %>)">Modifica Prodotto</button>
            </td>
        </tr>

        <!-- Form per la modifica del prodotto -->
        <tr id="edit-form-<%= product.getProductid() %>" style="display: none;">
            <form action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="Management.saveProduct">
                <input type="hidden" name="productId" value="<%= product.getProductid() %>">
                <td class="nome-column"><input type="text" name="nome" value="<%= product.getNome() %>"></td>
                <td class="nome-column"><input type="text" name="descrizione" value="<%= product.getDescrizione() %>"></td>
                <td class="prezzo-column"><input type="text" name="prezzo" value="<%= product.getPrezzo() %>"></td>
                <td class="prezzo-column"><input type="text" name="categoria" value="<%= product.getCategoria() %>"></td>
                <td class="prezzo-column"><input type="text" name="brand" value="<%= product.getBrand() %>"></td>
                <td class="prezzo-column"><input type="text" name="disponibilita" value="<%= product.getDisponibilita() %>"></td>
                <td class="prezzo-column"><input type="checkbox" name="vetrina" <%= product.getVetrina() ? "checked" : "" %>></td>
                <td class="immagine-column">Immagine non modificabile</td>
                <td class="actions-column">
                    <button type="submit">Salva</button>
                    <button type="button" onclick="hideEditForm(<%= product.getProductid() %>)">Annulla</button>
                </td>
            </form>
        </tr>

        <% }} %>
        </tbody>
    </table>
</div>
</body>
</html>
