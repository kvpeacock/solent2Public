<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
// request set in controller
//    request.setAttribute("selectedPage","about");
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Catalog</H1>
    
    <form action="./createNewItem">
        <button class="btn" type="submit" >Add Item</button>
    </form> 
    <H1>Available Items</H1>
    <!-- print error message if there is one -->
    <div style="color:red;">${errorMessage}</div>
    <div style="color:green;">${message}</div>
    
    <form action="./searchCatalog" method="GET">
        <input name="name" input="text" placeholder="Search by name" value="${searchedValue}">
        <button class="btn" type="submit" >Search</button>
    </form> 
    
    <table class="table">
        <thead>
            <tr>
                <th scope="col">Item Name</th>
                <th scope="col">Price</th>
                <th scope="col">Stock</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${availableItems}">
                <tr>
                    <td>${item.name}</td>
                    <td>${item.price}</td>
                    <td>${item.stock}</td>
                    <td></td>
                    <td>
                        <!-- post avoids url encoded parameters -->
                        <form action="./viewModifyItem" method="GET">
                            <input type="hidden" name="name" value="${item.name}">
                            <button class="btn" type="submit" >Modify Item</button>
                        </form> 
                    </td>
                </tr>
            </c:forEach>
        </tbody>

    </table>
</main>




<jsp:include page="footer.jsp" />
