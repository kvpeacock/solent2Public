package org.solent.com504.oodd.cart.spring.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.dto.Address;
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
public class CatalogController {

    final static Logger LOG = LogManager.getLogger(CatalogController.class);

    @Autowired
    ShoppingItemCatalogRepository itemRepository;

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

    @RequestMapping(value = {"/viewModifyItem"}, method = RequestMethod.GET)
    public String modifyItem(
            @RequestParam(value = "uuid", required = true) String uuid,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        model.addAttribute("selectedPage", "home");

        LOG.error("get viewModifyItem called for item=" + uuid);

        // check secure access to modifyItem profile
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);


        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            // if not an administrator you can only access your own account info
            errorMessage = "Acess Denied";
            model.addAttribute("errorMessage", errorMessage);
            return ("home");
            }
        
        
        ShoppingItem modifyItem = itemRepository.findByUuid(uuid);
        if (modifyItem==null) {
            LOG.error("viewModifyItem called for unknown item=" + uuid);
            return ("home");
        }

        model.addAttribute("modifyItem", modifyItem);

        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        return "viewModifyItem";
    }

    @RequestMapping(value = {"/viewModifyItem"}, method = RequestMethod.POST)
    public String updateitem(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "stock", required = false) Integer stock,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        LOG.error("post updateItem called for item " + uuid + "name= " + name);

        // security check if party is allowed to access or modify this party
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            // if not an administrator you can only access your own account info
            errorMessage = "Acess Denied";
            model.addAttribute("errorMessage", errorMessage);
            return ("home");
        }
        
        ShoppingItem modifyItem = itemRepository.findByUuid(uuid);
        if (modifyItem==null) {
            LOG.error("viewModifyItem called for unknown item=" + uuid);
            return ("home");
        }
        
        if (name != null) {
            modifyItem.setName(name);
        }
        
        if (price != null) {
            modifyItem.setPrice(price);
        }
        
        if (stock != null) {
            modifyItem.setStock(stock);
        }
             
        modifyItem = itemRepository.save(modifyItem);

        model.addAttribute("modifyItem", modifyItem);

        // add message if there are any 
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("message", "User " + modifyItem.getName() + " updated successfully");

        model.addAttribute("selectedPage", "home");

        return "viewModifyItem";
    }

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