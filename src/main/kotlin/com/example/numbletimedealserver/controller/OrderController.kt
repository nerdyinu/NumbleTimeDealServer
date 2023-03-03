package com.example.numbletimedealserver.controller

import com.example.numbletimedealserver.service.order.OrderService
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(private val orderService: OrderService){
}