<%-- CARRELLO --%>
<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Product"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.pisano.tennispadelstore.controller.Cart" %>

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
    <title>Tennis & Padel Store Carrello</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">

    <style>
        .container {
            background: rgba(255, 255, 255, 0.7);
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin: 20px auto;

        }

        h2 {
            color: #333;
            margin-bottom: 20px;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        th, td {
            padding: 12px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #007bff;
            color: white;
            font-weight: bold;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .buttons {
            background-color: #52a268;
            color: white;
            padding: 8px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 12px;
            display: block;
            margin: 4px auto;
            text-align: center;
            transition: background-color 0.3s;
            font-family: 'Inter', sans-serif;
        }

        .buttons:hover {
            background-color: #72c783; /* Colore del bottone al passaggio del mouse */
        }

        .buttons2 {
            background-color: #d97c0e;
            color: white;
            padding: 20px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 20px;
            display: block;
            margin: 10px auto;
            text-align: center;
            transition: background-color 0.3s;
            font-family: 'Inter', sans-serif;
        }

        .buttons2:hover {
            background-color: #218838; /* Colore del bottone al passaggio del mouse */
        }

    </style>
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


<div class="container" style="min-height: 219px;">
    <h2>Il tuo carrello</h2>
    <%-- Visualizza i prodotti nel carrello con la quantità --%>
    <%
        Map<Product, Integer> productsAndQuantity = (Map<Product, Integer>) request.getAttribute("productsAndQuantity");
        if (productsAndQuantity != null && !productsAndQuantity.isEmpty()) {
    %>
    <table>
        <tr>
            <th>Prodotto</th>
            <th>Prezzo Unitario</th>
            <th>Quantita'</th>

        </tr>
        <% for (Map.Entry<Product, Integer> entry : productsAndQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
        %>
        <tr>
            <td><%= product.getNome() %></td>
            <td class="price" data-prezzo="<%= product.getPrezzo() %>"> <%= product.getPrezzo() %></td>

            <td>
                <form action="Dispatcher" method="post" style="display:inline;">
                    <input type="hidden" name="controllerAction" value="Cart.decreaseQuantity" />
                    <input type="hidden" name="productId" value="<%= product.getProductid() %>" />
                    <button type="submit" class="buttons" style="width: 5px;">-</button>
                </form>

                <span class="quantity" data-quantita="<%= quantity %>"><%= quantity %></span>

                <form action="Dispatcher" method="post" style="display:inline;">
                    <input type="hidden" name="controllerAction" value="Cart.increaseQuantity" />
                    <input type="hidden" name="productId" value="<%= product.getProductid() %>" />
                    <button type="submit" class="buttons" style="width: 5px;">+</button>
                </form>
            </td>

        </tr>
        <% } %>
    </table>

    <!-- Totale generale -->
    <h2>Totale carrello: &euro;<span id="total-price">0.00</span></h2>

    <% } else { %>
    <p style="font-size: 20px">Carrello vuoto!</p>
    <% } %>

    <%
        if (productsAndQuantity != null && !productsAndQuantity.isEmpty()) {
    %>

    <!-- tasto acquista -->
    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="Order.create"/>
        <button type="submit" class="buttons2" style="margin-top: 40px">Effettua l'acquisto</button>
    </form>
    <%
        }
    %>
</div>

    <!-- Script calcolo totale carrello -->
    <script>
        function calculateTotal() {
            let total = 0;
            const rows = document.querySelectorAll('table tr');

            rows.forEach((row) => {
                const priceElement = row.querySelector('.price');
                const quantityElement = row.querySelector('.quantity');

                if (priceElement && quantityElement) {
                    const price = parseFloat(priceElement.getAttribute('data-prezzo'));
                    const quantity = parseInt(quantityElement.getAttribute('data-quantita'));

                    if (!isNaN(price) && !isNaN(quantity)) {
                        total += price * quantity;
                    }
                }
            });
            document.getElementById('total-price').textContent = total.toFixed(2);
        }
        window.onload = calculateTotal;
    </script>

</body>
</html>
