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
    List<Product> searchProducts = (List<Product>) request.getAttribute("searchProducts");
    List<Product> allProducts = (List<Product>) request.getAttribute("allProducts");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis & Padel Store</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<%-- Script js che ricarica una volta la pagina shop per ricevere il cookie user --%>
<script type="text/javascript">
    function getCookie(name) {
        let cookieArr = document.cookie.split(";");
        for (let i = 0; i < cookieArr.length; i++) {
            let cookiePair = cookieArr[i].split("=");
            if (name == cookiePair[0].trim()) {
                return decodeURIComponent(cookiePair[1]);
            }
        }
        return null;
    }

    // Funzione per ricaricare la pagina
    function handlePageReload() {
        // Se la pagina non è mai stata ricaricata, ricarica la prima volta
        if (!localStorage.getItem("firstLoad")) {
            localStorage.setItem("firstLoad", "true");
            window.location.reload();
        }

        // Controlla se l'utente è loggato (cookie 'loggedUser' presente)
        if (getCookie("loggedUser")) {
            // Se la pagina non è stata ricaricata dopo il login, ricarica
            if (!localStorage.getItem("loginReloaded")) {
                localStorage.setItem("loginReloaded", "true");
                window.location.reload();
            }
        } else {
            // Se l'utente non è loggato (logout), resetta il flag
            localStorage.removeItem("loginReloaded");
        }
    }
    handlePageReload();
</script>

<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=HomeManagement.view">
            Tennis & Padel Store
        </a>
    </div>
    <div class="search-bar">
        <form action="Dispatcher" method="post" id="searchForm" name="searchForm">
            <input type="hidden" name="controllerAction" value="Shop.searchProducts"/>
            <input type="text" name="query" placeholder="Cerca prodotti...">
            <button type="submit">Cerca</button>
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
<!--Filtri Shop -->
<nav class="navbar">
<div class="filter-navbar">

    <form name ="filterForm" id="filterForm">

        <!-- Opzioni per categoria -->
           <label for="category" style="margin-left: 10px">Categoria</label>
            <select id="category" name="category" style="font-family: 'Inter', sans-serif">
                <option value="*">Tutte</option>
                <option value="Racchette">Racchette</option>
                <option value="Palline">Palline</option>
                <option value="Grip">Grip</option>
                <option value="Abbigliamento">Abbigliamento</option>
                <option value="Accessori">Accessori</option>
                <option value="Borsa">Borsa</option>
            </select>

        <!-- Opzioni per brand -->
            <label for="brand" style="margin-left: 10px">Brand </label>
            <select id="brand" name="brand" style="font-family: 'Inter', sans-serif; width: 114px">
                <option value="*">Tutti</option>
                <option value="Babolat">Babolat</option>
                <option value="Nike">Nike</option>
                <option value="Adidas">Adidas</option>
                <option value="Wilson">Wilson</option>
                <option value="Head">Head</option>
                <option value="Lacoste">Lacoste</option>
                <option value="Bullpadel">Bullpadel</option>
                <option value="Dunlop">Dunlop</option>
            </select>

            <!-- Opzioni prezzo -->
            <label for="minPrice" style="margin-left: 10px">Prezzo Minimo </label>
            <input type="number" style="width: 100px; font-family: 'Inter', sans-serif" id="minPrice" name="minPrice" placeholder="Prezzo Min">

            <label for="maxPrice" style="margin-left: 10px">Prezzo Massimo </label>
            <input type="number" style="width: 100px; font-family: 'Inter', sans-serif" id="maxPrice" name="maxPrice" placeholder="Prezzo Max">

            <button type="button" id="resetFilters" style=" margin-left: 10px; padding: 0.2rem 1.8rem; border: none; border-radius: 0 4px 4px 0; background-color: #f05a28; color: #fff; cursor: pointer; font-family: 'Inter', sans-serif;">Resetta filtri</button>
    </form>
</div>
</nav>

<!-- Messaggio Applicativo -->
<% if (applicationMessage != null) { %>
<div class="alert">
    <%= applicationMessage %>
</div>
<% } %>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const searchForm = document.getElementById('searchForm');
        const productResults = document.getElementById('searchResults');

        searchForm.addEventListener('submit', function() {
            // Mostra il div dei risultati solo dopo che il form è stato inviato
            productResults.style.display = 'block';
        });
    });
</script>

<div class="container" style="min-height: 219px;">
    <h2>I nostri prodotti</h2>
    <div class="featured-products" id="searchResults">
        <div id="no-products-message" style="display: none;">Nessun prodotto trovato </div>
        <%
            List<Product> productsToDisplay = (searchProducts != null && !searchProducts.isEmpty()) ? searchProducts : allProducts;
            if (productsToDisplay != null && !productsToDisplay.isEmpty()) {
                for (Product product : productsToDisplay) {
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
        <div class="product-card"
             data-category="<%= product.getCategoria() %>"
             data-brand="<%= product.getBrand() %>"
             data-price="<%= product.getPrezzo() %>">
            <a href="Dispatcher?controllerAction=ProductDetail.view&productid=<%= product.getProductid() %>">
                <img src="data:image/jpeg;base64,<%= base64Image %>" alt="<%= product.getNome() %>">
                <h3><%= product.getNome() %></h3>
                <p><%= product.getDescrizione() %></p>
                <p><%= product.getPrezzo() %></p>
            </a>
        </div>
        <%
            }
        }
        %>
    </div>
</div>
</main>
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

<!-- Script per filtraggio dinamico -->
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const filterForm = document.getElementById('filterForm');
        const productResults = document.getElementById('productResults');
        const productCards = document.querySelectorAll('.product-card');
        const noProductsMessage = document.getElementById('no-products-message');

        function filterProducts() {
            const category = filterForm.querySelector('[name="category"]').value;
            const brand = filterForm.querySelector('[name="brand"]').value;
            const minPrice = parseFloat(filterForm.querySelector('[name="minPrice"]').value) || 0;
            const maxPrice = parseFloat(filterForm.querySelector('[name="maxPrice"]').value) || Infinity;

            let visibleCount = 0;

            productCards.forEach(product => {
                const productCategory = product.getAttribute('data-category');
                const productBrand = product.getAttribute('data-brand');
                const productPrice = parseFloat(product.getAttribute('data-price'));

                const categoryMatch = (category === '*' || productCategory === category);
                const brandMatch = (brand === '*' || productBrand === brand);
                const priceMatch = (productPrice >= minPrice && productPrice <= maxPrice);

                if (categoryMatch && brandMatch && priceMatch) {
                    product.style.display = '';
                    visibleCount++;
                } else {
                    product.style.display = 'none';
                }
            });
            if (visibleCount === 0) {
                noProductsMessage.style.display = 'block';
            } else {
                noProductsMessage.style.display = 'none';
            }
        }
        //Event listener per il form di filtraggio
        filterForm.addEventListener('change', filterProducts);
        document.getElementById('resetFilters').addEventListener('click', function() {
            filterForm.reset();
            filterProducts();
        });
        filterProducts();
    });
</script>
</body>
</html>