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
            display: flex;
            flex-wrap: wrap;
            background: rgba(255, 255, 255, 0.9);
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin: 20px auto;
            max-width: 1200px;
        }

        .cart-section, .checkout-section {
            flex: 1;
            min-width: 300px;
            margin: 10px;
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

        .buttons, .buttons2 {
            background-color: #52a268;
            color: white;
            padding: 8px 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            display: block;
            margin: 4px auto;
            text-align: center;
            transition: background-color 0.3s;
            font-family: 'Inter', sans-serif;
        }

        .buttons:hover {
            background-color: #72c783;
        }

        .buttons2 {
            background-color: #d97c0e;
            padding: 12px 15px;
            font-size: 16px;
        }

        .buttons2:hover {
            background-color: #218838;
        }

        .checkout-section form {
            display: flex;
            flex-direction: column;
        }

        .checkout-section label {
            margin-bottom: 5px;
            font-weight: bold;
        }

        .checkout-section input, .checkout-section select {
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }

        .payment-method {
            margin-bottom: 15px;
        }

        .payment-method input {
            margin-right: 10px;
        }

        @media (max-width: 768px) {
            .container {
                flex-direction: column;
            }
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
        <a href="Dispatcher?controllerAction=Cart.view" class="buttons"><img src="${pageContext.request.contextPath}/images/carrello.png" alt="Carrello" style="width:20px;height:20px;"></a>
    </div>
</nav>

<!-- Messaggio Applicativo -->
<% if (applicationMessage != null) { %>
<div class="alert">
    <%= applicationMessage %>
</div>
<% } %>

<div class="container">
    <!-- Sezione Carrello -->
    <div class="cart-section">
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
                <td class="price" data-prezzo="<%= product.getPrezzo() %>" style="color: black"><%=product.getPrezzo()%></td>

                <td>
                    <form action="Dispatcher" method="post" style="display:inline;">
                        <input type="hidden" name="controllerAction" value="Cart.decreaseQuantity" />
                        <input type="hidden" name="productId" value="<%= product.getProductid() %>" />
                        <button type="submit" class="buttons" style="width: 30px;">-</button>
                    </form>

                    <span class="quantity" data-quantita="<%= quantity %>"><%= quantity %></span>

                    <form action="Dispatcher" method="post" style="display:inline;">
                        <input type="hidden" name="controllerAction" value="Cart.increaseQuantity" />
                        <input type="hidden" name="productId" value="<%= product.getProductid() %>" />
                        <button type="submit" class="buttons" style="width: 30px;">+</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>

        <h2>Totale carrello: &euro;<span id="total-price">0.00</span></h2>
        <% } else { %>
        <p style="font-size: 20px; text-align: center;">Carrello vuoto!</p>
        <% } %>
    </div>

    <!-- Sezione Checkout -->
    <div class="checkout-section">
        <% if (productsAndQuantity != null && !productsAndQuantity.isEmpty()) { %>
        <h2>Dati di Spedizione</h2>
        <form action="Dispatcher" method="post">
            <input type="hidden" name="controllerAction" value="Order.create"/>

            <!-- Campi per i dati di spedizione -->
            <label for="nome">Nome e Cognome:</label>
            <input type="text" id="nome" name="nome" required />

            <label for="indirizzo">Indirizzo:</label>
            <input type="text" id="indirizzo" name="indirizzo" required />

            <label for="citta">Citta':</label>
            <input type="text" id="citta" name="citta" required />

            <label for="cap">CAP:</label>
            <input type="text" id="cap" name="cap" required />

            <label for="provincia">Provincia:</label>
            <input type="text" id="provincia" name="provincia" required />

            <label for="cell">Cellulare:</label>
            <input type="text" id="cell" name="cell" required />

            <!-- Sezione Metodo di Pagamento -->
            <h2>Metodo di Pagamento</h2>
            <div class="payment-method">
                <input type="radio" id="pagamento-consegna" name="metodoPagamento" value="Pagamento alla Consegna" required>
                <label for="pagamento-consegna">Pagamento alla Consegna (+4,90&euro;)</label>
            </div>
            <div class="payment-method">
                <input type="radio" id="bonifico" name="metodoPagamento" value="Bonifico Bancario" required>
                <label for="bonifico">Bonifico Bancario (IBAN: IT60X0542811101000000123456)</label>
            </div>

            <!-- Tasto Acquista -->
            <button type="submit" class="buttons2">Effettua l'acquisto</button>
        </form>
        <% } %>
    </div>
</div>

<!-- Script calcolo totale carrello -->
<script>
    function calculateTotal() {
        let total = 0;
        const rows = document.querySelectorAll('.cart-section table tr');

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

    //Verifica informazioni ordine
    function validateForm() {
        let valid = true;

        //Reset error messages
        document.querySelectorAll('.error-message').forEach(elem => elem.textContent = '');

        // Validazione CAP
        const cap = document.getElementById('cap').value;
        if (!/^\d{5}$/.test(cap)) {
            document.getElementById('cap-error').textContent = 'CAP deve essere un numero di 5 cifre.';
            valid = false;
        }

        // Validazione Provincia
        const provincia = document.getElementById('provincia').value;
        if (!/^[A-Za-z]{2}$/.test(provincia)) {
            document.getElementById('provincia-error').textContent = 'Provincia deve essere composta da 2 lettere.';
            valid = false;
        }

        // Validazione Cellulare
        const cellulare = document.getElementById('cell').value;
        if (!/^\d+$/.test(cellulare)) {
            document.getElementById('cellulare-error').textContent = 'Cellulare deve contenere solo numeri.';
            valid = false;
        }
        return valid;
    }
</script>

</body>
</html>
