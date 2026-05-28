package com.paq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paq.pojo.Payment;
import com.paq.pojo.response.ResPaymentDTO;
import com.paq.pojo.response.ResPaymentStatsDTO;
import com.paq.repository.PaymentRepository;
import com.paq.service.PaymentService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.PaymentMethodEnum;
import com.paq.utils.constant.PaymentStatusEnum;
import com.paq.utils.error.IdInvalidException;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResPaymentDTO> getPayments(Map<String, String> params) {
        this.permissionService.requireAdmin();
        return this.paymentRepo.getPayments(params).stream()
                .map(DTOMapper::toResPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResPaymentDTO getPaymentById(int id) {
        this.permissionService.requirePaymentOwnerOrAdmin(id);

        Payment payment = this.paymentRepo.getPaymentById(id);
        if (payment == null) {
            throw new IdInvalidException("Payment không tồn tại");
        }

        return DTOMapper.toResPaymentDTO(payment);
    }

    @Override
    public ResPaymentDTO updatePaymentStatus(int id, PaymentStatusEnum status) {
        this.permissionService.requireAdmin();

        Payment payment = this.paymentRepo.getPaymentById(id);
        if (payment == null) {
            throw new IdInvalidException("Payment không tồn tại");
        }

        payment.setStatus(status);
        return DTOMapper.toResPaymentDTO(this.paymentRepo.updatePayment(payment));
    }

    @Override
    public ResPaymentStatsDTO getPaymentStats(Map<String, String> params) {
        this.permissionService.requireAdmin();

        ResPaymentStatsDTO dto = new ResPaymentStatsDTO();
        dto.setTotalRevenue(this.paymentRepo.getTotalRevenue(params));
        dto.setTotalTransactions(this.paymentRepo.countPayments(params));
        dto.setSuccessfulTransactions(this.paymentRepo.countPaymentsByStatus(PaymentStatusEnum.SUCCESS, params));
        dto.setPendingTransactions(this.paymentRepo.countPaymentsByStatus(PaymentStatusEnum.PENDING, params));
        dto.setRefundedTransactions(this.paymentRepo.countPaymentsByStatus(PaymentStatusEnum.REFUNDED, params));
        dto.setCancelledTransactions(this.paymentRepo.countPaymentsByStatus(PaymentStatusEnum.CANCELLED, params));

        Map<String, Long> methodCounts = new HashMap<>();
        for (Map.Entry<PaymentMethodEnum, Long> entry : this.paymentRepo.countPaymentsByMethod(params).entrySet()) {
            methodCounts.put(entry.getKey().name(), entry.getValue());
        }
        dto.setMethodCounts(methodCounts);

        return dto;
    }
}
