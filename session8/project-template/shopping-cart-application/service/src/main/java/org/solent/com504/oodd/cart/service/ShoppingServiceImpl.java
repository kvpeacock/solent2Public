/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.service;

import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author cgallen
 * @author kpeacock
 */



@Component
public class ShoppingServiceImpl implements ShoppingService {
    
    final static Logger LOG = LogManager.getLogger("Transaction_Logger");
    
    
    @Autowired
    private ShoppingItemCatalogRepository shoppingItemCatalogRepository;

    public ShoppingServiceImpl() {
    }

    @Override
    public List<ShoppingItem> getAvailableItems() {
        
        List<ShoppingItem> itemList = shoppingItemCatalogRepository.findAll();
        return itemList;
    }

    @Override
    public boolean purchaseItems(ShoppingCart shoppingCart) {
        System.out.println("purchased items");
        for (ShoppingItem shoppingItem : shoppingCart.getShoppingCartItems()) {
            System.out.println(shoppingItem);
        }

        return true;
    }

    @Override
    public ShoppingItem getNewItemByUUID(String uuid) {
        ShoppingItem templateItem = shoppingItemCatalogRepository.findByUuid(uuid);
        if(templateItem==null) return null;
        ShoppingItem item = new ShoppingItem();
        item.setName(templateItem.getName());
        item.setPrice(templateItem.getPrice());
        item.setStock(templateItem.getStock());
        item.setQuantity(0);
        item.setUuid(UUID.randomUUID().toString());
        return item;
    }
}
