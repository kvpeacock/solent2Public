/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.model.dto;
import java.time.LocalDateTime;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;

/**
 *
 * @author pc
 */
public class InvoiceItem {
    private String name=null;
    private Integer quantity=0;
    private Double price=0.0;
    private Double totalPrice=0.0;
    
    public InvoiceItem(){
    }
    
    public InvoiceItem(String name, Integer quantity, Double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = price*quantity;
    }
    
        public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "InvoiceItem {name=" + name + ", quantity=" + quantity + ", price=" + price + ", total price =" + totalPrice +'}';
    }
    
    
}

