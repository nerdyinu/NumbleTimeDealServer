package com.example.numbletimedealserver.dto

import com.example.numbletimedealserver.domain.Order
import java.util.UUID

data class OrderDto(val id:UUID, val customerId:UUID, val productId:UUID){
    constructor(order: Order) :this(order.id,order.customer.id,order.product.id)
}
