<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Product"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Blob"%>
<%@ page import="java.util.Base64" %>

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
            for (Product product : allProducts) {
                String base64Image = null;
                if (product != null && product.getImage() != null) {
                    try {
                        Blob imageBlob = product.getImage();
                        byte[] imageData = imageBlob.getBytes(1, (int) imageBlob.length());
                        base64Image = Base64.getEncoder().encodeToString(imageData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        %>
        <tr>
            <td class="nome-column"><%= product.getNome() %></td>
            <td class="nome-column"><%= product.getDescrizione() %></td>
            <td class="prezzo-column"><%= product.getPrezzo() %></td>
            <td class="prezzo-column"><%= product.getCategoria() %></td>
            <td class="prezzo-column"><%= product.getBrand() %></td>
            <td class="prezzo-column"><%= product.getDisponibilita() %></td>
            <td class="prezzo-column"><%= product.getVetrina() ? "Si" : "No" %></td>
            <td class="immagine-column"><img src="data:image/jpeg;base64,<%= base64Image %>" alt="<%= product.getNome() %>" style="width: 130px; height: 130px;"></td>
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
            <td colspan="9">
                <form class="edit-form" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="Management.editProduct">
                    <input type="hidden" name="productId" value="<%= product.getProductid() %>">
                    <table style="width: 100%;">
                        <tr>
                            <td>Nome</td>
                            <td><input type="text" name="nome" value="<%= product.getNome() %>" required></td>
                        </tr>
                        <tr>
                            <td>Descrizione</td>
                            <td><input type="text" name="descrizione" value="<%= product.getDescrizione() %>" required></td>
                        </tr>
                        <tr>
                            <td>Prezzo</td>
                            <td><input type="text" name="prezzo" value="<%= product.getPrezzo() %>" required></td>
                        </tr>
                        <tr>
                            <td>Categoria</td>
                            <td><input type="text" name="categoria" value="<%= product.getCategoria() %>" required></td>
                        </tr>
                        <tr>
                            <td>Brand</td>
                            <td><input type="text" name="brand" value="<%= product.getBrand() %>" required></td>
                        </tr>
                        <tr>
                            <td>Disponibilita'</td>
                            <td><input type="text" name="disponibilita" value="<%= product.getDisponibilita() %>" required></td>
                        </tr>
                        <tr>
                            <td>Vetrina</td>
                            <td>
                                <!-- <input type="checkbox" name="vetrina" <%= product.getVetrina() ? "checked" : "" %>> -->
                                <select name="vetrina" id="vetrina">
                                    <option value="S" <%= product.getVetrina() ? "selected" : "" %>>Si</option>
                                    <option value="N" <%= !product.getVetrina() ? "selected" : "" %>>No</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>Immagine</td>
                            <td>
                                <img src="data:image/jpeg;base64,<%= base64Image %>" alt="<%= product.getNome() %>" style="width: 130px; height: 130px;">
                                <input type="file" name="image" accept="image/*">
                            </td>
                        </tr>
                        <tr class="form-actions">
                            <td colspan="2">
                                <button type="submit">Salva</button>
                                <button type="button" onclick="hideEditForm(<%= product.getProductid() %>)">Annulla</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        <% }} %>
        </tbody>
    </table>
</div>
</body>
</html>
