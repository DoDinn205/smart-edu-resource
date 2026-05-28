package com.paq.service;

import com.paq.pojo.response.ResPaymentDTO;
import com.paq.pojo.response.ResPaymentStatsDTO;
import com.paq.utils.constant.PaymentStatusEnum;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    List<ResPaymentDTO> getPayments(Map<String, String> params);

    ResPaymentDTO getPaymentById(int id);

    ResPaymentDTO updatePaymentStatus(int id, PaymentStatusEnum status);

    ResPaymentStatsDTO getPaymentStats(Map<String, String> params);
}
