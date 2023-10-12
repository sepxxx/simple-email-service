package com.bnk.store.repositories;

import com.bnk.store.entities.SendEmailTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SendEmailTaskRepository extends JpaRepository<SendEmailTaskEntity, Long> {
    @Query("""
        select task.id
        from SendEmailTaskEntity task
        where task.processedAt is NULL
        order by task.createdAt
    """)
    List<Long> findNotProcessedIds();

    @Modifying
    @Query("""
        update SendEmailTaskEntity task
        set task.processedAt = NOW()
        where task.id =:id
    """)
    void markAsProcessed(Long id);

    @Modifying
    @Query("""
        update SendEmailTaskEntity task
        set task.latestTryAt = NOW()
        where task.id =:id
    """)
    void updateLatestTryAt(Long id);


    @Query("""
        select task
        from SendEmailTaskEntity task
        where task.processedAt is NULL
        and task.id =:id
    """)
    Optional<SendEmailTaskEntity> findNotProcessedById(Long id);
}
