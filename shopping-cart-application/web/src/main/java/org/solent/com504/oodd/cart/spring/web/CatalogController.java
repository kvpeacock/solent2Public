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
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
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

/**
 * Adds catalogue CRUD functionality to the web site. 
 * Enforces a rule where only admins can view webpages with CRUD functionality - non-admins will be redirected to the home page
 * @author kpeacock
 */
@Controller
@RequestMapping("/")
public class CatalogController {

    final static Logger LOG = LogManager.getLogger(CatalogController.class);

    @Autowired
    ShoppingItemCatalogRepository itemRepository;

    @Autowired
    ShoppingService shoppingService = null;

    @Autowired
    ShoppingCart shoppingCart = null;

    @Autowired
    private ShoppingItemCatalogRepository shoppingItemCatalogRepository;

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
     * Redirects admins to the viewModifyItem page to view a specific item's details
     * @param name the name of the item to view
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the webpage detailing the specified item's details
     */
    @RequestMapping(value = {"/viewModifyItem"}, method = RequestMethod.GET)
    public String modifyItem(
            @RequestParam(value = "name", required = false) String name,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        // check secure access to modifyItem profile
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            // if not an administrator you can only access your own account info
            errorMessage = "Acess Denied";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        if (name == null || name.isEmpty()) {
            errorMessage = "viewModifyItem called for item with undefined name";
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
            model.addAttribute("availableItems", availableItems);
            model.addAttribute("selectedPage", "admin");
            return "catalog";
        }

        model.addAttribute("selectedPage", "admin");

        LOG.info("get viewModifyItem called for item named" + name);

        ShoppingItem modifyItem = itemRepository.findByName(name);
        if (modifyItem == null) {
            errorMessage = "viewModifyItem called for unknown item=" + name;
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
            model.addAttribute("availableItems", availableItems);
            model.addAttribute("selectedPage", "admin");
            return "catalog";
        }

        model.addAttribute("modifyItem", modifyItem);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        return "viewModifyItem";
    }
/**
 * Allows admins to update and delete {@link ShoppingItem} from the {@link ShoppingItemCatalogRepository}
 * @param uuid the uuid of the {@link ShoppingItem} to update
 * @param newName the name to update the {@link ShoppingItem} to
 * @param name the current name of the {@link ShoppingItem} to update
 * @param price the price to update the {@link ShoppingItem} to
 * @param stock the stock to update the {@link ShoppingItem} to
 * @param action the requested action to perform on the {@link ShoppingItem} - either "update" or "delete"
 * @param model used to access the model holder
 * @param session used to access the current session
 * @return the webpage to return to after the operation is complete
 */
    @RequestMapping(value = {"/viewModifyItem"}, method = RequestMethod.POST)
    public String updateitem(
            @RequestParam(value = "uuid", required = false) String uuid,
            @RequestParam(value = "newName", required = false) String newName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "action", required = false) String action,
            Model model,
            HttpSession session) {
        String message = "";
        String errorMessage = "";

        if (name == null || name.isEmpty() || uuid == null || uuid.isEmpty()) {
            errorMessage = "viewModifyItem called for item with unknown name or UUID";
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
            model.addAttribute("availableItems", availableItems);
            model.addAttribute("selectedPage", "admin");
            return "catalog";
        }

        // security check if party is allowed to access or modify this party
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            errorMessage = "Acess Denied";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }

        ShoppingItem modifyItem = itemRepository.findByName(name);
        if (modifyItem == null) {
            errorMessage = "viewModifyItem called for unknown item=" + name;
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
            model.addAttribute("availableItems", availableItems);
            model.addAttribute("selectedPage", "admin");
            return "catalog";
        }

        LOG.info("post updateItem called for item " + uuid + "name= " + name);

        ShoppingItem cartItemToUpdate = new ShoppingItem();
        boolean cartItemFound = false;
        for (ShoppingItem i : shoppingCart.getShoppingCartItems()) {
            if (i.getName().equals(modifyItem.getName())) {
                cartItemToUpdate = i;
                cartItemFound = true;
                break;
            }
        }

        switch (action) {
            case "update":
                if (newName != null) {
                    for (ShoppingItem i : itemRepository.findAll()) {
                        if (!i.getUuid().equals(modifyItem.getUuid()) && i.getName().equals(newName)) {
                            errorMessage = "Cannot update item - item named " + newName + " already exists";
                            LOG.warn(errorMessage);
                            model.addAttribute("errorMessage", errorMessage);
                            model.addAttribute("modifyItem", modifyItem);
                            return "viewModifyItem";
                        }
                    }

                    modifyItem.setName(newName);
                    if (cartItemFound) {
                        cartItemToUpdate.setName(newName);
                    }
                }

                if (price != null) {
                    modifyItem.setPrice(price);
                    if (cartItemFound) {
                        cartItemToUpdate.setPrice(price);
                    }
                }

                if (stock != null) {
                    modifyItem.setStock(stock);
                }

                modifyItem = itemRepository.save(modifyItem);
                model.addAttribute("modifyItem", modifyItem);
                model.addAttribute("message", "Item " + modifyItem.getName() + " updated successfully");
                LOG.info("Item " + modifyItem.getName() + " updated successfully");
                model.addAttribute("selectedPage", "viewModifyItem");
                return "viewModifyItem";

            case "delete":
                itemRepository.deleteById(modifyItem.getId());
                if (cartItemFound) {
                    shoppingCart.removeItemFromCart(cartItemToUpdate.getUuid());
                }
                model.addAttribute("availableItems", shoppingService.getAvailableItems());
                LOG.info("Item " + name + " deleted");
                model.addAttribute("selectedPage", "admin");
                return "catalog";
            default:
                model.addAttribute("errorMessage", errorMessage);
                List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
                model.addAttribute("availableItems", availableItems);
                model.addAttribute("selectedPage", "admin");
                return "catalog";
        }
    }
    /**
     * Used to redirect the admin to the createNewItem webpage
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the link to the createNewItem webpage
     */
    @RequestMapping(value = "/createNewItem")
    public String createItemPage(Model model, HttpSession session) {
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("selectedPage", "createNewItem");

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            // if not an administrator you can only access your own account info
            String errorMessage = "Acess Denied";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }
        return "createNewItem";
    }
    /**
     * Adds a new {@link ShoppingItem} to the {@link ShoppingItemRepository}, based on the supplied parameters
     * @param name the name of the {@link ShoppingItem} to create
     * @param price the price of the {@link ShoppingItem} to create
     * @param stock the stock of the {@link ShoppingItem} to create
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return a link to the catalog webpage
     */
    @RequestMapping(value = {"/createNewItem"}, method = RequestMethod.POST)
    public String createNewItem(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "stock", required = false) Integer stock,
            Model model,
            HttpSession session) {

        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("selectedPage", "createNewItem");

        if (!UserRole.ADMINISTRATOR.equals(sessionUser.getUserRole())) {
            // if not an administrator you can only access your own account info
            String errorMessage = "Acess Denied";
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("availableItems", shoppingService.getAvailableItems());
            model.addAttribute("shoppingCartItems", shoppingCart.getShoppingCartItems());
            model.addAttribute("shoppingcartTotal", shoppingCart.getTotal());
            return "home";
        }
        if (name == null || name.isEmpty() || price == null || stock == null) {
            String errorMessage = "Cannot create item with undefined name, price or stock";
            LOG.warn(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
            model.addAttribute("availableItems", availableItems);
            model.addAttribute("selectedPage", "admin");
            return "catalog";
        }
        for (ShoppingItem i : itemRepository.findAll()) {
            if (i.getName().equals(name)) {
                String errorMessage = "Cannot create new item - item named " + name + " already exists";
                LOG.warn(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                return "createNewItem";
            }
        }
        ShoppingItem itemToCreate = new ShoppingItem(name, price);
        itemToCreate.setUuid(UUID.randomUUID().toString());
        itemToCreate.setStock(stock);
        shoppingItemCatalogRepository.save(itemToCreate);
        LOG.info("Item " + name + " created");
        List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
        model.addAttribute("availableItems", availableItems);
        model.addAttribute("selectedPage", "admin");
        return "catalog";
    }
    /**
     * Provides the catalog webpage with partial name search funtionality
     * @param name the name to query against
     * @param model used to access the model holder
     * @param session used to access the current session
     * @return the catalog webpage, with only the items that match the defined query
     */
    @RequestMapping(value = "/searchCatalog", method = {RequestMethod.GET, RequestMethod.POST})
    public String catalogSearch(
            @RequestParam(value = "name", required = false) String name,
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

        List<ShoppingItem> shoppingItems = shoppingItemCatalogRepository.findByPartialName(name);
        model.addAttribute("availableItems", shoppingItems);
        model.addAttribute("searchedValue", name);

        // used to set tab selected
        model.addAttribute("selectedPage", "admin");
        return "catalog";
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
