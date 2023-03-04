package com.example.numbletimedealserver.service.order

import com.example.numbletimedealserver.dto.OrderDto
import java.util.UUID

interface OrderService {
    fun createOrder(customerId:UUID,productId:UUID):OrderDto
}