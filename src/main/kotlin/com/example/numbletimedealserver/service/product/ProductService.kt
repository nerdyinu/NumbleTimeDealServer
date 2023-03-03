package com.example.numbletimedealserver.service.product

import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.dto.ProductListCondition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalTime
import java.util.*

interface ProductService {
    fun update(productId:UUID):ProductDto
    fun findAllByAppointedTime(start:LocalTime, end:LocalTime):List<ProductDto>
    fun getAllBuyers(productId: UUID,pageable: Pageable): Page<Customer>
    fun getAllProducts(customerId:UUID,productListCondition: ProductListCondition,pageable: Pageable):Page<Product>
}