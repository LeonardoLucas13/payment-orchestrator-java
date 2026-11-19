package io.github.leonardolucas13.payment_orchestrator_java.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
    @Email @NotBlank String customerEmail, 
    @NotNull @Positive Integer amountCents
) {}
