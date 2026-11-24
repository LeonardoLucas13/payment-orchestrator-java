package io.github.leonardolucas13.payment_orchestrator_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.leonardolucas13.payment_orchestrator_java.model.IdempotencyKey;


public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {
    
}
