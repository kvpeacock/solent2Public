package org.solent.com504.oodd.cart.model.dto;

import javax.persistence.Embeddable;
/**
 * Class used to model addresses.
 * @author cgallen,kpeacock
 */
@Embeddable
public class Address {

    private String houseNumber;

    private String addressLine1;

    private String addressLine2;
    
    private String county;
    
    private String city;

    private String postcode;

    private String mobile;

    private String telephone;

    private String country;

    /**
    * @return stored houseNumber of the address
    */
    public String getHouseNumber() {
        return houseNumber;
    }
    /**
     * @param houseNumber The house number to set.
     */
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    /**
     * @return stored addressLine1
     */
    public String getAddressLine1() {
        return addressLine1;
    }
    /**
     * @param addressLine1 The address line 1 to set.
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
    * @return stored addressLine2 of the address
    */
    public String getAddressLine2() {
        return addressLine2;
    }
    /**
     * @param addressLine2 The address line 2 to set.
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
    * @return stored postcode of the address
    */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @param postcode The postcode to set.
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    /**
    * @return stored mobile of the address
    */
    public String getMobile() {
        return mobile;
    }
    /**
     * @param mobile The mobile number to set.
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    /**
    * @return stored telephone of the address
    */
    public String getTelephone() {
        return telephone;
    }
    /**
     * @param telephone The telephone number to set.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    /**
    * @return stored country of the address
    */
    public String getCountry() {
        return country;
    }
    /**
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }
    /**
    * @return stored county of the address
    */
    public String getCounty() {
        return county;
    }
    /**
     * @param county The county to set.
     */
    public void setCounty(String county) {
        this.county = county;
    }
    /**
    * @return stored city of the address
    */
    public String getCity() {
        return city;
    }
    /**
     * @param city The city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" + "houseNumber=" + houseNumber + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", county=" + county + ", city=" + city + ", postcode=" + postcode + ", mobile=" + mobile + ", telephone=" + telephone + ", country=" + country + '}';
    }
    
    


    
    
}
