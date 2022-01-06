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
package org.solent.com504.oodd.cart.dao.test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author cgallen
 * @author kpeacock
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the OrderServiceConfig class
@ContextConfiguration(classes = DAOTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class ShoppingItemCatalogRepositoryTest {

    private static final Logger LOG = LogManager.getLogger(ShoppingItemCatalogRepositoryTest.class);

    @Autowired
    private ShoppingItemCatalogRepository shoppingItemCatalogRepository;

    @Test
    public void givenItem_whenSaveItem_thenSaveItem() {
        LOG.debug("****************** starting save test");

        shoppingItemCatalogRepository.deleteAll();

        ShoppingItem shoppingItem1 = new ShoppingItem();
        shoppingItem1.setName("item 1");
        shoppingItem1.setPrice(100.1);
        shoppingItem1.setQuantity(1);
        shoppingItem1.setUuid(UUID.randomUUID().toString());
        shoppingItemCatalogRepository.save(shoppingItem1);

        assertEquals(1, shoppingItemCatalogRepository.count());

        Optional<ShoppingItem> optional = shoppingItemCatalogRepository.findById(shoppingItem1.getId());
        ShoppingItem foundItem = optional.get();
        assertEquals("item 1", foundItem.getName());

        LOG.debug("****************** save test complete");
    }
    @Test
    public void givenItem_whenDeleteItem_thenDeleteItem() {
        LOG.debug("****************** starting delete test");

        shoppingItemCatalogRepository.deleteAll();

        ShoppingItem shoppingItem1 = new ShoppingItem();
        shoppingItem1.setName("item 1");
        shoppingItem1.setPrice(100.1);
        shoppingItem1.setQuantity(1);
        shoppingItem1.setUuid(UUID.randomUUID().toString());
        
        shoppingItemCatalogRepository.save(shoppingItem1);
        assertEquals(1, shoppingItemCatalogRepository.count());
        
        shoppingItemCatalogRepository.delete(shoppingItem1);
        assertEquals(0, shoppingItemCatalogRepository.count());
        
        LOG.debug("****************** delete test complete");
    }
    @Test
    public void givenItem_whenUpdateItem_thenUpdateItem() {
        LOG.debug("****************** starting update test");

        shoppingItemCatalogRepository.deleteAll();

        ShoppingItem shoppingItem1 = new ShoppingItem();
        shoppingItem1.setName("item 1");
        shoppingItem1.setPrice(100.1);
        shoppingItem1.setQuantity(1);
        shoppingItem1.setUuid(UUID.randomUUID().toString());
        
        shoppingItem1 = shoppingItemCatalogRepository.save(shoppingItem1);
        assertEquals(1, shoppingItemCatalogRepository.count());
        
        shoppingItem1.setPrice(45.0);
        
        ShoppingItem shoppingItem = shoppingItemCatalogRepository.getOne(shoppingItem1.getId());
        assertTrue(shoppingItem.getPrice().equals(45.0));
        
        LOG.debug("****************** update test complete");
    }
    @Test
    public void givenName_whenFindByName_thenReturnItemWithName() {
        LOG.debug("****************** starting findByName test");

        shoppingItemCatalogRepository.deleteAll();

        ShoppingItem shoppingItem1 = new ShoppingItem();
        shoppingItem1.setName("item 1");
        shoppingItem1.setPrice(100.1);
        shoppingItem1.setQuantity(1);
        shoppingItem1.setUuid(UUID.randomUUID().toString());
        
        shoppingItemCatalogRepository.save(shoppingItem1);
        assertEquals(1, shoppingItemCatalogRepository.count());
        
        ShoppingItem shoppingItem = shoppingItemCatalogRepository.findByName("item 1");
        assertEquals("item 1", shoppingItem.getName());
        
        LOG.debug("****************** findByName test complete");
    }
    @Test
    public void givenName_whenFindByPartialName_thenReturnItemWithPartialName() {
        LOG.debug("****************** starting findByName test");

        shoppingItemCatalogRepository.deleteAll();

        ShoppingItem shoppingItem1 = new ShoppingItem();
        shoppingItem1.setName("item 1");
        shoppingItem1.setPrice(100.1);
        shoppingItem1.setQuantity(1);
        shoppingItem1.setUuid(UUID.randomUUID().toString());
        
        shoppingItemCatalogRepository.save(shoppingItem1);
        assertEquals(1, shoppingItemCatalogRepository.count());
        
        List<ShoppingItem> shoppingItems = shoppingItemCatalogRepository.findByPartialName("iTeM");
        assertEquals("item 1", shoppingItems.get(0).getName());
        
        LOG.debug("****************** findByPartialName test complete");
    }

}
