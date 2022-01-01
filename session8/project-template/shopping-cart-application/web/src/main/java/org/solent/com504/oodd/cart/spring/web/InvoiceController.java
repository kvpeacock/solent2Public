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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.InvoiceRepository;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.dto.Address;
import org.solent.com504.oodd.cart.model.dto.Invoice;
import org.solent.com504.oodd.cart.model.dto.User;
import org.solent.com504.oodd.cart.model.dto.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class InvoiceController {

    final static Logger LOG = LogManager.getLogger(InvoiceController.class);

    @Autowired
    InvoiceRepository invoiceRepository;

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

    @RequestMapping(value = {"/viewModifyInvoice"}, method = RequestMethod.GET)
    public String invoice(
            @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        model.addAttribute("selectedPage", "home");

        LOG.error("get viewModifyInvoice called for item=" + invoiceNumber);

        // check secure access to modifyItem profile
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);


        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole()) && !UserRole.CUSTOMER.equals(sessionUser.getUserRole()) ) {
            // if not an administrator you can only access your own account info
            errorMessage = "Please sign in to view orders";
            model.addAttribute("errorMessage", errorMessage);
            return ("home");
        }
        
        
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        if (invoice==null) {
            LOG.error("viewModifyInvoice called for unknown item=" + invoiceNumber);
            errorMessage = "Unknown order: "  + invoiceNumber;
            model.addAttribute("errorMessage", errorMessage);
            return ("home");
        }

        model.addAttribute("invoice", invoice);
        model.addAttribute("sessionUser",sessionUser);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        return "viewModifyInvoice";
    }

//    @RequestMapping(value = {"/viewModifyItem"}, method = RequestMethod.POST)
//    public String updateitem(
//            @RequestParam(value = "uuid", required = true) String uuid,
//            @RequestParam(value = "name", required = false) String name,
//            @RequestParam(value = "price", required = false) Double price,
//            @RequestParam(value = "stock", required = false) Integer stock,
//            Model model,
//            HttpSession session) {
//        String message = "";
//        String errorMessage = "";
//
//        LOG.error("post updateItem called for item " + uuid + "name= " + name);
//
//        // security check if party is allowed to access or modify this party
//        User sessionUser = getSessionUser(session);
//        model.addAttribute("sessionUser", sessionUser);
//
//        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
//            // if not an administrator you can only access your own account info
//            errorMessage = "Acess Denied";
//            model.addAttribute("errorMessage", errorMessage);
//            return ("home");
//        }
//        
//        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
//        if (invoice==null) {
//            LOG.error("viewModifyItem called for unknown item=" + name);
//            return ("home");
//        }
//        
//        if (name != null) {
//            modifyItem.setName(name);
//        }
//        
//        if (price != null) {
//            modifyItem.setPrice(price);
//        }
//        
//        if (stock != null) {
//            modifyItem.setStock(stock);
//        }
//             
//        modifyItem = itemRepository.save(modifyItem);
//
//        model.addAttribute("modifyItem", modifyItem);
//
//        // add message if there are any 
//        model.addAttribute("errorMessage", errorMessage);
//        model.addAttribute("message", "User " + modifyItem.getName() + " updated successfully");
//
//        model.addAttribute("selectedPage", "home");
//
//        return "viewModifyItem";
//    }

    /*
     * Default exception handler, catches all exceptions, redirects to friendly
     * error page. Does not catch request mapping errors
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