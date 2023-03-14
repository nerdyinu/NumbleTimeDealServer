package com.example.numbletimedealserver.repository

import com.example.numbletimedealserver.JpaTestConfig
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.repository.product.ProductRepository
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
        val res =productRepository.findAllByAdminId(admin.id)
        assertThat(res).extracting("name").containsExactly("book1", "book2")
    }
    /* fun countAllByAdminId(adminId:UUID):JPAQuery<Long>*/
    @Test
    fun `test countAllbyAdminId`(){
        val admin =Customer("inu", "12345", ROLE.ADMIN)
        em.persist(admin)
        val product1 = Product("book1", "empty", LocalTime.now(),100L,admin)
        val product2 = Product("book2", "empty2", LocalTime.now(),100L,admin)
        em.persist(product1)
        em.persist(product2)
        em.flush()
        val count = productRepository.countAllByAdminId(admin.id).fetchOne()
        assertThat(count).isNotNull
        assertThat(count).isEqualTo(2L)
    }
    /*fun findByIdAndAdminId(productId:UUID, adminId:UUID):Product?*/
    @Test
    fun `test findByIdandAdminID`(){
        val admin =Customer("inu", "12345", ROLE.ADMIN)
        em.persist(admin)
        val product1 = Product("book1", "empty", LocalTime.now(),100L,admin)
        val product2 = Product("book2", "empty2", LocalTime.now(),100L,admin)
        em.persist(product1)
        em.persist(product2)
        em.flush()
        val res1=productRepository.findByIdAndAdminId(product1.id,admin.id)
        val res2 = productRepository.findByIdAndAdminId(product2.id, admin.id)
        assertThat(res1).isEqualTo(product1)
        assertThat(res2).isEqualTo(product2)
    }
    /*  fun findAllByAppointedTimeBetween(start: LocalTime, end: LocalTime):List<Product>*/
    @Test
    fun `testfindAllByAppointedTimeBetween`(){
        val admin =Customer("inu", "12345", ROLE.ADMIN)
        em.persist(admin)
        val product1 = Product("book1", "empty", LocalTime.now(),100L,admin)
        val product2 = Product("book2", "empty2", LocalTime.now(),100L,admin)
        val product3 = Product("book3", "empty3", LocalTime.now().minusHours(2L),100L,admin)
        em.persist(product1)
        em.persist(product2)
        em.persist(product3)
        em.flush()

        val res=productRepository.findAllByAppointedTimeBetween(LocalTime.now().minusHours(1L), LocalTime.now().plusHours(1L))
        assertThat(res).containsExactly(product1,product2)


        val res2 = productRepository.findAllByAppointedTimeBetween(LocalTime.now().minusHours(3L), LocalTime.now().minusHours(1L))
        assertThat(res2).containsExactly(product3)
    }
}