package com.example.numbletimedealserver

import com.fasterxml.jackson.databind.ObjectMapper
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.filter.HiddenHttpMethodFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
@EnableJpaAuditing
class WebConfiguration {
    @PersistenceContext
    lateinit var em: EntityManager
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory = JPAQueryFactory(em)

    @Bean
    fun hiddenHttpMethodFilter(): HiddenHttpMethodFilter = HiddenHttpMethodFilter()
    @Bean
    fun jackson2ObjectMapperBuilder(objectMapper: ObjectMapper): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder.json()
        builder.configure(objectMapper)
        return builder
    }

}