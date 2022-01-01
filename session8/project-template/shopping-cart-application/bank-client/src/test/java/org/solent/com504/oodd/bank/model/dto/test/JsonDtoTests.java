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
package org.solent.com504.oodd.bank.model.dto.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.solent.com504.oodd.bank.model.dto.CreditCard;
import org.solent.com504.oodd.bank.model.dto.TransactionRequestMessage;

/**
 *
 * @author cgallen
 */
public class JsonDtoTests {

    private static final Logger LOG = LogManager.getLogger(JsonDtoTests.class);

    ObjectMapper objectMapper = null;

    @Before
    public void before() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.findAndRegisterModules()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testJsonMessages() throws JsonProcessingException {
        TransactionRequestMessage trequest = new TransactionRequestMessage();
        CreditCard fromCard = new CreditCard();
        trequest.setFromCard(fromCard);
        CreditCard toCard = new CreditCard();
        trequest.setToCard(toCard);

        Double amount = 100.01;

        trequest.setAmount(amount);

        String tRequestString = objectMapper.writeValueAsString(trequest);

        LOG.debug("Json transactionRequest output:\n" + tRequestString);

        TransactionRequestMessage receivedTransactionRequest = objectMapper.readValue(tRequestString, TransactionRequestMessage.class);
        
        assertTrue(trequest.toString().equals(receivedTransactionRequest.toString()));
    }
}
