/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class used to model invoice items.
 * @author kpeacock
 */
@Entity
public class InvoiceItem {
    
    private Long id;
    private String uuid=null;
    private String name=null;
    private Integer quantity=0;
    private Double price=0.0;
    
    /**
    * Creates an empty invoice item.
    */
    public InvoiceItem(){
        }
    /**
    * Creates a new invoice item.
    * @param name the name to set
    * @param price the price to set
    */
    public InvoiceItem(String name, Double price) {
        this.name = name;
        this.price = price;
    }
    /**
    * @return the item id
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
    * @return the item UUID
    */
    public String getUuid() {
        return uuid;
    }
    /**
    * @param uuid the uuid to set
    */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    /**
    * @return the item name
    */
    public String getName() {
        return name;
    }
    /**
    * @param name the name to set
    */
    public void setName(String name) {
        this.name = name;
    }
    /**
    * @return the item quantity
    */
    public Integer getQuantity() {
        return quantity;
    }
    /**
    * @param quantity the quantity to set
    */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    /**
    * @return the item price
    */
    public Double getPrice() {
        return price;
    }
    /**
    * @param price the price to set
    */
    public void setPrice(Double price) {
        this.price = price;
    }
    

    @Override
    public String toString() {
        return "InvoiceItem{" + "uuid=" + uuid + ", name=" + name + ", quantity=" + quantity + ", price=" + price + '}';
    }
    
            
    
}
