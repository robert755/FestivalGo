package com.server.backend.participare;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout-session")
    public ResponseEntity<Map<String, String>> creeazaCheckoutSession(
            @RequestParam Integer festivalId,
            @RequestParam Integer userId,
            @RequestParam String successUrl
    ) {
        try {
            String cancelUrl = successUrl + "?cancel=true";

            Session session = stripeService.createCheckoutSession(festivalId, userId, successUrl, cancelUrl);

            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());

            return ResponseEntity.ok(response);

        } catch (StripeException | IllegalArgumentException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizeazaPlata(@RequestBody Map<String, Integer> payload) {
        Integer festivalId = payload.get("festivalId");
        Integer userId = payload.get("userId");
        stripeService.finalizeazaPlata(festivalId, userId);
        return ResponseEntity.ok().build();
    }

}
