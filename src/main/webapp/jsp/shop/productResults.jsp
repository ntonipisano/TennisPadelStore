<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.pisano.tennispadelstore.model.mo.Product" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>

<div class="featured-products" id="productResults">
    <%
        List<Product> filteredProducts = (List<Product>) request.getAttribute("filteredProducts");

    for (Product product : filteredProducts) {
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

