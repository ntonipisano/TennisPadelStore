<%-- SHOP --%>
<%@page session="false"%>
<%@page import="com.pisano.tennispadelstore.model.mo.User"%>
<%@page import="com.pisano.tennispadelstore.model.mo.Product"%>
<%@ page import="java.sql.Blob"%>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.List" %>

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
    <title>Tennis & Padel Store</title>
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

<!-- Messaggio Applicativo -->
<% if (applicationMessage != null) { %>
<div class="alert">
    <%= applicationMessage %>
</div>
<% } %>

<div class="container">
    <h2>Tutti i nostri prodotti</h2>
    <div class="featured-products">
        <%
            List<Product> allProducts = (List<Product>) request.getAttribute("allProducts");
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
        <div class="product-card">
            <a href="Dispatcher?controllerAction=ProductDetail.view&productid=<%= product.getProductid() %>">
                <img src="data:image/jpeg;base64,<%= base64Image %>" alt="<%= product.getNome() %>">
                <h3><%= product.getNome() %></h3>
                <p><%= product.getDescrizione() %></p>
                <p><%= product.getPrezzo() %></p>
            </a>
        </div>
        <%
            }
        %>
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
