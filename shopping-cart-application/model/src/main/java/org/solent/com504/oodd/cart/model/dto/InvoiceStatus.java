/*
 * Copyright 2021 pc.
 *
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
package org.solent.com504.oodd.cart.model.dto;

/**
 * Enumerator used to represent invoice statuses.
 * @author kpeacock
 */
public enum InvoiceStatus {
    /**
    * An invoice that has been fulfilled, with the items purchased confirmed to be with the purchaser.
    */
    FULFILLED,
    /**
    * An invoice that has been rejected, by payment failure or other circumstances.
    */
    REJECTED,
    /**
    * An invoice that has been paid for, but the items have not been confirmed to be with the purchaser.
    */
    PENDING
}


