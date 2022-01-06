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

import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.solent.com504.oodd.cart.dao.impl.InvoiceItemRepository;
import org.solent.com504.oodd.cart.model.dto.InvoiceItem;
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
public class InvoiceItemRepositoryTest {

    private static final Logger LOG = LogManager.getLogger(InvoiceItemRepositoryTest.class);

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Test
    public void givenItem_whenSaveItem_thenSaveItem() {
        LOG.debug("****************** starting save test");

        invoiceItemRepository.deleteAll();

        InvoiceItem invoiceItem1 = new InvoiceItem();
        invoiceItem1.setName("item 1");
        invoiceItem1.setPrice(100.1);
        invoiceItem1.setQuantity(1);
        invoiceItem1.setUuid(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem1);

        assertEquals(1, invoiceItemRepository.count());

        LOG.debug("****************** save test complete");
    }
    @Test
    public void givenItem_whenDeleteItem_thenDeleteItem() {
        LOG.debug("****************** starting delete test");

        invoiceItemRepository.deleteAll();

        InvoiceItem invoiceItem1 = new InvoiceItem();
        invoiceItem1.setName("item 1");
        invoiceItem1.setPrice(100.1);
        invoiceItem1.setQuantity(1);
        invoiceItem1.setUuid(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem1);

        assertEquals(1, invoiceItemRepository.count());
        
        invoiceItemRepository.delete(invoiceItem1);
        assertEquals(0, invoiceItemRepository.count());
        
        LOG.debug("****************** delete test complete");
    }
    @Test
    public void givenItem_whenUpdateItem_thenUpdateItem() {
        LOG.debug("****************** starting update test");

        invoiceItemRepository.deleteAll();

        InvoiceItem invoiceItem1 = new InvoiceItem();
        invoiceItem1.setName("item 1");
        invoiceItem1.setPrice(100.1);
        invoiceItem1.setQuantity(1);
        invoiceItem1.setUuid(UUID.randomUUID().toString());
        invoiceItem1 = invoiceItemRepository.save(invoiceItem1);

        assertEquals(1, invoiceItemRepository.count());
        
        invoiceItem1.setPrice(45.0);
        
        InvoiceItem invoiceItem = invoiceItemRepository.getOne(invoiceItem1.getId());
        assertTrue(invoiceItem.getPrice().equals(45.0));
        
        LOG.debug("****************** update test complete");
    }
}
