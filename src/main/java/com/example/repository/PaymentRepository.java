package com.example.repository;

import com.example.model.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRepository.class);

    @PersistenceContext
    private EntityManager entityManager;
    

    public List<Payment> findAllPayments() {
        try {
            logger.info("Retrieving all payments");
            TypedQuery<Payment> query = entityManager.createQuery(
                    "SELECT e FROM Payment e", Payment.class);
            return query.getResultList();
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving all payments", e);
            return Collections.emptyList();
        }
    }


    @Transactional
    public boolean createPayment(Payment payment) {
        try {
            entityManager.persist(payment);
            entityManager.flush();
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment data: {}", payment, e);
            return false;
        } catch (DataAccessException e) {
            logger.error("Database error while creating payment: {}", payment, e);
            return false;
        }
    }

    @Transactional
    public boolean updatePayment(Payment payment) {
        try {
            if (payment.getPaymentId()== null) {
                logger.error("Cannot update payment with null ID");
                return false;
            }

            logger.info("Updating payment with ID: {}", payment.getPaymentId());
            Payment managedPayment = entityManager.find(Payment.class, payment.getPaymentId());

            if (managedPayment == null) {
                logger.error("Payment with ID {} not found for update", payment.getPaymentId());
                return false;
            }

            // Update fields
            managedPayment.setAmount(payment.getAmount());
            managedPayment.setPaymentDate(payment.getPaymentDate());
            managedPayment.setPaymentMethod(payment.getPaymentMethod());
            managedPayment.setStatus(payment.getStatus());

            entityManager.merge(managedPayment);
            entityManager.flush();
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment data for update: {}", payment, e);
            return false;
        } catch (DataAccessException e) {
            logger.error("Database error while updating payment: {}", payment, e);
            return false;
        }
    }

    @Transactional
    public boolean deleteById(Long empId) {
        try {
            logger.info("Deleting payment with ID: {}", empId);
            Payment payment = entityManager.find(Payment.class, empId);

            if (payment == null) {
                logger.error("Payment with ID {} not found for deletion", empId);
                return false;
            }

            entityManager.remove(payment);
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid ID provided for payment deletion: {}", empId, e);
            return false;
        } catch (DataAccessException e) {
            logger.error("Database error while deleting payment with ID: {}", empId, e);
            return false;
        }
    }


}