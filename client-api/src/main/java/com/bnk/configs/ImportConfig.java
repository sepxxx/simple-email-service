package com.bnk.configs;

import com.bnk.store.EnableSesStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        EnableSesStore.class
})
@Configuration
public class ImportConfig {
}


//SendEmailTaskDao - bean
//класс в другом модуле,для инъекции необходим этот файл