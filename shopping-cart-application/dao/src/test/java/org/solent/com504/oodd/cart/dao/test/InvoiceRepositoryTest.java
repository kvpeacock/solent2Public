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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.solent.com504.oodd.cart.dao.impl.InvoiceRepository;
import org.solent.com504.oodd.cart.dao.impl.InvoiceItemRepository;
import org.solent.com504.oodd.cart.dao.impl.UserRepository;
import org.solent.com504.oodd.cart.model.dto.Invoice;
import org.solent.com504.oodd.cart.model.dto.InvoiceItem;
import org.solent.com504.oodd.cart.model.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author cgallen
 * @author kpeacock
 */
@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the OrderServiceConfig class
@ContextConfiguration(classes = DAOTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class InvoiceRepositoryTest {

    private static final Logger LOG = LogManager.getLogger(InvoiceRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void givenInvoice_whenSaveInvoice_thenSaveInvoice() {

        LOG.debug("****************** starting save test");

        invoiceRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        user1 = userRepository.save(user1);
        assertEquals(1, userRepository.count());

        Invoice invoice1 = new Invoice();
        invoice1.setAmountDue(100.0);
        invoice1.setDateOfPurchase(new Date());
        invoice1.setPurchaser(user1);

        invoice1 = invoiceRepository.save(invoice1);
        assertEquals(1, invoiceRepository.count());

        Optional<Invoice> optional = invoiceRepository.findById(invoice1.getId());
        Invoice foundInvoice = optional.get();
        LOG.debug("found Invoice : " + foundInvoice);
  
        LOG.debug("****************** save test complete");
    }
    
    @Test
    public void givenInvoice_whenDeleteInvoice_thenDeleteInvoice() {
        LOG.debug("****************** starting delete test");

        invoiceRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        user1 = userRepository.save(user1);
        assertEquals(1, userRepository.count());

        Invoice invoice1 = new Invoice();
        invoice1.setAmountDue(100.0);
        invoice1.setDateOfPurchase(new Date());
        invoice1.setPurchaser(user1);

        invoice1 = invoiceRepository.save(invoice1);
        assertEquals(1, invoiceRepository.count());

        invoiceRepository.delete(invoice1);
        assertEquals(0, invoiceRepository.count());
        
        LOG.debug("****************** delete test complete");
    }
    @Test
    public void givenInvoiceItem_whenUpdateInvoice_thenUpdateInvoice() {
        LOG.debug("****************** starting update test");

        invoiceRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        user1 = userRepository.save(user1);
        assertEquals(1, userRepository.count());

        Invoice invoice1 = new Invoice();
        invoice1.setAmountDue(100.0);
        invoice1.setDateOfPurchase(new Date());
        invoice1.setPurchaser(user1);

        invoice1 = invoiceRepository.save(invoice1);
        assertEquals(1, invoiceRepository.count());

        // create invoice item
        InvoiceItem invoiceItem1 = new InvoiceItem();
        invoiceItem1.setName("item 1");
        invoiceItem1.setPrice(100.1);
        invoiceItem1.setQuantity(1);
        invoiceItem1.setUuid(UUID.randomUUID().toString());
        invoiceItem1 = invoiceItemRepository.save(invoiceItem1);

        List<InvoiceItem> purchasedItems = new ArrayList<>();
        purchasedItems.add(invoiceItem1);

        invoice1.setPurchasedItems(purchasedItems);
        //invoiceRepository.save(invoice1);

        Optional<Invoice> optional = invoiceRepository.findById(invoice1.getId());
        Invoice foundInvoice = optional.get();
        
        assertEquals(1, foundInvoice.getPurchasedItems().size());
        LOG.debug("****************** update test complete");
    }
    @Test
    public void givenPurchaser_whenfindByPurchaser_thenReturnInvoicesFromPurchaser() {
        LOG.debug("****************** starting findByPurchaser test");

        invoiceRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        user1 = userRepository.save(user1);
        assertEquals(1, userRepository.count());

        Invoice invoice1 = new Invoice();
        invoice1.setAmountDue(100.0);
        invoice1.setDateOfPurchase(new Date());
        invoice1.setPurchaser(user1);
        invoiceRepository.save(invoice1);
        
        Invoice invoice2 = new Invoice();
        invoice2.setAmountDue(100.0);
        invoice2.setDateOfPurchase(new Date());
        invoice2.setPurchaser(user1);
        invoiceRepository.save(invoice2);
          
        assertEquals(2, invoiceRepository.count());

        List<Invoice> userInvoices = invoiceRepository.findByPurchaser(user1);
        assertEquals(2, userInvoices.size());
        LOG.debug("****************** findByPurchaser test complete");
    }
    
    @Test
    public void givenPurchaser_whenfindByInvoiceNumber_thenReturnInvoiceWithInvoiceNumber() {
        LOG.debug("****************** starting findByInvoiceNumber test");

        invoiceRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        user1 = userRepository.save(user1);
        assertEquals(1, userRepository.count());

        Invoice invoice1 = new Invoice();
        invoice1.setAmountDue(100.0);
        invoice1.setDateOfPurchase(new Date());
        String invoiceUUID = UUID.randomUUID().toString();
        invoice1.setInvoiceUUID(invoiceUUID);
        invoice1.setPurchaser(user1);
        invoiceRepository.save(invoice1);
          
        assertEquals(1, invoiceRepository.count());

        Invoice foundInvoice = invoiceRepository.findByInvoiceUUID(invoiceUUID);
        assertEquals(invoiceUUID, foundInvoice.getInvoiceUUID());
                
        LOG.debug("****************** findByInvoiceNumber test complete");
    }
    @Test
    public void givenPurchaser_whenfindByPartialInvoiceNumber_thenReturnInvoiceWithPartialInvoiceNumber() {
        LOG.debug("****************** starting findByPartialInvoiceNumber test");

        invoiceRepository.deleteAll();

        User user1 = new User();
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        user1 = userRepository.save(user1);
        assertEquals(1, userRepository.count());

        Invoice invoice1 = new Invoice();
        invoice1.setAmountDue(100.0);
        invoice1.setDateOfPurchase(new Date());
        String invoiceUUID = UUID.randomUUID().toString();
        invoice1.setInvoiceUUID(invoiceUUID);
        invoice1.setPurchaser(user1);
        invoiceRepository.save(invoice1);
        
        assertEquals(1, invoiceRepository.count());

        List<Invoice> foundInvoices = invoiceRepository.findByPartialInvoiceUUID(invoiceUUID.substring(0,5));
        assertEquals(1, foundInvoices.size());

        assertEquals(invoiceUUID, foundInvoices.get(0).getInvoiceUUID());
                
        LOG.debug("****************** findByPartialInvoiceNumber test complete");
    }
}
