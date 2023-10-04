package com.bnk.store;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;

@ComponentScan("com.bnk.store.dao")
@EntityScan("com.bnk.store.entities")
                     //"com.bnk.store.repositories"
@EnableJpaRepositories("com.bnk.store.repositories")

public class EnableSesStore {
}
