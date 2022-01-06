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
 * Class used to model shopping items.
 * @author cgallen
 * @author kpeacock
 */
@Entity
public class ShoppingItem {

    private Long id;
    private String uuid = null;
    private String name = null;
    private Integer quantity = 0;
    private Double price = 0.0;
    private Integer stock = 0;
    /**
    * Creates a new empty shopping item.
    */
    public ShoppingItem(){
        
    }
    /**
    * Creates a new shopping item.
    *
    * @param name the name to set
    * @param price the price to set
    */
    public ShoppingItem(String name, Double price) {
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
    /**
    * @return the item stock
    */
    public Integer getStock() {
        return stock;
    }
    /**
    * @param stock the stock to set
    */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" + "uuid=" + uuid + ", name=" + name + ", quantity=" + quantity + ", stock=" + stock + ", price=" + price + '}';
    }

}
