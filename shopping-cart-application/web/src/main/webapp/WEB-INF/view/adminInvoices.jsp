<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
// request set in controller
//    request.setAttribute("selectedPage","about");
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Order History</H1>
    
    <form action="./searchInvoices" method="GET">
        <input name="invoiceNumber" input="text" placeholder="Search by partial Order ID" value="${searchedValue}">
        <button class="btn" type="submit" >Search</button>
    </form> 

    <H1></H1>
    <table class="table">
        <thead>
            <tr>
                <th scope="col">Order ID</th>
                <th scope="col">Purchaser</th>
                <th scope="col">Status</th>
                <th scope="col">Date</th>
                <th scope="col">Cost</th>
                
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="invoice" items="${invoices}">
                <tr>
                    <td>${invoice.invoiceUUID}</td>
                    <td>${invoice.purchaser.username}</td>
                    <td>${invoice.status}</td>
                    <td>${invoice.dateOfPurchase}</td>
                    <td>${invoice.amountDue}</td>
                    <td></td>
                    <td>
                        <!-- post avoids url encoded parameters -->
                        <form action="./viewModifyInvoice" method="GET">
                            <input type="hidden" name="invoiceNumber" value="${invoice.invoiceUUID}">
                            <button class="btn" type="submit" >View Full Details</button>
                        </form> 
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</main>




<jsp:include page="footer.jsp" />
