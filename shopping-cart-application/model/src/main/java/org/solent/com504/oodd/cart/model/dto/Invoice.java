package org.solent.com504.oodd.cart.model.dto;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Class used to model invoices.
 * @author cgallen,kpeacock
 */
@Entity
public class Invoice {

    private Long id;

    private String invoiceUUID;

    private Date dateOfPurchase;

    private Double amountDue;

    private List<InvoiceItem> purchasedItems;

    private User purchaser;
    
    private InvoiceStatus status;

    /**
     * @return the invoice Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
    */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return the invoice UUID
    */
    public String getInvoiceUUID() {
        return invoiceUUID;
    }
    /**
     * @param invoiceUUID The UUID to set.
    */
    public void setInvoiceUUID(String invoiceUUID) {
        this.invoiceUUID = invoiceUUID;
    }
    /**
     * @return the DateOfPurchase
    */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }
    /**
     * @param dateOfPurchase The date of purchase to set.
    */
    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }
    /**
     * @return the amount due
    */
    public Double getAmountDue() {
        return amountDue;
    }
    /**
     * @param amountDue The cost of the order.
    */
    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    /**
     * @return a list of purchased items
    */
    @OneToMany
    public List<InvoiceItem> getPurchasedItems() {
        return purchasedItems;
    }
    /**
     * @param purchasedItems list of all items purchased
    */
    public void setPurchasedItems(List<InvoiceItem> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }
    /**
     * @return the user who made the purchase
    */
    @OneToOne
    public User getPurchaser() {
        return purchaser;
    }
    /**
     * @param purchaser the purchaser of the goods
    */
    public void setPurchaser(User purchaser) {
        this.purchaser = purchaser;
    }
    /**
     * @return the status of the invoice
    */
    public InvoiceStatus getStatus() {
        return status;
    }
    /**
     * @param status the invoice status to set
    */
    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Invoice{" + "invoice number=" + invoiceUUID + ", DOP=" + dateOfPurchase + ", amount due=" + amountDue + ", items= "+ purchasedItems + ", purchaser= " + purchaser + ",status= " + status + '}';
    }

}
