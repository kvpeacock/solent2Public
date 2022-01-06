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
package org.solent.com504.oodd.cart.model.service;

import java.util.List;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;

/**
 * Interface used for ShoppingCart functionality.
 * @author cgallen
 * @author kpeacock
 */
public interface ShoppingCart {

    /** 
    * Returns a list of all {@link ShoppingItem} in the session's <code>Cart</code>.
    * @return a List of type {@link ShoppingItem} of all the items in the <code>Cart</code>.     
    */
    public List<ShoppingItem> getShoppingCartItems();
    
    /** 
    * Adds {@link ShoppingItem} shoppingItem to the session's <code>Cart</code>.
    * @param shoppingItem The {@link ShoppingItem} to add to the <code>Cart</code>.
    * @return A string about the status of the attempted addition - either "", denoting that the item has been added, "Out of Stock", or "Item Not Found".
    */
    public String addItemToCart(ShoppingItem shoppingItem);
    
    /** 
    * Removes {@link ShoppingItem} shoppingItem from the session's <code>Cart</code>.
    * @param itemUuid The item UUID of the{@link ShoppingItem} to be removed from the <code>Cart</code>.
    */
    public void removeItemFromCart(String itemUuid);
    
    /** 
    * Returns the total cost of all the items within the session's <code>Cart</code>.
    * @return  A double representing the total cost of all the items within the session's <code>Cart</code>.
    */
    public double getTotal();
    
}
