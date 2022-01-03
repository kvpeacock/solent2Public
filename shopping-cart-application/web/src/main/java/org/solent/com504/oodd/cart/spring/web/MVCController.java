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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.InvoiceItemRepository;
import org.solent.com504.oodd.cart.dao.impl.InvoiceRepository;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.model.dto.Invoice;
import org.solent.com504.oodd.cart.model.dto.InvoiceItem;
import org.solent.com504.oodd.cart.model.dto.InvoiceStatus;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
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
@Controller
@RequestMapping("/")
public class MVCController {

    final static Logger LOG = LogManager.getLogger(MVCController.class);

    @Autowired
    ShoppingService shoppingService = null;

    @Autowired
    ShoppingCart shoppingCart = null;

    @Autowired
    private ShoppingItemCatalogRepository shoppingItemCatalogRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

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

    // this redirects calls to the root of our application to index.html
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewCart(@RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "itemName", required = false) String itemName,
            @RequestParam(name = "itemUUID", required = false) String itemUuid,
            @RequestParam(name = "cart", required = false) String cart,
            Model model,
            HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        // used to set tab selected
        model.addAttribute("selectedPage", "home");

        String message = "";
        String errorMessage = "";

        if (null == action) {
            // do nothing but show page
        } else {
            switch (action) {
                case "addItemToCart":
                    message = "Attempting to add " + itemName + " to cart.";
                    LOG.info(message);
                    ShoppingItem shoppingItem = shoppingService.getNewItemByName(itemName);
                    if (shoppingItem == null) {
                        message = "";
                        errorMessage = "Cannot add unknown item " + itemName + " to cart";
                        LOG.error(message);
                    } else {

                        String status = shoppingCart.addItemToCart(shoppingItem);
                        if (!"".equals(status)) {
                            message = "";
                            errorMessage = "Error adding " + itemName + " to cart: " + status;
                        } else {
                            message = "Added " + itemName + " to cart";
                        }
                    }
                    break;
                case "removeItemFromCart":
                    message = "Removed " + itemName + " from cart";
                    shoppingCart.removeItemFromCart(itemUuid);
                    break;
                case "purchaseItems":
                    if (sessionUser.getUserRole() == UserRole.ANONYMOUS) {
                        model.addAttribute("errorMessage", "You must be signed in to purchase items.");
                        model.addAttribute("availableItems", shoppingService.getAvailableItems());
                        model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
                        model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
                        return "home";
                    }
                    List<ShoppingItem> shoppingCartItems = shoppingCart.getShoppingCartItems();
                    for (ShoppingItem cartItem : shoppingCartItems) {
                        ShoppingItem catalogItem = shoppingItemCatalogRepository.findByName(cartItem.getName());
                        Integer newStock = catalogItem.getStock() - cartItem.getQuantity();
                        if (newStock < 0) {
                            errorMessage = "Cannot purchase item " + cartItem.getName() + ": Not enough stock.";
                            List<ShoppingItem> availableItems = shoppingService.getAvailableItems();

                            Double shoppingcartTotal = shoppingCart.getTotal();

                            model.addAttribute("availableItems", availableItems);
                            model.addAttribute("shoppingCartItems", shoppingCartItems);
                            model.addAttribute("shoppingcartTotal", shoppingcartTotal);
                            model.addAttribute("message", message);
                            model.addAttribute("errorMessage", errorMessage);
                            return "home";
                        }
                    }
                    
                    
                    
                    
                    
                    shoppingCartItems.forEach((cartItem) -> {
                        ShoppingItem catalogItem = shoppingItemCatalogRepository.findByName(cartItem.getName());
                        catalogItem.setStock(catalogItem.getStock() - cartItem.getQuantity());
                        shoppingItemCatalogRepository.save(catalogItem);
                    });

                    Invoice invoice = new Invoice();
                    invoice.setInvoiceNumber(UUID.randomUUID().toString());
                    invoice.setDateOfPurchase(new Date());
                    invoice.setAmountDue(shoppingCart.getTotal());

                    List<InvoiceItem> itemList = new ArrayList<>();
                    shoppingCartItems.forEach((cartItem) -> {
                        InvoiceItem invoiceItem = new InvoiceItem(cartItem.getName(), cartItem.getPrice());
                        invoiceItem.setQuantity(cartItem.getQuantity());
                        invoiceItem.setUuid(UUID.randomUUID().toString());
                        invoiceItemRepository.save(invoiceItem);
                        itemList.add(invoiceItem);
                    });

                    invoice.setPurchasedItems(itemList);
                    invoice.setPurchaser(sessionUser);
                    invoice.setStatus(InvoiceStatus.PENDING);
                    invoiceRepository.save(invoice);
                    LOG.error(invoice.toString());

                    shoppingCartItems.forEach((cartItem) -> {
                        LOG.error("Attempting to delete from cart");
                        shoppingCart.removeItemFromCart(cartItem.getUuid());
                    });
                    break;
                default:
                    message = "Unknown action=" + action;
                    LOG.error(message);
                    break;
            }
        }

        model.addAttribute("availableItems", shoppingService.getAvailableItems());
        model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
        model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        return "home";
    }

    @RequestMapping(value = "/about", method = {RequestMethod.GET, RequestMethod.POST})
    public String aboutCart(Model model, HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        // used to set tab selected
        model.addAttribute("selectedPage", "about");
        return "about";
    }

    @RequestMapping(value = "/contact", method = {RequestMethod.GET, RequestMethod.POST})
    public String contactCart(Model model, HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        // used to set tab selected
        model.addAttribute("selectedPage", "contact");
        return "contact";
    }

    @RequestMapping(value = "/catalog", method = {RequestMethod.GET, RequestMethod.POST})
    public String catalogList(Model model, HttpSession session) {
        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            // if not an administrator you can only access your own account info
            String errorMessage = "Acess Denied";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
        model.addAttribute("availableItems", availableItems);
        model.addAttribute("selectedPage", "admin");
        return "catalog";
    }

    @RequestMapping(value = "/userInvoices", method = {RequestMethod.GET, RequestMethod.POST})
    public String userInvoiceList(Model model, HttpSession session) {
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (sessionUser.getUserRole() == UserRole.ANONYMOUS) {
            model.addAttribute("errorMessage", "You must be signed in to view orders");
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        List<Invoice> invoices = invoiceRepository.findByPurchaser(sessionUser);
        model.addAttribute("invoices", invoices);
        model.addAttribute("selectedPage", "userInvoices");
        return "userInvoices";
    }
    
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

    /*
     * Default exception handler, catches all exceptions, redirects to friendly
     * error page. Does not catch request mapping errors
     */
    @ExceptionHandler(Exception.class)
    public String myExceptionHandler(final Exception e, Model model, HttpServletRequest request) {
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
