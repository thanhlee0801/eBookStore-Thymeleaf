package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.PaymentDetail;
import com.vn.ebookstore.repository.PaymentDetailRepository;
import com.vn.ebookstore.service.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Override
    public PaymentDetail createPaymentDetail(PaymentDetail paymentDetail) {
        return paymentDetailRepository.save(paymentDetail);
    }

    @Override
    public PaymentDetail updatePaymentDetail(int id, PaymentDetail paymentDetail) {
        PaymentDetail existingPayment = paymentDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("PaymentDetail not found"));
        existingPayment.setAmount(paymentDetail.getAmount());
        existingPayment.setProvider(paymentDetail.getProvider());
        existingPayment.setStatus(paymentDetail.getStatus());
        return paymentDetailRepository.save(existingPayment);
    }

    @Override
    public void deletePaymentDetail(int id) {
        paymentDetailRepository.deleteById(id);
    }

    @Override
    public PaymentDetail getPaymentDetailById(int id) {
        return paymentDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("PaymentDetail not found"));
    }

    @Override
    public List<PaymentDetail> getAllPaymentDetails() {
        return paymentDetailRepository.findAll();
    }
}