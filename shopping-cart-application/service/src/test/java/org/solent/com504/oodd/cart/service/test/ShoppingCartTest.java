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
package org.solent.com504.oodd.cart.service.test;

import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.service.ServiceObjectFactory;
/**
 *
 * @author cgallen
 * @author kpeacock
 */
public class ShoppingCartTest {
    
    final static Logger LOG = LogManager.getLogger(ShoppingCartTest.class);
    
    ShoppingCart shoppingCart = null;

    @Before
    public void before() {
        shoppingCart = ServiceObjectFactory.getNewShoppingCart();
        shoppingCart.getShoppingCartItems().clear();
    }

    @Test
    public void test1() {
        assertNotNull(shoppingCart);
    }

    @Test
    public void givenItem_whenOutOfStock_thenReturnStatus() {
        assertNotNull(shoppingCart);
        List<ShoppingItem> items = shoppingCart.getShoppingCartItems(); 
        assertTrue(items.isEmpty());
        
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setStock(0);
        String status = shoppingCart.addItemToCart(shoppingItem);
        LOG.debug("Out of stock test: Attempted to add item to cart - status: " + status);
        
        assertEquals("Out of Stock", status);

    }
    
    @Test
    public void givenItem_whenInStock_thenAddToCart() {
        assertNotNull(shoppingCart);
        List<ShoppingItem> items = shoppingCart.getShoppingCartItems();
        assertTrue(items.isEmpty());
        
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setStock(1);
        
        shoppingCart.addItemToCart(shoppingItem);
        
        assertEquals(1, shoppingCart.getShoppingCartItems().size());
    }
    
    @Test
    public void givenItem_whenDeleted_thenDeleteItem() {
        assertNotNull(shoppingCart);
        
        List<ShoppingItem> items = shoppingCart.getShoppingCartItems();
        assertTrue(items.isEmpty());
        
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setStock(1);
        String uuid = UUID.randomUUID().toString();
        shoppingItem.setUuid(uuid);
        
        shoppingCart.addItemToCart(shoppingItem);
        
        assertEquals(1, shoppingCart.getShoppingCartItems().size());
       
        shoppingCart.removeItemFromCart(uuid);
   
        assertEquals(0, shoppingCart.getShoppingCartItems().size());
    }
    @Test
    public void givenItem_whenCalculateTotal_thenReturnTotal() {
        assertNotNull(shoppingCart);
        
        List<ShoppingItem> items = shoppingCart.getShoppingCartItems();
        assertTrue(items.isEmpty());

        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.setStock(1);
      //  shoppingItem.setUuid("A");
        shoppingItem.setPrice(5.0);
        shoppingCart.addItemToCart(shoppingItem);
        shoppingItem.setQuantity(3);
               
        assertEquals(15.00, shoppingCart.getTotal(),0.001);
    }
}
