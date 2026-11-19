package io.github.leonardolucas13.payment_orchestrator_java.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name ="orders")
 
public class Order {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; 
    private String customerEmail;
    private Integer amountCents;
    private String status = "pending";
    @CreationTimestamp 
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Integer getAmountCents() {
        return amountCents;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
