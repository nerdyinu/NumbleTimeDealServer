package com.example.numbletimedealserver.repository.order

import com.example.numbletimedealserver.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderRepository:JpaRepository<Order,UUID> , OrderRepositoryCustom{
}