package com.paq.repository;

import com.paq.pojo.Payment;

public interface PaymentRepository {

    Payment getPaymentById(int id);
}
