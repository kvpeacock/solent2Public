<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.solent.com504.oodd.cart.model.dto.User"%>
<%@page import="org.solent.com504.oodd.cart.model.dto.UserRole"%>
<c:set var = "selectedPage" value = "invoices" scope="request"/>
<jsp:include page="header.jsp" />

<!-- Begin page content -->
<main role="main" class="container">

    <div>
        <H1>Invoice Details</H1>
        <!-- print error message if there is one -->
        <div style="color:red;">${errorMessage}</div>
        <div style="color:green;">${message}</div>

        <form action="./updateInvoiceStatus" method="POST">
            <table class="table" id="General Details Table">
                <thead>
                    <tr><h5>General</h5></tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Date of Purchase</td>
                        <td>${invoice.dateOfPurchase}</td>
                    </tr>
                    <c:if test="${sessionUser.userRole =='ADMINISTRATOR'}">
                        <tr>
                            <td>Purchaser</td>
                            <td>${invoice.purchaser.firstName} ${invoice.purchaser.secondName} </td>
                        </tr>
                    </c:if>

                    <tr>
                        <td>Status</td>
                        <c:if test="${sessionUser.userRole =='CUSTOMER'}">
                            <td>${invoice.status} </td>
                        </c:if>
                        <c:if test="${sessionUser.userRole =='ADMINISTRATOR'}">
                            <td>
                                <select name="status">
                                    <c:forEach items="${statusValues}" var="statusValue">
                                        <option ${invoice.status == statusValue
                                                  ? 'selected="selected"' 
                                                  : ''
                                                  } value="<c:out value="${statusValue}"/>">
                                            <c:out value="${statusValue}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                        </c:if>
                    </tr>
                </tbody>
            </table>

            <table class="table" id="Item Table">
                <thead>
                    <tr><h5>Items</h5></tr>
                <tr>
                    <td>Name</td>
                    <td>Price</td>
                    <td>Quantity</td>
                    <td>Combined Cost(£)</td>
                </tr>
                </thead>

                <tbody>
                    <c:forEach var="item" items="${invoice.purchasedItems}">
                        <tr>
                            <td>${item.name}</td>
                            <td>${item.price}</td>
                            <td>${item.quantity}</td>
                            <td>${item.quantity * item.price}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td></td>
                        <td></td>
                        <td><h5>Total Cost</h5></td>
                        <td><h5>${invoice.amountDue}</h5></td>
                    </tr>
                </tbody>
            </table>
            <c:if test="${sessionUser.userRole =='ADMINISTRATOR'}">
                <input type="hidden" name="invoiceNumber" value="${invoice.invoiceNumber}"/>
                <button class="btn" type="submit" >Update Invoice Status</button>
            </c:if>
        </form>
    </div>
</main>

<jsp:include page="footer.jsp" />
