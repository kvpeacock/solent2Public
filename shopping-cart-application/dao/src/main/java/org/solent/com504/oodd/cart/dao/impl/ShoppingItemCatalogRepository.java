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
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** 
* A JpaRepository used to store type {@link Shoppingtem}. 
* @see org.springframework.data.jpa.repository.JpaRepository;
* @author cgallen, kpeacock
*/
@Repository
public interface ShoppingItemCatalogRepository  extends JpaRepository<ShoppingItem,Long>{
    /** 
    * A Spring Query that returns a list of type {@link ShoppingItem} where shoppingItem.name matches the specified parameter. 
    * @param name The item name that should be queried against.
    * @return A List of {@link ShoppingItem} objects, where the item name matches the <code>name</code> parameter.
    * @see org.springframework.data.jpa.repository.Query;
    */
    @Query("SELECT s FROM ShoppingItem s WHERE s.name = :name")
    public ShoppingItem findByName(@Param("name")String name);
    
    /** 
    * A Spring Query that returns a list of type {@link ShoppingItem} where shoppingItem.name starts with the specified parameter. 
    * @param name The partial item name that should be queried against. Not case sensitive.
    * @return A List of {@link ShoppingItem} objects, where the item name starts with the <code>name</code> parameter.
    * @see org.springframework.data.jpa.repository.Query;
    */
    @Query("SELECT s FROM ShoppingItem s WHERE LOWER(s.name) LIKE LOWER(concat(?1, '%'))")
    public List<ShoppingItem> findByPartialName(@Param("name")String name);
}