package com.bnk.services;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Random;

@Log4j2
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailClientApi {
    Random random = new Random();
    public boolean sendEmail(String destinationEmail, String message) {
        try {
            Thread.sleep(1000L);
            log.info("Email to %s successfully sent.".formatted(destinationEmail));
        } catch (InterruptedException e) {
            return false;
        }

        return random.nextInt(11) <5;
    }
}
