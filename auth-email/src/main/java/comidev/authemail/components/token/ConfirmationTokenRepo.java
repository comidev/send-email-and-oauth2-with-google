package comidev.authemail.components.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    List<ConfirmationToken> findByUserId(Long userId);
}
