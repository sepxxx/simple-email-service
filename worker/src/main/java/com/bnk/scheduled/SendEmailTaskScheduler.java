package com.bnk.scheduled;

import com.bnk.services.EmailClientApi;
import com.bnk.store.dao.SendEmailTaskDao;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@RequiredArgsConstructor
public class SendEmailTaskScheduler {
    SendEmailTaskDao sendEmailTaskDao;
    EmailClientApi emailClientApi;
    @Scheduled(cron = "*/5 * * * * *")
    public void executeSendEmailTasks() {
        log.info("cron working");
        sendEmailTaskDao
                .findAllNotProcessed()
                .forEach(sendEmailTask -> {
                    String destEmail = sendEmailTask.getDestinationEmail();
                    String message = sendEmailTask.getMessage();
                    boolean delivered = emailClientApi.sendEmail(destEmail, message);

                    if(delivered) {
                        log.debug("Task %d already processed.".formatted(sendEmailTask.getId()));
                        sendEmailTaskDao.markAsProcessed(sendEmailTask);
                        return;
                    }
                    log.warn("Task %d returned to process.".formatted(sendEmailTask.getId()));
                    sendEmailTaskDao.updateLatestTryAt(sendEmailTask);

                });
    }
}
