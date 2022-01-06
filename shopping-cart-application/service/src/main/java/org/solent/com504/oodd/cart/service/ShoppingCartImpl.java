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
package org.solent.com504.oodd.cart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;

/**
 *
 * @author cgallen
 * @author kpeacock
 */
public class ShoppingCartImpl implements ShoppingCart {
    
    private final HashMap<String, ShoppingItem> itemMap = new HashMap<>();

    @Override
    public List<ShoppingItem> getShoppingCartItems() {
        List<ShoppingItem> itemlist = new ArrayList();
        for (String itemUUID : itemMap.keySet()) {
            ShoppingItem shoppingCartItem = itemMap.get(itemUUID);
            itemlist.add(shoppingCartItem);
        }
        return itemlist;
    }

    @Override
    public String addItemToCart(ShoppingItem shoppingItem) {

        boolean itemExists = false;
        for (String itemUUID : itemMap.keySet()) {
            ShoppingItem shoppingCartItem = itemMap.get(itemUUID);
            if (shoppingCartItem==null) return"Not Found";
            else if (shoppingCartItem.getName().equals(shoppingItem.getName())){
                if (shoppingItem.getStock() <= shoppingCartItem.getQuantity()){
                    return "Out of Stock";
                }
                Integer q = shoppingCartItem.getQuantity();
                shoppingCartItem.setQuantity(q+1);
                return "";
            }
        }
        if (!itemExists){
            if (shoppingItem.getStock()>0){
                shoppingItem.setQuantity(1);
                itemMap.put(shoppingItem.getUuid(), shoppingItem);
                return "";
            }
            else return "Out of Stock";
        }
        return "Item Not Found";
    }

    @Override
    public void removeItemFromCart(String itemUuid) {
        itemMap.remove(itemUuid);
    }

    @Override
    public double getTotal() {
        double total = 0;
        for (String itemUUID : itemMap.keySet()) {
            ShoppingItem shoppingCartItem = itemMap.get(itemUUID);
            total = total + shoppingCartItem.getPrice() * shoppingCartItem.getQuantity();
        }
        return total;
    }
}