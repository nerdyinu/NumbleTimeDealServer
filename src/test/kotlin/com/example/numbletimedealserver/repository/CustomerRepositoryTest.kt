package com.example.numbletimedealserver.repository

import com.example.numbletimedealserver.JpaTestConfig
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@ExtendWith(SpringExtension::class)
@Import(JpaTestConfig::class)
@ActiveProfiles("test")
@Transactional

class CustomerRepositoryTest @Autowired constructor(
    private val em: TestEntityManager,
    private val customerRepository: CustomerRepository
) {
    /*
    *     fun findByUsernameAndPassword(username:String,pw:String):Customer?
    fun pageAll(pageable: Pageable): Page<Customer>
    *
    * */
    @Test
    fun `findByUsernameAndPassword`() {
        val customer = Customer("inu", "12345", ROLE.ADMIN)
        em.persist(customer)
        em.flush()
        val res=customerRepository.findByUsernameAndPassword(customer.name,customer.password)
        assertThat(res).isEqualTo(customer)
    }

    @Test
    fun `pageAll`(){
        val customer = Customer("inu", "12345", ROLE.ADMIN)
        val customer2 = Customer("inu2", "12345", ROLE.ADMIN)
        val customer3 = Customer("inu3", "12345", ROLE.ADMIN)
        em.persist(customer)
        em.persist(customer2)
        em.persist(customer3)
        val req=PageRequest.of(0,10)
        val res =customerRepository.pageAll(req)
        assertThat( res.content).containsExactly(customer,customer2,customer3)
        assertThat(res.totalPages).isEqualTo(1)
        assertThat(res.totalElements).isEqualTo(3)
    }
}