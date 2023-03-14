package com.example.numbletimedealserver

import com.example.numbletimedealserver.config.CustomObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@TestConfiguration
class MvcTestConfig :WebMvcConfigurer{

    @Bean
    fun objectMapper(): ObjectMapper = CustomObjectMapper()


}