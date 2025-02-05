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

import java.util.List;
import java.util.UUID;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the shopping service
 * @author cgallen, kpeacock
 */

@Component
public class ShoppingServiceImpl implements ShoppingService {
       
    @Autowired
    private ShoppingItemCatalogRepository shoppingItemCatalogRepository;

    /** 
    * creates shopping service implementation
    */
    public ShoppingServiceImpl() {
    }

    @Override
    public List<ShoppingItem> getAvailableItems() {
        List<ShoppingItem> itemList = shoppingItemCatalogRepository.findAll();
        return itemList;
    }

    @Override
    public ShoppingItem getNewItemByName(String name) {
        ShoppingItem templateItem = shoppingItemCatalogRepository.findByName(name);
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
