/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.model.service;

import java.util.List;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;

/**
 * Interface used to provide the shopping service.
 * @author cgallen, kpeacock
 */
public interface ShoppingService {
    /** 
    * Returns a list of all {@link ShoppingItem} within the item catalog.
    * @return a List of type {@link ShoppingItem} of all the items in the {@link ShoppingItemCatalogRepository}
    */
    public List<ShoppingItem> getAvailableItems();

    /** 
    * Returns a {@link ShoppingItem} where item name matches the specified parameter.
    * @param name the name to search for.
    * @return a {@link ShoppingItem} in the {@link ShoppingItemCatalogRepository} where item name matches the name parameter.
    */
    public ShoppingItem getNewItemByName(String name);

}
