/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.dto.InvoiceItem;
import org.solent.com504.oodd.cart.model.service.ShoppingService;

/**
 *
 * @author cgallen
 * @author kpeacock
 */
public class ShoppingServiceImpl implements ShoppingService {

    private HashMap<String, ShoppingItem> itemMap = new HashMap<String, ShoppingItem>();

    private List<ShoppingItem> itemlist = Arrays.asList(new ShoppingItem("house", 20000.00),
            new ShoppingItem("hen", 5.00),
            new ShoppingItem("car", 5000.00),
            new ShoppingItem("pet ", 65.00)
    );

    public ShoppingServiceImpl() {

        // initialised the hashmap
        for (ShoppingItem item : itemlist) {
            itemMap.put(item.getName(), item);
        }
    }

    @Override
    public List<ShoppingItem> getAvailableItems() {
        return itemlist;
    }

    @Override
    public boolean purchaseItems(ShoppingCart shoppingCart) {
        System.out.println("purchased ite");
        InvoiceItem test = new InvoiceItem("EEEE",5,5.0);
        
        for (ShoppingItem shoppingItem : shoppingCart.getShoppingCartItems()) {
            System.out.println(shoppingItem.getName());
            System.out.println(shoppingItem.getQuantity());
            System.out.println((shoppingItem.getQuantity()*shoppingItem.getPrice()));
            System.out.println(test.toString());
        }

        return true;
    }

    @Override
    public ShoppingItem getNewItemByName(String name) {
        ShoppingItem templateItem = itemMap.get(name);
        
        if(templateItem==null) return null;
        
        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setPrice(templateItem.getPrice());
        item.setQuantity(0);
        item.setUuid(UUID.randomUUID().toString());
        return item;
    }

}
