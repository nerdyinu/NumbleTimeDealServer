package com.example.numbletimedealserver.controller

import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.OrderDto
import com.example.numbletimedealserver.service.order.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class OrderController(private val orderService: OrderService){
    @PostMapping("/order/{productId}")
    fun order(@SessionLogin loginUser:CustomerDto, @PathVariable("productId") productId:UUID):ResponseEntity<OrderDto> =orderService.createOrder(loginUser.id,productId).let { ResponseEntity.ok(it) }

}