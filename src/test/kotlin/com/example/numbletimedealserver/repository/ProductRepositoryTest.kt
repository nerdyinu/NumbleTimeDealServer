package com.example.numbletimedealserver.repository

import com.example.numbletimedealserver.JpaTestConfig
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import org.assertj.core.api.Assertions
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
import java.time.LocalTime

@DataJpaTest
@ExtendWith(SpringExtension::class)
@Import(JpaTestConfig::class)
@ActiveProfiles("test")
@Transactional
class ProductRepositoryTest @Autowired constructor(
    private val em: TestEntityManager,
    private val productRepository: ProductRepository
){
    /*
        fun findAllByAppointedTimeBetween(start: LocalTime, end: LocalTime):List<Product>
    fun findAllByAdminId(adminId:UUID):List<Product>
    fun countAllByAdminId(adminId:UUID):JPAQuery<Long>
    fun findByIdAndAdminId(productId:UUID, adminId:UUID):Product?
    fun findByIdLockOption(productId: UUID, isLock:Boolean):Product?

    */

    @Test
    fun `test findAllbyAdminId`(){
        val admin =Customer("inu", "12345", ROLE.ADMIN)
        em.persist(admin)
        val product1 = Product("book1", "empty", LocalTime.now(),100L,admin)
        val product2 = Product("book2", "empty2", LocalTime.now(),100L,admin)
        em.persist(product1)
        em.persist(product2)
        em.flush()

        val p1=em.find(Product::class.java,product1.id)
        println("p1:::${p1.name}")
        val res =productRepository.findAllByAdminId(admin.id)
        println("log::::")
        res.forEach {println(it.name)}
        println("end::::")
//        assertThat(res).extracting("name").contain("book1", "book2")
    }
}