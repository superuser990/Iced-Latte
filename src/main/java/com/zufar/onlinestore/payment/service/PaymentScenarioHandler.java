package com.zufar.onlinestore.payment.service;

import com.stripe.model.PaymentIntent;

public interface PaymentScenarioHandler {

    void handlePaymentScenario(final PaymentIntent paymentIntent);

}
