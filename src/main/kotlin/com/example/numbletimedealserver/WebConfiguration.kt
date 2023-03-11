package com.example.numbletimedealserver

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.filter.HiddenHttpMethodFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@Configuration
@EnableJpaAuditing
class WebConfiguration {
    @PersistenceContext
    lateinit var em: EntityManager
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory = JPAQueryFactory(em)

    @Bean
    fun hiddenHttpMethodFilter(): HiddenHttpMethodFilter = HiddenHttpMethodFilter()

}