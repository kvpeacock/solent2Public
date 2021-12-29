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
        LOG.error("user");
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

//    @RequestMapping(value = {"/viewModifyUser"}, method = RequestMethod.POST)
//    public String updateuser(
//            @RequestParam(value = "username", required = true) String username,
//            @RequestParam(value = "firstName", required = false) String firstName,
//            @RequestParam(value = "secondName", required = false) String secondName,
//            @RequestParam(value = "userRole", required = false) String userRole,
//            @RequestParam(value = "userEnabled", required = false) String userEnabled,
//            @RequestParam(value = "houseNumber", required = false) String houseNumber,
//            @RequestParam(value = "addressLine1", required = false) String addressLine1,
//            @RequestParam(value = "addressLine2", required = false) String addressLine2,
//            @RequestParam(value = "city", required = false) String city,
//            @RequestParam(value = "county", required = false) String county,
//            @RequestParam(value = "country", required = false) String country,
//            @RequestParam(value = "postcode", required = false) String postcode,
//            @RequestParam(value = "latitude", required = false) String latitude,
//            @RequestParam(value = "longitude", required = false) String longitude,
//            @RequestParam(value = "telephone", required = false) String telephone,
//            @RequestParam(value = "mobile", required = false) String mobile,
//            @RequestParam(value = "password", required = false) String password,
//            @RequestParam(value = "password2", required = false) String password2,
//            @RequestParam(value = "action", required = false) String action,
//            Model model,
//            HttpSession session) {
//        String message = "";
//        String errorMessage = "";
//
//        LOG.debug("post updateUser called for username=" + username);
//
//        // security check if party is allowed to access or modify this party
//        User sessionUser = getSessionUser(session);
//        model.addAttribute("sessionUser", sessionUser);
//
//        if (UserRole.ANONYMOUS.equals(sessionUser.getUserRole())) {
//            errorMessage = "you must be logged in to access users information";
//            model.addAttribute("errorMessage", errorMessage);
//            return "home";
//        }
//
//        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
//            if (!sessionUser.getUsername().equals(username)) {
//                errorMessage = "security viewModifyUser called for non admin username " + username
//                        + "which is not the logged in user =" + sessionUser.getUsername();
//                model.addAttribute("errorMessage", errorMessage);
//                LOG.warn(errorMessage);
//                return ("home");
//            }
//        }
//
//        List<User> userList = userRepository.findByUsername(username);
//        if (userList.isEmpty()) {
//            errorMessage = "update user called for unknown username:" + username;
//            LOG.warn(errorMessage);
//            model.addAttribute("errorMessage", errorMessage);
//            return ("home");
//        }
//
//        User modifyUser = userList.get(0);
//
//        if (firstName != null) {
//            modifyUser.setFirstName(firstName);
//        }
//        if (secondName != null) {
//            modifyUser.setSecondName(secondName);
//        }
//
//        Address address = new Address();
//        address.setHouseNumber(houseNumber);
//        address.setAddressLine1(addressLine1);
//        address.setAddressLine2(addressLine2);
//        address.setCity(city);
//        address.setCounty(county);
//        address.setCountry(country);
//
//        address.setPostcode(postcode);
//        address.setMobile(mobile);
//        address.setTelephone(telephone);
//
//        modifyUser.setAddress(address);
//
//        modifyUser = userRepository.save(modifyUser);
//
//        model.addAttribute("modifyUser", modifyUser);
//
//        // add message if there are any 
//        model.addAttribute("errorMessage", errorMessage);
//        model.addAttribute("message", "User " + modifyUser.getUsername() + " updated successfully");
//
//        model.addAttribute("selectedPage", "home");
//
//        return "viewModifyUser";
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