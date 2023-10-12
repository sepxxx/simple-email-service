package com.bnk.configs;

import com.bnk.store.EnableSesStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({
        EnableSesStore.class
})
@Configuration
@EnableScheduling
public class ImportConfig {
}


//SendEmailTaskDao - bean
//класс в другом модуле,для инъекции необходим этот файл