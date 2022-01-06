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
package org.solent.com504.oodd.cart.spring.web;

import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.service.ServiceObjectFactory;
import org.solent.com504.oodd.cart.spring.service.ServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author cgallen, kpeacock
 */
@Configuration
@Import(ServiceConfiguration.class)
@PropertySource("classpath:persistence-app.properties")
public class SpringBootJspConfiguration {

    /**
     * 
     * @return a new shopping cart object
     * @see https://www.baeldung.com/spring-mvc-session-attributes
     */
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ShoppingCart getNewShoppingCart() {
        return ServiceObjectFactory.getNewShoppingCart();
    }
}
