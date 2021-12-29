<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.solent.com504.oodd.cart.model.dto.User"%>
<%@page import="org.solent.com504.oodd.cart.model.dto.UserRole"%>
<c:set var = "selectedPage" value = "items" scope="request"/>
<jsp:include page="header.jsp" />

<!-- Begin page content -->
<main role="main" class="container">

    <div>
        <H1>Item Details ${modifyItem.name} </H1>
        <!-- print error message if there is one -->
        <div style="color:red;">${errorMessage}</div>
        <div style="color:green;">${message}</div>

        <form action="./viewModifyUser" method="GET">
            <table class="table">
                <thead>
                </thead>

                <tbody>
                    <tr>
                        <td>Item UUID</td>
                        <td>${modifyItem.uuid}</td>
                    </tr>
                    <tr>
                        <td>First Name</td>
                        <td><input type="text" name="firstName" value="${modifyItem.name}" /></td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="uuid" value="${modifyItem.uuid}"/>
            <button class="btn" type="submit" >Update Item</button>
        </form>

        <c:if test="${sessionUser.userRole =='ADMINISTRATOR'}">
            <BR>
            <form action="./catalog">
                <button class="btn" type="submit" >Return To Catalog</button>
            </form> 
        </c:if> 

        </div>

    </main>

<jsp:include page="footer.jsp" />
