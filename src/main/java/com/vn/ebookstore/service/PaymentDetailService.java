package com.vn.ebookstore.service;

import com.vn.ebookstore.model.PaymentDetail;
import java.util.List;

public interface PaymentDetailService {
    PaymentDetail createPaymentDetail(PaymentDetail paymentDetail);
    PaymentDetail updatePaymentDetail(int id, PaymentDetail paymentDetail);
    void deletePaymentDetail(int id);
    PaymentDetail getPaymentDetailById(int id);
    List<PaymentDetail> getAllPaymentDetails();
}