package io.github.leonardolucas13.payment_orchestrator_java.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import io.github.leonardolucas13.payment_orchestrator_java.model.IdempotencyKey;
import io.github.leonardolucas13.payment_orchestrator_java.repository.IdempotencyKeyRepository;

@Service 
public class IdempotencyService {
    private final IdempotencyKeyRepository repository;

    public IdempotencyService(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna vazio se ESTA requisição ganhou a chave e deve processar.
     * Retorna o registro existente se for uma repetição já concluída.
     * Lança 422 ou 409 nos demais casos.
     */
    @Transactional
    public Optional<IdempotencyKey> reserve(String key, String requestHash) {
        if (repository.tryInsert(key, requestHash) == 1) {
            return Optional.empty();
        }

        IdempotencyKey existing = repository.findById(key).orElseThrow();

        if (!existing.getRequestHash().equals(requestHash)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "Idempotency-Key already used with a different request");
        }
        if ("processing".equals(existing.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Request with this Idempotency-Key is still being processed");
        }
        return Optional.of(existing);
    }

    @Transactional
    public void complete(String key, int responseCode, String responseBody) {
        IdempotencyKey idem = repository.findById(key).orElseThrow();
        idem.setStatus("completed");
        idem.setResponseCode(responseCode);
        idem.setResponseBody(responseBody);
    }

    @Transactional
        public void release(String key) {
        repository.deleteById(key);
    }
}
