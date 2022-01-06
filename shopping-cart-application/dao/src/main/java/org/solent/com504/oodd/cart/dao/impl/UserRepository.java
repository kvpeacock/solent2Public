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
import org.solent.com504.oodd.cart.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** 
* A JpaRepository used to store type {@link User}. 
* @see org.springframework.data.jpa.repository.JpaRepository;
* @author cgallen, kpeacock
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        
    /** 
    * A Spring Query that returns a list of type {@link User} where user.username matches the specified parameter. 
    * @param username The username that should be queried against.
    * @return A List of {@link User} objects, where the username matches the <code>username</code> parameter.
    * @see org.springframework.data.jpa.repository.Query;
    */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public List<User> findByUsername(@Param("username")String username);

    /** 
    * A Spring Query that returns a list of type {@link User} where user.firstName matches the firstName parameter,
    * and user.secondName matches the secondName parameter.
    * @param firstName The <code>firstName</code> that should be queried against.
    * @param secondName The <code>secondName</code> that should be queried against.
    * @return A List of {@link User} objects, where the username matches the <code>username</code> parameter.
    * @see org.springframework.data.jpa.repository.Query;
    */
    @Query("SELECT u FROM User u WHERE u.firstName = :firstName and u.secondName = :secondName")
    public List<User> findByNames(@Param("firstName") String firstName, @Param("secondName") String secondName);

}
