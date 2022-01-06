package org.solent.com504.oodd.cart.model.dto;

/**
 * Enumerator used to represent user roles
 * @author cgallen, kpeacock
 */
public enum UserRole {
    /**
     * Default role - any user who is viewing the site but hasn't logged in.
    */
    ANONYMOUS,
    /**
     * Any user who has created an account, but is not an admin.
    */
    CUSTOMER, 
    /**
     *Able to add and remove and modify items from the catalogue. 
     *Able to able to view and modify users in the system.
     *Able to able to view and modify orders in the system.
    */
    ADMINISTRATOR
}
