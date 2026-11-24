package io.github.leonardolucas13.payment_orchestrator_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.leonardolucas13.payment_orchestrator_java.model.IdempotencyKey;


public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> {
    @Modifying
    @Query(value = """
            INSERT INTO idempotency_keys ("key", request_hash, status)
            VALUES (:key, :requestHash, 'processing')
            ON CONFLICT ("key") DO NOTHING
            """, nativeQuery = true)
    int tryInsert(@Param("key") String key, @Param("requestHash") String requestHash);  
}
