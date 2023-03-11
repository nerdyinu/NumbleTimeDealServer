package com.example.numbletimedealserver.repository

import com.example.numbletimedealserver.JpaTestConfig
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@ExtendWith(SpringExtension::class)
@Import(JpaTestConfig::class)
@ActiveProfiles("test")
@Transactional

class OrderRepositoryTest @Autowired constructor(
    private val em: TestEntityManager,
    private val orderRepository: OrderRepository
) {
}