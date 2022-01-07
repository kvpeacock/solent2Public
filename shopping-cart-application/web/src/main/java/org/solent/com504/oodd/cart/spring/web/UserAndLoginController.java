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
import org.solent.com504.oodd.cart.dao.impl.UserRepository;
import org.solent.com504.oodd.cart.model.dto.Address;
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
 * Controller used to provide handle the user functionality
 * @author cgallen, kpeacock
 */
@Controller
@RequestMapping("/")
public class UserAndLoginController {

    final static Logger LOG = LogManager.getLogger(UserAndLoginController.class);

    @Autowired
    UserRepository userRepository;
    
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
     * Logs out of the session
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return to the home page
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(Model model,
            HttpSession session) {
        String message = "you have been successfully logged out";
        String errorMessage = "";
        // logout of session and clear
        session.invalidate();

        return "redirect:/home";
    }

    /**
     * Redirects to the login page
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the login page
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    @Transactional
    public String login(
            Model model,
            HttpSession session) {
        String message = "log into site using username";
        String errorMessage = "";

        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ANONYMOUS.equals(sessionUser.getUserRole())) {
            errorMessage = "User " + sessionUser.getUsername() + " already logged in";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
    }
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        // used to set tab selected
        model.addAttribute("selectedPage", "home");

        return "login";
    }
    /**
     * Attempts to login a user
     * @param action the action to perform
     * @param username the username to query against
     * @param password the password to query against
     * @param password2
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the home page, if the login is successful, otherwise the login page with an error message
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @Transactional
    public String login(@RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "password2", required = false) String password2,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        LOG.info("login for username=" + username);

        // get current session modifyUser 
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ANONYMOUS.equals(sessionUser.getUserRole())) {
            errorMessage = "user " + sessionUser.getUsername() + " already logged in";
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        };

        if (username == null || username.trim().isEmpty()) {
            errorMessage = "you must enter a username";
            model.addAttribute("errorMessage", errorMessage);
            return "login";
        }

        List<User> userList = userRepository.findByUsername(username);

        if ("login".equals(action)) {
            //todo find and add modifyUser and test password
            LOG.info("logging in user username=" + username);
            if (userList.isEmpty()) {
                errorMessage = "cannot find user for username :" + username;
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                return "login";
            }
            if (password == null) {
                errorMessage = "you must enter a password";
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                return "login";
            }

            User loginUser = userList.get(0);
            if (!loginUser.isValidPassword(password)) {
                model.addAttribute("errorMessage", "invalid username or password");
                return "login";
            }

            if (!loginUser.getEnabled()) {
                model.addAttribute("errorMessage", "user account "+username
                        + " is disabled in this system");
                return "login";
            }

            message = "successfully logged in user:" + username;
            session.setAttribute("sessionUser", loginUser);

            model.addAttribute("sessionUser", loginUser);

            model.addAttribute("message", message);
            model.addAttribute("errorMessage", errorMessage);
            // used to set tab selected
            model.addAttribute("selectedPage", "home");
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        } else {
            model.addAttribute("errorMessage", "unknown action requested:" + action);
            LOG.warn("login page unknown action requested:" + action);
            model.addAttribute("errorMessage", errorMessage);
            // used to set tab selected
            model.addAttribute("selectedPage", "home");
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }
    }

    /**
     * Redirects the user to the register page
     * @param action the action to perform
     * @param username the username for the new account
     * @param password the password for the new account
     * @param password2 the value of the 'confirm password' field
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the register page
     */
    @RequestMapping(value = "/register", method = {RequestMethod.GET})
    @Transactional
    public String registerGET(@RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "password2", required = false) String password2,
            Model model,
            HttpSession session) {
        String message = "register new user";
        String errorMessage = "";

        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        // used to set tab selected
        model.addAttribute("selectedPage", "home");

        return "register";
    }

    /**
     * Attempts to register a new user
     * @param action the action to perform
     * @param username the username for the new account
     * @param password the password for the new account
     * @param password2 the value of the 'confirm password' field
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the home page if the registration was successful, else the register page with an error
     */
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    @Transactional
    public String register(@RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "password2", required = false) String password2,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        LOG.info("register new username=" + username);

        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (username == null || username.trim().isEmpty()) {
            errorMessage = "you must enter a username";
            model.addAttribute("errorMessage", errorMessage);
            return "register";
        }

        List<User> userList = userRepository.findByUsername(username);

        if ("createNewAccount".equals(action)) {
            if (!userList.isEmpty()) {
                errorMessage = "trying to create user with username which already exists :" + username;
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                return "register";
            }
            if (password == null || !password.equals(password2) || password.length() < 8) {
                errorMessage = "you must enter two identical passwords with atleast 8 characters";
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                return "register";
            }

            User modifyUser = new User();
            modifyUser.setUserRole(UserRole.CUSTOMER);
            modifyUser.setUsername(username);
            modifyUser.setFirstName(username);
            modifyUser.setPassword(password);
            modifyUser = userRepository.save(modifyUser);

            // if already logged in - keep session modifyUser else set session modifyUser to modifyUser
            // else set session modifyUser the newly created modifyUser (i.e. automatically log in)
            if (UserRole.ANONYMOUS.equals(sessionUser.getUserRole())) {
                session.setAttribute("sessionUser", modifyUser);
                model.addAttribute("sessionUser", modifyUser);
                LOG.info("log in newly created user=" + modifyUser);
            }

            LOG.info("createNewAccount created new user user=" + modifyUser);
            message = "enter user details";
            model.addAttribute("modifyUser", modifyUser);
            model.addAttribute("message", message);
            model.addAttribute("errorMessage", errorMessage);
            return "viewModifyUser";
        } else {
            LOG.info("unknown action " + action);
            model.addAttribute("errorMessage", "unknown action " + action);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }
    }

    /**
     * Redirects to the user webpage, provided the user is an admin
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the user webpage
     */
    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    @Transactional
    public String users(Model model,
            HttpSession session) {
        //String message = "";
        String errorMessage = "";

        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            errorMessage = "Access Denied";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        List<User> userList = userRepository.findAll();

        model.addAttribute("userListSize", userList.size());
        model.addAttribute("userList", userList);
        model.addAttribute("selectedPage", "users");
        return "users";
    }

    /**
     * Displays a specified user's details
     * @param username the username of the user to query against
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return 
     */
    @RequestMapping(value = {"/viewModifyUser"}, method = RequestMethod.GET)
    public String modifyuser(
            @RequestParam(value = "username", required = true) String username,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        model.addAttribute("selectedPage", "home");

        LOG.info("get viewModifyUser called for username=" + username);

        // check secure access to modifyUser profile
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (UserRole.ANONYMOUS.equals(sessionUser.getUserRole())) {
            errorMessage = "you must be logged in to access user information";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            // if not an administrator you can only access your own account info
            if (!sessionUser.getUsername().equals(username)) {
                errorMessage = "security non admin viewModifyUser called for username " + username
                        + "which is not the logged in user =" + sessionUser.getUsername();
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("availableItems", shoppingService.getAvailableItems());
                model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
                model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
                return "home";
            }
        }

        List<User> userList = userRepository.findByUsername(username);
        if (userList.isEmpty()) {
            errorMessage="viewModifyUser called for unknown username=" + username;
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        User modifyUser = userList.get(0);
        model.addAttribute("modifyUser", modifyUser);

        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        return "viewModifyUser";
    }

    /**
     * Updates a specified user
     * @param username the username of the user
     * @param firstName updates the corresponding property of the specified user
     * @param secondName updates the corresponding property of the specified user
     * @param userRole updates the corresponding property of the specified user
     * @param userEnabled updates the corresponding property of the specified user
     * @param houseNumber updates the corresponding property of the specified user
     * @param addressLine1 updates the corresponding property of the specified user
     * @param addressLine2 updates the corresponding property of the specified user
     * @param city updates the corresponding property of the specified user
     * @param county updates the corresponding property of the specified user
     * @param country updates the corresponding property of the specified user
     * @param postcode updates the corresponding property of the specified user
     * @param latitude updates the corresponding property of the specified user
     * @param longitude updates the corresponding property of the specified user
     * @param telephone updates the corresponding property of the specified user
     * @param mobile updates the corresponding property of the specified user
     * @param password updates the corresponding property of the specified user
     * @param password2 updates the corresponding property of the specified user
     * @param action the action to perform
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return 
     */
    @RequestMapping(value = {"/viewModifyUser"}, method = RequestMethod.POST)
    public String updateuser(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "secondName", required = false) String secondName,
            @RequestParam(value = "userRole", required = false) String userRole,
            @RequestParam(value = "userEnabled", required = false) String userEnabled,
            @RequestParam(value = "houseNumber", required = false) String houseNumber,
            @RequestParam(value = "addressLine1", required = false) String addressLine1,
            @RequestParam(value = "addressLine2", required = false) String addressLine2,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "county", required = false) String county,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "postcode", required = false) String postcode,
            @RequestParam(value = "latitude", required = false) String latitude,
            @RequestParam(value = "longitude", required = false) String longitude,
            @RequestParam(value = "telephone", required = false) String telephone,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "password2", required = false) String password2,
            @RequestParam(value = "action", required = false) String action,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        LOG.info("post updateUser called for username=" + username);

        // security check if party is allowed to access or modify this party
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (UserRole.ANONYMOUS.equals(sessionUser.getUserRole())) {
            errorMessage = "you must be logged in to access users information";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            if (!sessionUser.getUsername().equals(username)) {
                errorMessage = "security viewModifyUser called for non admin username " + username
                        + "which is not the logged in user =" + sessionUser.getUsername();
                model.addAttribute("errorMessage", errorMessage);
                LOG.warn(errorMessage);
                model.addAttribute("availableItems", shoppingService.getAvailableItems());
                model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
                model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
                return "home";
            }
        }

        List<User> userList = userRepository.findByUsername(username);
        if (userList.isEmpty()) {
            errorMessage = "update user called for unknown username:" + username;
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        User modifyUser = userList.get(0);

        // update password if requested
        if ("updatePassword".equals(action)) {
            if (password == null || !password.equals(password2) || password.length() < 8) {
                errorMessage = "you must enter two identical passwords with atleast 8 characters";
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                return "viewModifyUser";
            } else {
                modifyUser.setPassword(password);
                modifyUser = userRepository.save(modifyUser);
                model.addAttribute("modifyUser", modifyUser);
                message = "password updated for user :" + modifyUser.getUsername();
                model.addAttribute("message", message);
                return "viewModifyUser";
            }
        }

        // else update all other properties
        // only admin can update modifyUser role aand enabled
        if (UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            try {
                UserRole role = UserRole.valueOf(userRole);
                modifyUser.setUserRole(role);
                if (userEnabled != null && "true".equals(userEnabled)) {
                    modifyUser.setEnabled(Boolean.TRUE);
                } else {
                    modifyUser.setEnabled(Boolean.FALSE);
                }
            } catch (Exception ex) {
                errorMessage = "cannot parse userRole" + userRole;
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("availableItems", shoppingService.getAvailableItems());
                model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
                model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
                return "home";
            }
        }

        if (firstName != null) {
            modifyUser.setFirstName(firstName);
        }
        if (secondName != null) {
            modifyUser.setSecondName(secondName);
        }

        Address address = new Address();
        address.setHouseNumber(houseNumber);
        address.setAddressLine1(addressLine1);
        address.setAddressLine2(addressLine2);
        address.setCity(city);
        address.setCounty(county);
        address.setCountry(country);

        address.setPostcode(postcode);
        address.setMobile(mobile);
        address.setTelephone(telephone);

        modifyUser.setAddress(address);

        modifyUser = userRepository.save(modifyUser);

        model.addAttribute("modifyUser", modifyUser);

        // add message if there are any 
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("message", "User " + modifyUser.getUsername() + " updated successfully");

        model.addAttribute("selectedPage", "home");

        return "viewModifyUser";
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