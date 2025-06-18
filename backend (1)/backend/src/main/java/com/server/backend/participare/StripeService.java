package com.server.backend.participare;

import com.server.backend.festival.Festival;
import com.server.backend.festival.FestivalRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final ParticipareService participareService;
    private final FestivalRepository festivalRepository;

    public StripeService(ParticipareService participareService, FestivalRepository festivalRepository) {
        this.participareService = participareService;
        this.festivalRepository = festivalRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(Integer festivalId, Integer userId, String successUrl, String cancelUrl) throws StripeException {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new IllegalArgumentException("Festivalul nu a fost găsit."));

        // Transformăm prețul în bani (ex: 59.99 RON -> 5999)
        long pretInBani = Math.round(festival.getPrice() * 100);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("ron")
                                                .setUnitAmount(pretInBani)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Bilet pentru " + festival.getName())
                                                                .setDescription("Acces la festivalul " + festival.getName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("userId", userId.toString())
                .putMetadata("festivalId", festivalId.toString())
                .build();

        return Session.create(params);
    }

    public void finalizeazaPlata(Integer festivalId, Integer userId) {
        participareService.confirmaBiletCumparat(userId, festivalId);
    }
}
