package com.example.numbletimedealserver.repository

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository

@NoRepositoryBean
interface MyBaseRepository<T,ID>:Repository<T,ID> {
    fun findById(id:ID):T?
    fun <S:T> save(entity:S)
}