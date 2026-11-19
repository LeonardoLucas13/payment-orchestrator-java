package io.github.leonardolucas13.payment_orchestrator_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.leonardolucas13.payment_orchestrator_java.model.Order;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    
}
