<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Create New Item</H1>
    <!-- print error message if there is one -->
    <div style="color:red;">${errorMessage}</div>
    <div style="color:green;">${message}</div>
    <form action="./createNewItem" method="POST">
        <table class="table">
            <thead>
            </thead>
            <tbody>
                <tr>
                    <td>Name</td>
                    <td><input type="text" required name="name" /></td>
                </tr>
                <tr>
                    <td>Price</td>
                    <td><input type="number" min="0" step="0.01" required name="price" /></td>
                </tr>
                <tr>
                    <td>Stock</td>
                    <td><input type="number" min="0" required name="stock"/></td>
                </tr>
            </tbody>
        </table>
    <button class="btn" type="submit" >Create Item</button>
    </form>
</main>
<jsp:include page="footer.jsp" />
