<%-- PAGINA SINGOLO PRODOTTO --%>
<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Product"%>
<%@ page import="java.sql.Blob"%>
<%@ page import="java.util.Base64" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    Product product = (Product) request.getAttribute("product");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
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

<!DOCTYPE html>
<html>
<head>
    <title>Dettagli Prodotto</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<!-- Navbar -->
<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=HomeManagement.view">
            Tennis & Padel Store
        </a>
    </div>
    <div class="search-bar">
        <form action="searchProducts" method="get">
            <input type="text" name="query" placeholder="Cerca prodotti...">
            <button type="submit">Cerca</button>
        </form>
    </div>
    <div class="navbar-right">
        <% if (!loggedOn) { %>
        <a href="Dispatcher?controllerAction=Login.view" class="buttons">Login</a>
        <% } else { %>
        <span>Benvenuto, <%= loggedUser.getNome() %>!</span>
        <% } %>
        <a href="Dispatcher?controllerAction=Carrello.view" class="buttons"><img src="${pageContext.request.contextPath}/images/carrello.png">
        </a>
    </div>
</nav>

<!-- Sezione per il prodotto -->
<div class="container">

    <!-- Link per tornare alla pagina precedente -->
    <div class="back-link">
        <a href="Dispatcher?controllerAction=Shop.view">Torna allo Shop</a>
    </div>

    <div class="product-detail">
        <div class="product-image">
            <img src="data:image/jpeg;base64,<%= base64Image %>" alt="<%= product.getNome() %>" />
        </div>

        <div class="product-info">
            <h1> <strong> <%= product.getNome() %></strong></h1>
            <p><strong>Brand </strong> <%= product.getBrand() %></p>
            <p><strong>Categoria </strong> <%= product.getCategoria() %></p>
            <p><strong>Descrizione </strong> <%= product.getDescrizione() %></p>
            <div class="price"><strong>Prezzo </strong><%= product.getPrezzo() %></div>

            <form action="cart?action=add&productid=<%= product.getProductid() %>" method="post">
                <button type="submit" class="add-to-cart-btn">Aggiungi al Carrello</button>
            </form>
        </div>
        </div>
    </div>


<footer>
    <div class="footer-content">
        <div class="footer-left">
            <h4>Contact</h4>
            <p><strong>Address:</strong> Via Francia, Ferrara, FE</p>
            <p><strong>Phone:</strong> +39 3389237840</p>
        </div>
        <div class="footer-right">
            <h4>Follow us on</h4>
            <a href="https://www.facebook.com/tennisepadelstore" target="_blank"><img src="${pageContext.request.contextPath}/images/facebook.png" alt="Facebook"></a>
            <a href="https://x.com/tennisepadelstore" target="_blank"><img src="${pageContext.request.contextPath}/images/twitter.png" alt="Twitter"></a>
            <a href="https://www.instagram.com/tennisepadelstore" target="_blank"><img src="${pageContext.request.contextPath}/images/instagram.png" alt="Instagram"></a>
        </div>
    </div>
    <div class="footer-bottom">
        <h5>&copy; 2024 Tennis & Padel Store. Tutti i diritti riservati.</h5>
    </div>
</footer>

</body>
</html>
