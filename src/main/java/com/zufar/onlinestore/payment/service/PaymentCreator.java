package com.zufar.onlinestore.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.zufar.onlinestore.payment.converter.PaymentIntentConverter;
import com.zufar.onlinestore.payment.dto.CreatePaymentDto;
import com.zufar.onlinestore.payment.dto.PaymentDetailsWithTokenDto;
import com.zufar.onlinestore.payment.entity.Payment;
import com.zufar.onlinestore.payment.converter.PaymentConverter;
import com.zufar.onlinestore.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentCreator {

    private final PaymentRepository paymentRepository;
    private final PaymentIntentCreator paymentIntentCreator;
    private final PaymentIntentConverter paymentIntentConverter;
    private final PaymentConverter paymentConverter;

    public PaymentDetailsWithTokenDto createPayment(final CreatePaymentDto createPaymentDto) throws StripeException {
        PaymentIntent paymentIntent = paymentIntentCreator.createPaymentIntent(createPaymentDto);
        log.info("Create payment: payment intent: {} successfully created.", paymentIntent);
        String paymentToken = paymentIntent.getClientSecret();
        Payment payment = paymentIntentConverter.toPayment(paymentIntent);
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Create payment: payment {} successfully saved.", savedPayment);

        return PaymentDetailsWithTokenDto.builder()
                .paymentToken(paymentToken)
                .paymentDetailsDto((paymentConverter.toDto(savedPayment)))
                .build();
    }

}
