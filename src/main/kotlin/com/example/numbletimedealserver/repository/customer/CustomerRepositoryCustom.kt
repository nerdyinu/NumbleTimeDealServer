package com.example.numbletimedealserver.repository.customer

import com.example.numbletimedealserver.domain.Customer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomerRepositoryCustom {
    fun findByUsernameAndPassword(username:String,pw:String):Customer?
    fun pageAll(pageable: Pageable): Page<Customer>
}