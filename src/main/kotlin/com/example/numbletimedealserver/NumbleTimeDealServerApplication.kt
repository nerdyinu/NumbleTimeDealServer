package com.example.numbletimedealserver

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.filter.HiddenHttpMethodFilter

@SpringBootApplication
@EnableJpaAuditing
class NumbleTimeDealServerApplication{
    @PersistenceContext
    lateinit var em:EntityManager
    @Bean
    fun jpaQueryFactory():JPAQueryFactory = JPAQueryFactory(em)

    @Bean
    fun hiddenHttpMethodFilter(): HiddenHttpMethodFilter =HiddenHttpMethodFilter()

}

fun main(args: Array<String>) {
    runApplication<NumbleTimeDealServerApplication>(*args)
}
