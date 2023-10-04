package com.bnk.controllers;

import com.bnk.store.dao.SendEmailTaskDao;
import com.bnk.store.entities.SendEmailTaskEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class EmailController {
    SendEmailTaskDao sendEmailTaskDao;
    @PostMapping("/api/email/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendEmail(@RequestParam("destination_email") String destinationEmail,
                          @RequestParam("message") String message
    ) {
        log.info("sendEmail, destination_email:{}, message:{}",destinationEmail, message);
        sendEmailTaskDao.save(
                SendEmailTaskEntity.builder()
                        .destinationEmail(destinationEmail)
                        .message(message)
                        .build()
        );

    }
}
