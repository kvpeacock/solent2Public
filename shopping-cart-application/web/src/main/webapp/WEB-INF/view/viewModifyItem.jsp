<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.solent.com504.oodd.cart.model.dto.User"%>
<%@page import="org.solent.com504.oodd.cart.model.dto.UserRole"%>
<c:set var = "selectedPage" value = "invoice" scope="request"/>
<jsp:include page="header.jsp" />

<!-- Begin page content -->
<main role="main" class="container">

    <div>
        <H1>Invoice Details ${modifyItem.name} </H1>
        <!-- print error message if there is one -->
        <div style="color:red;">${errorMessage}</div>
        <div style="color:green;">${message}</div>

        <form action="./viewModifyItem" method="POST">
            <table class="table">
                <thead>
                </thead>

                <tbody>
                    <tr>
                        <td>Item UUID</td>
                        <td>${modifyItem.uuid}</td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td><input type="text" required name="newName" value="${modifyItem.name}" /></td>
                    </tr>
                    <tr>
                        <td>Price</td>
                        <td><input type="number" required min="0" step="0.01" name="price" value="${modifyItem.price}" /></td>
                    </tr>
                    <tr>
                        <td>Stock</td>
                        <td><input type="number" required min="0" name="stock" value="${modifyItem.stock}" /></td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="uuid" value="${modifyItem.uuid}"/>
            <input type="hidden" name="name" value="${modifyItem.name}"/>
            <input type="hidden" name="action" value="update"/>
            <button class="btn" type="submit" >Update Item</button>
        </form>
            
            <BR>
            <form action="./viewModifyItem" method="POST">
                <input type="hidden" name="action" value="delete"/>
                <input type="hidden" name="name" value="${modifyItem.name}"/>
                <input type="hidden" name="uuid" value="${modifyItem.uuid}"/>
                <button class="btn" type="submit" >Delete Item</button>
            </form>

            <BR>
            <form action="./catalog">
                <button class="btn" type="submit" >Return To Catalog</button>
            </form> 

        </div>

    </main>

<jsp:include page="footer.jsp" />
