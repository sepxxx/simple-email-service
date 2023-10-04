package com.bnk.store.dao;

import com.bnk.store.entities.SendEmailTaskEntity;
import com.bnk.store.repositories.SendEmailTaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendEmailTaskDao {
    SendEmailTaskRepository sendEmailTaskRepository;

    @Transactional
    public SendEmailTaskEntity save(SendEmailTaskEntity entity) {
        return sendEmailTaskRepository.save(entity);
    }

    public List<SendEmailTaskEntity> findAllNotProcessed() {
        return sendEmailTaskRepository.findAllNotProcessed();
    }

    @Transactional
    public void markAsProcessed(SendEmailTaskEntity entity) {
        sendEmailTaskRepository.markAsProcessed(entity.getId());
    }

    @Transactional
    public void updateLatestTryAt(SendEmailTaskEntity entity) {
        entity.setLatestTryAt(Instant.now());
    }
}
