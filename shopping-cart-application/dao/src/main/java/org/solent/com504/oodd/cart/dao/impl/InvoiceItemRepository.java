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

import org.solent.com504.oodd.cart.model.dto.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** 
* A JpaRepository used to store type {@link InvoiceItem}. 
* @see org.springframework.data.jpa.repository.JpaRepository;
* @author cgallen
*/
@Repository
public interface InvoiceItemRepository  extends JpaRepository<InvoiceItem,Long>{
}