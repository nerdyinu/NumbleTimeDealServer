package com.example.numbletimedealserver

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.snippet.AbstractDescriptor

infix fun FieldDescriptor.TYPE(customType: DocsFieldType): FieldDescriptor = type(customType.type)
infix fun <T:AbstractDescriptor<T>> AbstractDescriptor<T>.desc(desc:Any):T = description(desc)
@TestConfiguration
class RestDocsConfig {
    @Bean
    fun restDocsMockMvcConfigurationCustomizer(): RestDocsMockMvcConfigurationCustomizer {
        return RestDocsMockMvcConfigurationCustomizer {
            it.operationPreprocessors()
                .withRequestDefaults(Preprocessors.prettyPrint())
                .withResponseDefaults(Preprocessors.prettyPrint())
        }
    }

}