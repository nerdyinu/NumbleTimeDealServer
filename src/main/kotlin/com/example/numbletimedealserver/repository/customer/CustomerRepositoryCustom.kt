package com.example.numbletimedealserver.repository.customer

import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.dto.CustomerDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomerRepositoryCustom {
    fun findByUsernameAndPassword(username:String,pw:String):Customer?
    fun pageAll(pageable: Pageable): Page<Customer>
}