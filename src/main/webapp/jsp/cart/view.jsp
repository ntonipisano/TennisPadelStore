<%-- CARRELLO --%>
<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Product"%>
<%@ page import="java.util.Map" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis & Padel Store Carrello</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cart.css">
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
        <a href="Dispatcher?controllerAction=Cart.view" class="buttons"><img src="${pageContext.request.contextPath}/images/carrello.png" alt="Carrello" style="width:20px;height:20px;"></a>
    </div>
</nav>

<main>
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
        <form action="Dispatcher" method="post" id="checkout-form">
            <input type="hidden" name="controllerAction" value="OrderController.createOrder"/>


            <!-- Campi per i dati di spedizione -->
            <label for="nome">Nome e Cognome</label>
            <input type="text" id="nome" name="nome" pattern="[A-Za-z\s]+" title="Inserisci: nome spazio cognome" required/>

            <label for="indirizzo">Indirizzo e civico</label>
            <input type="text" id="indirizzo" name="indirizzo" pattern="[A-Za-z0-9\s]+" title="Inserisci un indirizzo valido" required/>

            <label for="citta">Citta'</label>
            <input type="text" id="citta" name="citta" pattern="[A-Za-z\s]+" title="Solo lettere sono ammesse" required />

            <label for="cap">CAP</label>
            <input type="text" id="cap" name="cap" pattern="\d{5}" title="Inserisci un cap valido" required />

            <label for="provincia">Provincia</label>
            <input type="text" id="provincia" name="provincia" pattern="[A-Za-z]{2}" title="Inserisci la sigla di una provincia (2 caratteri)" required />

            <label for="cell">Cellulare</label>
            <input type="text" id="cell" name="cell" pattern="\d{10}" title="Inserisci un numero di telefono italiano (10 cifre)" required />

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
            <input type="hidden" name="totalPrice" id="totalPrice"/>
            <button type="submit" class="buttons2">Effettua l'acquisto</button>
        </form>
        <% } %>
    </div>
</div>
</main>
<!-- Script calcolo totale carrello -->
<script>
    function calculateTotal() {
        let total = 0;
        //Per ogni elemento del carrello estraggo il prezzo e la quantità
        const rows = document.querySelectorAll('.cart-section table tr');

        rows.forEach((row) => {
            const priceElement = row.querySelector('.price');
            const quantityElement = row.querySelector('.quantity');

            if (priceElement && quantityElement) {
                const price = parseFloat(priceElement.getAttribute('data-prezzo'));
                const quantity = parseInt(quantityElement.getAttribute('data-quantita'));

                //Verifico se sono dei numeri
                if (!isNaN(price) && !isNaN(quantity)) {
                    total += price * quantity;
                }
            }
        });
        document.getElementById('total-price').textContent = total.toFixed(2);
        //Setto il campo nascosto da inviare al server
        const totalPriceInput = document.getElementById('totalPrice');
        totalPriceInput.value = total.toFixed(2);
    }
    //La funzione viene eseguita auto quando la pag viene caricata
    window.onload = calculateTotal;

    </script>

</body>
</html>
