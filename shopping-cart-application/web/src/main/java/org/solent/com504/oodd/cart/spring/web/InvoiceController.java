/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.solent.com504.oodd.cart.spring.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.InvoiceRepository;
import org.solent.com504.oodd.cart.model.dto.Invoice;
import org.solent.com504.oodd.cart.model.dto.InvoiceStatus;
import org.solent.com504.oodd.cart.model.dto.User;
import org.solent.com504.oodd.cart.model.dto.UserRole;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Adds invoice CRUD functionality to the web site. 
 * Enforces a rule where only admins can view webpages with CRUD functionality - non-admins will be redirected to the home page
 * @author kpeacock
 */
@Controller
@RequestMapping("/")
public class InvoiceController {

    final static Logger LOG = LogManager.getLogger(InvoiceController.class);

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    ShoppingService shoppingService = null;

    @Autowired
    ShoppingCart shoppingCart = null;

    private User getSessionUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            sessionUser = new User();
            sessionUser.setUsername("anonymous");
            sessionUser.setUserRole(UserRole.ANONYMOUS);
            session.setAttribute("sessionUser", sessionUser);
        }
        return sessionUser;
    }
    /**
     * Redirects admins to the viewModifyInvoice page to view details of a specific invoice
     * @param invoiceNumber the UUID of the invoice to view
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the webpage detailing the specified invoice details
     */
    @RequestMapping(value = {"/viewModifyInvoice"}, method = RequestMethod.GET)
    public String invoice(
            @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        model.addAttribute("selectedPage", "home");

        LOG.info("get viewModifyInvoice called for item=" + invoiceNumber);

        // check secure access to modifyInvoice profile
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole()) && !UserRole.CUSTOMER.equals(sessionUser.getUserRole())) {
            errorMessage = "Please sign in to view orders";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        Invoice invoice = invoiceRepository.findByInvoiceUUID(invoiceNumber);
        if (invoice == null) {
            LOG.warn("viewModifyInvoice called for unknown item=" + invoiceNumber);
            errorMessage = "Unknown order: " + invoiceNumber;
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }
        List<String> statusValues = new ArrayList<>();
        for (InvoiceStatus s : InvoiceStatus.values()) {
            statusValues.add(s.toString());
        }
        model.addAttribute("statusValues", statusValues);
        model.addAttribute("invoice", invoice);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        return "viewModifyInvoice";
    }
    /**
     * Provides the invoice webpage with partial invoice number search funtionality
     * @param invoiceNumber the invoice UUID to query against
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the invoice webpage, with only the invoices that match the defined query
     */
    @RequestMapping(value = "/searchInvoices", method = {RequestMethod.GET, RequestMethod.POST})
    public String searchInvoices(
            @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
            Model model,
            HttpSession session
    ) {
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (sessionUser.getUserRole() != UserRole.ADMINISTRATOR) {
            model.addAttribute("errorMessage", "Access Denied");
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        List<Invoice> invoices = invoiceRepository.findByPartialInvoiceUUID(invoiceNumber);
        model.addAttribute("invoices", invoices);
        model.addAttribute("searchedValue", invoiceNumber);

        // used to set tab selected
        model.addAttribute("selectedPage", "admin");
        return "adminInvoices";
    }

    /**
     * Used to update the status of an invoice
     * @param invoiceNumber the UUID of the invoice to update
     * @param status the status to update to
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the viewModifyInvoice webpage, updated with the new specified status
     */
    @RequestMapping(value = "/updateInvoiceStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public String invoiceStatusUpdate(
            @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
            @RequestParam(value = "status", required = false) String status,
            Model model,
            HttpSession session
    ) {
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (sessionUser.getUserRole() != UserRole.ADMINISTRATOR) {
            model.addAttribute("errorMessage", "Access Denied");
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        Invoice invoiceToUpdate = invoiceRepository.findByInvoiceUUID(invoiceNumber);

        if (invoiceToUpdate == null) {
            LOG.warn("viewModifyInvoice called for unknown item=" + invoiceNumber);
            String errorMessage = "Unknown order: " + invoiceNumber;
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }
        else if (null == status) {
            LOG.warn("Cannot update invoice " + invoiceNumber + " status to " + status);
            String errorMessage = "Unknown order: " + invoiceNumber;
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        } else {
            switch (status) {
                case "PENDING":
                    invoiceToUpdate.setStatus(InvoiceStatus.PENDING);
                    break;
                case "FULFILLED":
                    invoiceToUpdate.setStatus(InvoiceStatus.FULFILLED);
                    break;
                case "REJECTED":
                    invoiceToUpdate.setStatus(InvoiceStatus.REJECTED);
                    break;
                default:
                    LOG.warn("Cannot update invoice " + invoiceNumber + " status to " + status);
                    String errorMessage = "Unknown order: " + invoiceNumber;
                    model.addAttribute("errorMessage", errorMessage);
                    model.addAttribute("availableItems", shoppingService.getAvailableItems());
                    model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
                    model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
                    return "home";
            }
        }

        invoiceRepository.save(invoiceToUpdate);
        LOG.info("Invoice " + invoiceToUpdate.getInvoiceUUID() + " status updated to " + status);
        List<String> statusValues = new ArrayList<>();
        for (InvoiceStatus s : InvoiceStatus.values()) {
            statusValues.add(s.toString());
        }
        model.addAttribute("statusValues", statusValues);
        model.addAttribute("invoice", invoiceToUpdate);
        model.addAttribute("sessionUser", sessionUser);
        String message = "Invoice updated";
        model.addAttribute("message", message);
        model.addAttribute("selectedPage", "admin");
        return "viewModifyInvoice";
    }
    /**
     * Generates a list of all invoices
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the admin invoice webpage
     */
    @RequestMapping(value = "/invoices", method = {RequestMethod.GET, RequestMethod.POST})
    public String invoiceList(Model model, HttpSession session) {
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (sessionUser.getUserRole() != UserRole.ADMINISTRATOR) {
            model.addAttribute("errorMessage", "Access Denied");
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        List<Invoice> invoices = invoiceRepository.findAll();
        model.addAttribute("invoices", invoices);

        // used to set tab selected
        model.addAttribute("selectedPage", "admin");
        return "adminInvoices";
    }

    /**
     * Default exception handler, catches all exceptions, redirects to friendly
     * error page. Does not catch request mapping errors
     * @param e the exception
     * @param model used to access the model holder
     * @param request the request made
     * @return the error details
     */
    @ExceptionHandler(Exception.class)
    public String myExceptionHandler(final Exception e, Model model,
            HttpServletRequest request
    ) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        final String strStackTrace = sw.toString(); // stack trace as a string
        String urlStr = "not defined";
        if (request != null) {
            StringBuffer url = request.getRequestURL();
            urlStr = url.toString();
        }
        model.addAttribute("requestUrl", urlStr);
        model.addAttribute("strStackTrace", strStackTrace);
        model.addAttribute("exception", e);
        //logger.error(strStackTrace); // send to logger first
        return "error"; // default friendly exception message for sessionUser
    }

}
