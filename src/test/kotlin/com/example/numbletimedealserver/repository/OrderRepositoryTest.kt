package com.example.numbletimedealserver.repository

import com.example.numbletimedealserver.JpaTestConfig
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Order
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.request.ProductListCondition
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@DataJpaTest
@ExtendWith(SpringExtension::class)
@Import(JpaTestConfig::class)
@ActiveProfiles("test")
@Transactional

class OrderRepositoryTest @Autowired constructor(
    private val em: TestEntityManager,
    private val orderRepository: OrderRepository
) {


    /* fun findAllByCustomerId(customerId:UUID,productListCondition: ProductListCondition): List<Order>*/
    @Test
    fun `findAllByCustomerId`(){
        val customer = Customer("inu", "12345", ROLE.USER)
        em.persist(customer)
        val product = Product("book1", "empty", LocalTime.now(), 100L, customer)
        em.persist(product)
        val order = Order(customer,product)
        val order2 = Order(customer,product)
        em.persist(order)
        em.persist(order2)
        em.flush()

        val res=orderRepository.findAllByCustomerId(customer.id, ProductListCondition(LocalDate.now().minusDays(1L)))
        assertThat(res).containsExactly(order,order2)
    }
    /*fun countByCustomerId(customerId: UUID,productListCondition: ProductListCondition): JPAQuery<Long>*/
    @Test
    fun `testCountByCustomerId`(){
        val customer = Customer("inu", "12345", ROLE.USER)
        em.persist(customer)
        val product = Product("book1", "empty", LocalTime.now(), 100L, customer)
        em.persist(product)
        val order = Order(customer,product)
        val order2 = Order(customer,product)
        em.persist(order)
        em.persist(order2)
        em.flush()
        val res=orderRepository.countByCustomerId(customer.id, ProductListCondition(LocalDate.now().minusDays(1L))).fetchOne()
        assertThat(res).isNotNull
        assertThat(res).isEqualTo(2)
    }
    /* fun findAllByProductIdAndDate(productId:UUID, orderDate:LocalDate):List<Order>
    * */
    @Test
    fun `findAllByProductIdAndDate`(){
        val customer = Customer("inu", "12345", ROLE.USER)
        em.persist(customer)
        val product = Product("book1", "empty", LocalTime.now(), 100L, customer)
        em.persist(product)
        val order = Order(customer,product)
        val order2 = Order(customer,product)
        em.persist(order)
        em.persist(order2)
        em.flush()
        val res = orderRepository.findAllByProductIdAndDate(productId = product.id, orderDate = LocalDate.now())
        assertThat(res).containsExactly(order,order2)
    }
    /*  fun countAllByProductIdAndDate(productId:UUID, orderDate:LocalDate):JPAQuery<Long>*/
    @Test
    fun `testcountAllByProductIdAndDate`(){
        val customer = Customer("inu", "12345", ROLE.USER)
        em.persist(customer)
        val product = Product("book1", "empty", LocalTime.now(), 100L, customer)
        em.persist(product)
        val order = Order(customer,product)
        val order2 = Order(customer,product)
        em.persist(order)
        em.persist(order2)
        em.flush()
        val res=orderRepository.countByCustomerId(customer.id, ProductListCondition(LocalDate.now().minusDays(1L))).fetchOne()
        assertThat(res).isNotNull
        assertThat(res).isEqualTo(2)
    }
}