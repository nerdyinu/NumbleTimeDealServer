package com.example.numbletimedealserver.repository.customer

import com.example.numbletimedealserver.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository:JpaRepository<Customer,UUID>, CustomerRepositoryCustom {
}