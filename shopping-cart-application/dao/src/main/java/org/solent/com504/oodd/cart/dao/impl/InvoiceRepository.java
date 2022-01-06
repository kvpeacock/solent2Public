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
package org.solent.com504.oodd.cart.dao.impl;


import java.util.List;
import org.solent.com504.oodd.cart.model.dto.Invoice;
import org.solent.com504.oodd.cart.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** 
* A JpaRepository used to store type {@link Invoice}. 
* @see org.springframework.data.jpa.repository.JpaRepository;
* @author cgallen, kpeacock
*/
@Repository
public interface InvoiceRepository  extends JpaRepository<Invoice,Long>{
    
    /** 
    * A Spring Query that returns a list of type {@link Invoice} where invoice.purchaser matches a specified {@link User}. 
    * @param purchaser The {@link User} that should be queried against.
    * @return A List of {@link Invoice} objects, where the purchaser matches the <code>purchaser</code> parameter.
    * @see org.springframework.data.jpa.repository.Query;
    */
    @Query("SELECT i FROM Invoice i WHERE i.purchaser = :purchaser")
    public List<Invoice> findByPurchaser(@Param("purchaser")User purchaser);
    
    /** 
    * A Spring Query that returns a list of type {@link Invoice} where invoice.UUID matches a specified UUID. 
    * @param invoiceUUID The <code>UUID</code> that should be queried against.
    * @return A List of {@link Invoice} objects, where the UUID matches the <code>UUID</code> parameter.
    * @see org.springframework.data.jpa.repository.Query;
    */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceUUID = :invoiceUUID")
    public Invoice findByInvoiceUUID(@Param("invoiceUUID")String invoiceUUID);
    
    /** 
    * A Spring Query that returns a list of type {@link Invoice} where invoice.UUID starts with a specified string. 
    * @param invoiceUUID The partial <code>UUID</code> that should be queried against.
    * @return A List of {@link Invoice} objects, where the UUID starts with the <code>UUID</code> parameter.
    * @see org.springframework.data.jpa.repository.Query;
    */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceUUID LIKE :invoiceUUID%")
    public List<Invoice> findByPartialInvoiceUUID(@Param("invoiceUUID")String invoiceUUID);
}