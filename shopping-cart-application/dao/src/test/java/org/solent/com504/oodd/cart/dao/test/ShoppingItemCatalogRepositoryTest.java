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

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.solent.com504.oodd.cart.dao.impl.InvoiceRepository;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.dao.impl.UserRepository;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author cgallen
 */
@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the OrderServiceConfig class
@ContextConfiguration(classes = DAOTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class ShoppingItemCatalogRepositoryTest {

    private static final Logger LOG = LogManager.getLogger(ShoppingItemCatalogRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingItemCatalogRepository shoppingItemCatalogRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void testCatalog() {
        LOG.debug("****************** starting test");

        shoppingItemCatalogRepository.deleteAll();

        ShoppingItem shoppingItem1 = new ShoppingItem();
        shoppingItem1.setName("item 1");
        shoppingItem1.setPrice(100.1);
        shoppingItem1.setQuantity(1);
        shoppingItem1.setUuid(UUID.randomUUID().toString());

        shoppingItem1 = shoppingItemCatalogRepository.save(shoppingItem1);

        ShoppingItem shoppingItem2 = new ShoppingItem();
        shoppingItem2.setName("item 1");
        shoppingItem2.setPrice(100.1);
        shoppingItem2.setQuantity(1);
        shoppingItem2.setUuid(UUID.randomUUID().toString());

        shoppingItem2 = shoppingItemCatalogRepository.save(shoppingItem2);

        assertEquals(2, shoppingItemCatalogRepository.count());

        Optional<ShoppingItem> optional = shoppingItemCatalogRepository.findById(shoppingItem2.getId());
        ShoppingItem foundItem = optional.get();

        LOG.debug("found user: " + foundItem);

        LOG.debug("****************** test complete");
    }

}
