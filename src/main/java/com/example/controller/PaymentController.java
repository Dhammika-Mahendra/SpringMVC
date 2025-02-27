package com.example.controller;

import com.example.model.Payment;
import com.example.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    // Create a new payment
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Payment payment) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate the payment first
            if (!payment.isValidPayment()) {
                response.put("success", false);
                response.put("message", "Invalid payment data. Please check amount, payment method, and status.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Save the payment
            boolean savedPayment = paymentRepository.createPayment(payment);

            if (!savedPayment) {
                response.put("success", false);
                response.put("message", "Failed to save payment. Please try again.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Payment processing failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}