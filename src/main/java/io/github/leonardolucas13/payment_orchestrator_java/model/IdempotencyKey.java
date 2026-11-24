package io.github.leonardolucas13.payment_orchestrator_java.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name ="idempotency_keys")
 
public class IdempotencyKey {
    @Id 
    @Column (name = "`key`")
    private String key; 
    private String requestHash;
    private String status = "processing";
    @JdbcTypeCode(SqlTypes.JSON)
    private String responseBody;
    private Integer responseCode;
    @CreationTimestamp 
    private OffsetDateTime createdAt;

    protected IdempotencyKey() {
    }

    public IdempotencyKey(String key, String requestHash) {
        this.key = key;
        this.requestHash = requestHash;
    }

    public String    getKey() {
        return key;
    }

    public String getRequestHash() {
        return requestHash;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
    
    public String getStatus() {
        return status;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

}
