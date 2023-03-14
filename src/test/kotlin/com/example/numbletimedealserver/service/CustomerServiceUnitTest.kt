package com.example.numbletimedealserver.service

import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.exception.CustomException
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import com.example.numbletimedealserver.request.LoginRequest
import com.example.numbletimedealserver.request.SignUpRequest
import com.example.numbletimedealserver.service.customer.CustomerService
import com.example.numbletimedealserver.service.customer.CustomerServiceImpl
import com.ninjasquad.springmockk.MockkBean
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageRequest
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.util.*

@ExtendWith(SpringExtension::class, MockKExtension::class)
class CustomerServiceUnitTest (){
    @MockK
     lateinit var customerRepository: CustomerRepository
    @InjectMockKs(overrideValues = true, injectImmutable = true)
    lateinit var customerService :CustomerServiceImpl
    /* fun signup(signupRequest: SignUpRequest): CustomerDto {
        val (name, pw, role) = signupRequest
        return customerRepository.save(Customer(name, pw, role)).let(::CustomerDto)
    }
    */

    @Test
    fun `signup returns CustomerDto`(){
        val signUpRequest =SignUpRequest("inu", "12345", ROLE.ADMIN)


        mockkConstructor(Customer::class)
        val entity = Customer(signUpRequest.name,signUpRequest.pw,signUpRequest.role)
        every { customerRepository.save(any()) } returns entity
        assertThat(customerService.signup(signUpRequest)).isEqualTo(CustomerDto(entity))
        verify(exactly = 1) { customerRepository.save(any()) }
    }

    /*

    @Transactional(readOnly = true)
    override fun login(loginRequest: LoginRequest): CustomerDto =
        customerRepository.findByUsernameAndPassword(loginRequest.username, loginRequest.pw)?.let(com.example.numbletimedealserver.dto::CustomerDto)
            ?: throw CustomException.UserNotFoundException()
    */
    @Test
    fun `login success`(){
        val loginRequest = LoginRequest("inu", "12345")
        val customer = Customer(loginRequest.username,loginRequest.pw,ROLE.ADMIN)
        every { customerRepository.findByUsernameAndPassword(loginRequest.username,loginRequest.pw) } returns customer
        assertThat(customerService.login(loginRequest)).isEqualTo(CustomerDto(customer))
        verify (exactly = 1){ customerRepository.findByUsernameAndPassword(loginRequest.username,loginRequest.pw) }

    }
    @Test
    fun `throw 404 when user not found`(){
        val loginRequest = LoginRequest("inu", "12345")
        val customer = Customer(loginRequest.username,loginRequest.pw,ROLE.ADMIN)
        every { customerRepository.findByUsernameAndPassword(loginRequest.username,loginRequest.pw) } returns null
        assertThrows<CustomException.UserNotFoundException> { customerService.login(loginRequest) }
        verify (exactly = 1){ customerRepository.findByUsernameAndPassword(loginRequest.username,loginRequest.pw) }

    }

    /*

    override fun resign(id: UUID) {
        val customer=customerRepository.findById(id).orElse(null) ?: throw CustomException.UserNotFoundException()
        customerRepository.deleteById(id)
    }
    */
    @Test
    fun `success delete user`(){
        val id = UUID.randomUUID()
        val customer = Customer("inu", "12345", ROLE.ADMIN)
        every { customerRepository.findById(id) } returns Optional.of(customer)
        every { customerRepository.deleteById(id) } returns Unit
        assertDoesNotThrow { customerService.resign(id) }
        verify (exactly = 1){ customerRepository.findById(id) }
        verify(exactly = 1) { customerRepository.deleteById(id) }
    }

    @Test
    fun `throws 404 when id not found`(){
        val id = UUID.randomUUID()
        val customer = Customer("inu", "12345", ROLE.ADMIN)
        every { customerRepository.findById(id) } returns Optional.empty()
        every { customerRepository.deleteById(id) } returns Unit
        assertThrows<CustomException.UserNotFoundException> {  customerService.resign(id)}
        verify (exactly = 1){ customerRepository.findById(id) }
        verify(exactly = 0) { customerRepository.deleteById(id) }
    }
    /*

    override fun getAll(pageable: Pageable): Page<CustomerDto> {
        val content= customerRepository.findAll().map(com.example.numbletimedealserver.dto::CustomerDto)
        return PageableExecutionUtils.getPage(content,pageable){customerRepository.count()}
    }
    */
    @Test
    fun `returns Page CustomerDto success`(){
        val request = PageRequest.of(0,10)
        val list =List(5){ Customer("name$it", "12345", ROLE.ADMIN) }
        every { customerRepository.findAll() } returns list
        every { customerRepository.count() } returns list.size.toLong()
        val pageRes = customerService.getAll(request)
        assertThat(pageRes.totalElements).isEqualTo(list.size.toLong())
        assertThat(pageRes.size).isEqualTo(request.pageSize)
        assertThat(pageRes.content).containsAll(list.map(::CustomerDto))
    }

}