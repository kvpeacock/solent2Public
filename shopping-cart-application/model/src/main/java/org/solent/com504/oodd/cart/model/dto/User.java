package org.solent.com504.oodd.cart.model.dto;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.solent.com504.oodd.password.PasswordUtils;

/**
 * Class used to model users.
 * @author cgallen,kpeacock
 */
@Entity
public class User {

    private Long id;

    private String firstName = "";

    private String secondName = "";

    private String username = "";

    private String password = "";

    private String hashedPassword = "";

    private Address address;

    private UserRole userRole;

    private Boolean enabled = true;

    /**
    * @return the user Id
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
    */
    public void setId(Long id) {
        this.id = id;
    }
    /**
    * @return the user role
    */
    public UserRole getUserRole() {
        return userRole;
    }
    /**
     * @param userRole the user role to set
    */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    /**
    * @return the user's username
    */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
    */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
    * @return the user's address
    */
    @Embedded
    public Address getAddress() {
        return address;
    }
    /**
     * @param address the address to set
    */
    public void setAddress(Address address) {
        this.address = address;
    }
    /**
    * @return the user's first name
    */
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName the firstName to set
    */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
    * @return the user's second name
    */
    public String getSecondName() {
        return secondName;
    }
    /**
     * @param secondName the secondName to set
    */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    /**
    * @return the user status
    */
    public Boolean getEnabled() {
        return enabled;
    }
    /**
     * @param enabled the user status to set
    */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    /**
     * passwords are not saved in the database - only password hash is saved
     * @return the user password
    */
    @Transient
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
    */
    public void setPassword(String password) {
        this.password = password;
        setHashedPassword(PasswordUtils.hashPassword(password));
    }
    /**
     * Checks if a given password is valid.
     * @param checkPassword the password to check
     * @return <code>boolean</code> - whether the password is valid
    */
    public boolean isValidPassword(String checkPassword) {
        if (checkPassword == null || getHashedPassword() == null) {
            return false;
        }
        return PasswordUtils.checkPassword(checkPassword, getHashedPassword());
    }
    /**
    * @return the user's hashed password
    */
    public String getHashedPassword() {
        return hashedPassword;
    }
    /**
     * @param hashedPassword the hashed password to set
    */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    // no password or hashed password
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", firstName=" + firstName + ", secondName=" + secondName + ", username=" + username + ", password=NOT SHOWN, address=" + address + ", userRole=" + userRole + '}';
    }

}
