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
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.solent.com504.oodd.cart.dao.impl.UserRepository;
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
public class UserRepositoryTest {

    private static final Logger LOG = LogManager.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUser_whenSaveUser_thenSaveUser() {
        LOG.debug("****************** starting save test");

        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("cg101");
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        userRepository.save(user1);

        assertEquals(1, userRepository.count());
        
        LOG.debug("****************** save test complete");
    }
    @Test
    public void givenUser_whenUpdateUser_thenUpdateUser() {
        LOG.debug("****************** starting update test");

        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("cg101");
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        user1 = userRepository.save(user1);
        
        user1.setFirstName("test");

        Optional<User> optional = userRepository.findById(user1.getId());
        User foundUser = optional.get();

        LOG.debug("found user: " + foundUser);
        
        assertEquals("test", user1.getFirstName());
        
        List<User> foundUsers1 = userRepository.findByUsername("cg101");
        LOG.debug("found user21: " + foundUsers1);
        
        List<User> foundUsers = userRepository.findByNames("craig", "gallen");
        LOG.debug("found user3: " + foundUsers);
        

        LOG.debug("****************** update test complete");
    }
    @Test
    public void givenUser_whenDeleteUser_thenDeleteUser() {
        LOG.debug("****************** starting test");

        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("cg101");
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        userRepository.save(user1);
        assertEquals(1, userRepository.count());
        userRepository.delete(user1);
        assertEquals(0, userRepository.count());

        LOG.debug("****************** delete test complete");
    }
    @Test
    public void givenUsername_whenFindByUsername_thenReturnUserWithUsername() {
        LOG.debug("****************** starting findByUsername test");

        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("cg101");
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        userRepository.save(user1);
               
        List<User> foundUsers = userRepository.findByUsername("cg101");
        assertEquals(1, foundUsers.size());
              
        LOG.debug("****************** findByUsername test complete");
    }
    @Test
    public void givenNames_whenFindByNames_thenReturnUserWithNames() {
        LOG.debug("****************** starting findByNames test");

        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("cg101");
        user1.setFirstName("craig");
        user1.setSecondName("gallen");
        userRepository.save(user1);
               
        List<User> foundUsers = userRepository.findByNames("craig","gallen");
        assertEquals(1, foundUsers.size());
              
        LOG.debug("****************** findByNames test complete");
    }

}
