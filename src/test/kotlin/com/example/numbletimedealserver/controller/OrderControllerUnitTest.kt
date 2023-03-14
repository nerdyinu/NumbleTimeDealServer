package com.example.numbletimedealserver.controller

import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.OrderDto
import com.example.numbletimedealserver.service.order.OrderService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.*

@ExtendWith(SpringExtension::class, MockKExtension::class)
@WebMvcTest(OrderController::class)
class OrderControllerUnitTest  @Autowired constructor(val mapper: ObjectMapper){
    @Autowired
    lateinit var mockMvc: MockMvc
    @MockkBean
    lateinit var orderService: OrderService
    /*@PostMapping("/order/{productId}")
    fun order(@SessionLogin loginUser: CustomerDto, @PathVariable("productId") productId: UUID): ResponseEntity<OrderDto> =orderService.createOrder(loginUser.id,productId).let { ResponseEntity.ok(it) }
    */
    @Test
    fun `test createOrder`(){
        val customerId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        every { orderService.createOrder(customerId,productId) } returns OrderDto(orderId,customerId, productId)
        mockMvc.post("/order/{productId}", productId){
            accept = MediaType.APPLICATION_JSON
            sessionAttrs = mapOf("user" to CustomerDto(customerId, "inu", ROLE.ADMIN))
        }.andExpect {
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isOk() }
            jsonPath("$.id"){value(orderId.toString())}
            jsonPath("$.customerId"){value(customerId.toString())}
            jsonPath("$.productId"){value(productId.toString())}
        }
        verify(exactly = 1) { orderService.createOrder(customerId, productId) }
    }
}