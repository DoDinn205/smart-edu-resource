/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.service.PaymentService;
import com.paq.utils.constant.PaymentMethodEnum;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api/secure/student")
public class ApiStudentPaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments")
    public ResponseEntity<?> getMyPayments(
            Principal principal) {
        return ResponseEntity.ok(
                this.paymentService.getMyPayments(
                        principal.getName()
                )
        );
    }

    @PostMapping("/payments/{enrollmentId}")
    public ResponseEntity<?> createPayment(
            @PathVariable int enrollmentId,
            @RequestParam PaymentMethodEnum method,
            Principal principal) {
        return ResponseEntity.ok(
                this.paymentService.createPayment(
                        enrollmentId,
                        method,
                        principal.getName()
                )
        );
    }
}
