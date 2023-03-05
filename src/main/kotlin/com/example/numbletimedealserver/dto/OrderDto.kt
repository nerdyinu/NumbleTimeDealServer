package com.example.numbletimedealserver.dto

import com.example.numbletimedealserver.domain.Order
import java.io.Serializable
import java.util.UUID

data class OrderDto(val id:UUID, val customerId:UUID, val productId:UUID): Serializable {
    constructor(order: Order) :this(order.id,order.customer.id,order.product.id)
}
