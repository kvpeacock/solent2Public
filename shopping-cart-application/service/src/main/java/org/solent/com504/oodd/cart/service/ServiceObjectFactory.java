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
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.service.ShoppingService;

/**
 * Creates service objects used for core functionality.
 * @author cgallen, kpeacock
 * 
 */
public class ServiceObjectFactory {
    
    static ShoppingService shoppingService = new ShoppingServiceImpl();
    
    // cannot instantiate
    private ServiceObjectFactory(){
        
    }
    /** 
    * @return the shopping service.
    */
    public static ShoppingService getShoppingService(){
        return shoppingService;
    }
    /** 
    * @return the implementation of the shopping cart interface.
    */
    public static ShoppingCart getNewShoppingCart(){
        return new ShoppingCartImpl();
    }
    
}
