package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "PAYMENTS", schema = "DHAMMIKA")
public class Payment {

    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @SequenceGenerator(name = "payment_seq", sequenceName = "PAYMENT_SEQ", allocationSize = 1)
    @Column(name = "PAYMENT_ID", nullable = false)
    private Long paymentId;

    @Column(name = "AMOUNT", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "PAYMENT_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Column(name = "PAYMENT_METHOD", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Column(name = "STATUS", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // Enums for payment method and status
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, CASH, CHEQUE
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED, CANCELLED
    }

    // Default constructor required by JPA
    public Payment() {
    }

    // Constructor with required fields
    public Payment(BigDecimal amount, Date paymentDate, PaymentMethod paymentMethod, PaymentStatus status) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }



    // Pre-persist hook to set defaults before saving
    @PrePersist
    public void prePersist() {
        if (paymentDate == null) {
            paymentDate = new Date();
        }
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }

    // Business validation method
    public boolean isValidPayment() {
        return amount != null &&
                amount.compareTo(BigDecimal.ZERO) > 0 &&
                paymentMethod != null &&
                status != null;
    }

    // Business method to process refund
    public boolean refund() {
        if (status == PaymentStatus.COMPLETED) {
            status = PaymentStatus.REFUNDED;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod=" + paymentMethod +
                ", status=" + status +
                '}';
    }
}