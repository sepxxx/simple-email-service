package com.bnk.scheduled;

import com.bnk.services.EmailClientApi;
import com.bnk.services.RedisLock;
import com.bnk.services.RedisLockWrapper;
import com.bnk.store.dao.SendEmailTaskDao;
import com.bnk.store.entities.SendEmailTaskEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@RequiredArgsConstructor
public class SendEmailTaskScheduler {
    SendEmailTaskDao sendEmailTaskDao;
    EmailClientApi emailClientApi;
//    RedisLock redisLock;
    private static final String SEND_EMAIL_TASK_KEY_FORMAT = "send:email:task:%s";
    RedisLockWrapper redisLockWrapper; //будет не безопасно если unlock будет к коде executeSendEmailTasks
    //что бы не произошло, блокировку нужно отпустить

    @Scheduled(cron = "*/5 * * * * *")
    public void executeSendEmailTasks() {
//        log.info("cron working");
        sendEmailTaskDao
                .findNotProcessedIds()
                .forEach(sendEmailTaskId -> {
                    String sendEmailTaskKey = getSendEmailTaskKey(sendEmailTaskId);

                    redisLockWrapper.lockAndExecuteTask(
                            sendEmailTaskKey,
                            Duration.ofSeconds(5),
                            ()-> sendEmail(sendEmailTaskId)
                    );

                });
    }

    private static String getSendEmailTaskKey(Long taskId) {
        return SEND_EMAIL_TASK_KEY_FORMAT.formatted(taskId);
    }

    private void sendEmail(Long sendEmailTaskId) {

        Optional<SendEmailTaskEntity> optionalSendEmailTask = sendEmailTaskDao.findNotProcessedById(sendEmailTaskId);
        if(optionalSendEmailTask.isEmpty())
            return;

        SendEmailTaskEntity sendEmailTask = optionalSendEmailTask.get();
        String destEmail = sendEmailTask.getDestinationEmail();
        String message = sendEmailTask.getMessage();
        boolean delivered = emailClientApi.sendEmail(destEmail, message);

        if(delivered) {
            log.info("Task %d has been processed.".formatted(sendEmailTask.getId()));
            sendEmailTaskDao.markAsProcessed(sendEmailTask);
            return;
        }
        log.info("Task %d returned to process.".formatted(sendEmailTask.getId()));
        sendEmailTaskDao.updateLatestTryAt(sendEmailTask);
    }
}
