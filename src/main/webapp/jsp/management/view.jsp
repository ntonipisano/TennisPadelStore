<%--PAGINA ADMIN SITO--%>
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

%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pagina amministratori</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">

    <style>

    </style>

    <script>
        function toggleSection(sectionId) {
            var section = document.getElementById(sectionId);
            // Mostra o nasconde la sezione
            if (section.style.display === "none" || section.style.display === "") {
                section.style.display = "block";
            } else {
                section.style.display = "none";
            }
        }
    </script>
</head>
<body>
<nav class="navbar">
    <div class="navbar-brand">
        <a href="Dispatcher?controllerAction=HomeManagement.view">
            Tennis & Padel Store - Amministrazione
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

<div class="admin-container">
    <!-- Gestione Utenti -->
    <div class="admin-section">
        <h2><button class="start-button" onclick="toggleSection('userManagement')">Gestione Utenti</button></h2>
        <!-- Sezione inizialmente nascosta -->
        <div id="userManagement" class="admin-section-content" style="display: none;">
            <button onclick="window.location.href='AdminManagement?controllerAction=viewUsers'">Visualizza Utenti</button>
            <button onclick="window.location.href='AdminManagement?controllerAction=manageAdmins'">Gestisci Amministratori</button>
            <button onclick="window.location.href='AdminManagement?controllerAction=blockUsers'">Blocca Utenti</button>
        </div>
    </div>

    <!-- Gestione Catalogo -->
    <div class="admin-section">
        <h2><button class="start-button" onclick="toggleSection('catalogManagement')">Gestione Catalogo</button></h2>
        <!-- Sezione inizialmente nascosta -->
        <div id="catalogManagement" class="admin-section-content" style="display: none;">
            <button onclick="window.location.href='CatalogManagement?controllerAction=addProduct'">Aggiungi Prodotto</button>
            <button onclick="window.location.href='CatalogManagement?controllerAction=manageProducts'">Gestisci Prodotti</button>
            <button onclick="window.location.href='CatalogManagement?controllerAction=manageCategories'">Gestisci Categorie</button>
            <button onclick="window.location.href='CatalogManagement?controllerAction=manageBrands'">Gestisci Brand</button>
            <button onclick="window.location.href='CatalogManagement?controllerAction=manageStock'">Gestisci Magazzino</button>
        </div>
    </div>

    <!-- Gestione Ordini -->
    <div class="admin-section">
        <h2><button class="start-button" onclick="toggleSection('orderManagement')">Gestione Ordini</button></h2>
        <!-- Sezione inizialmente nascosta -->
        <div id="orderManagement" class="admin-section-content" style="display: none;">
            <button onclick="window.location.href='OrderManagement?controllerAction=viewOrders'">Visualizza Ordini</button>
            <button onclick="window.location.href='OrderManagement?controllerAction=markDelivered'">Marca Ordini come Consegnati</button>
        </div>
    </div>
</div>
</body>
</html>
