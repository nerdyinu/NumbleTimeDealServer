package com.example.numbletimedealserver.service.product

import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.request.ProductListCondition
import com.example.numbletimedealserver.request.ProductRegisterRequest
import com.example.numbletimedealserver.request.ProductUpdateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalTime
import java.util.*

interface ProductService {
    fun delete(productId: UUID, adminId: UUID)
    fun update(productId: UUID, adminId: UUID,productUpdateRequest: ProductUpdateRequest):ProductDto
    fun register(adminId:UUID,productRegisterRequest: ProductRegisterRequest):ProductDto
    fun dailyUpdate(productId:UUID):ProductDto
    fun findAllByAppointedTime(start:LocalTime, end:LocalTime):List<ProductDto>
    fun getAllBuyers(productId: UUID,pageable: Pageable): Page<CustomerDto>
    fun getAllProductsBought(customerId:UUID, productListCondition: ProductListCondition, pageable: Pageable):Page<ProductDto>
    fun getAllProductsRegistered(adminId: UUID,pageable: Pageable):Page<ProductDto>
}