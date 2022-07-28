package comidev.authemail.components.token;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import comidev.authemail.exceptions.HttpException;
import comidev.authemail.components.notification.EmailSenderService;
import comidev.authemail.components.user.UserEntity;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final EmailSenderService emailSenderService;

    public String save(UserEntity user) {
        String token = UUID.randomUUID().toString();

        confirmationTokenRepo.save(
                new ConfirmationToken(token, 15, user));

        // ? RabbitMQ y Kafka ---------------------------
        emailSenderService.sendConfirmationToken(
                user.getEmail(), user.getName(), token);
        // ? --------------------------------------------
        return token;
    }

    private ConfirmationToken getByToken(String token) {
        return confirmationTokenRepo.findByToken(token)
                .orElseThrow(() -> {
                    String message = "El token es invalido";
                    return new HttpException(HttpStatus.UNAUTHORIZED, message);
                });
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = this.getByToken(token);

        if (confirmationToken.isConfirmed()) {
            String message = "El token ya esta confirmado";
            throw new HttpException(HttpStatus.BAD_REQUEST, message);
        }

        if (confirmationToken.expired()) {
            String message = "Token expirado. Vuelva a iniciar sesión.";
            throw new HttpException(HttpStatus.UNAUTHORIZED, message);
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepo.save(confirmationToken);

        return confirmationToken.getUser().getEmail();
    }

    public void checkAndResendToken(UserEntity user) {
        List<ConfirmationToken> tokens = confirmationTokenRepo.findByUserId(user.getId());

        if (tokens.isEmpty()) {
            String message = "El usuario no tiene tokens";
            throw new HttpException(HttpStatus.UNAUTHORIZED, message);
        }

        ConfirmationToken lastToken = tokens.get(tokens.size() - 1);

        if (lastToken.expired()) {
            this.save(user);
        }
    }
}
