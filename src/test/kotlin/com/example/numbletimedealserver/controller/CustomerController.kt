package com.example.numbletimedealserver.controller

import com.example.numbletimedealserver.service.customer.CustomerService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockKExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CustomerController  @Autowired constructor(
    @MockkBean  val customerService: CustomerService
){
    /*
    *     @PostMapping("/signup")
    fun signUp(
        @RequestBody signupRequest: SignUpRequest
    ): ResponseEntity<CustomerDto> = customerService.signup(signupRequest).let { ResponseEntity.ok(it) }
    */
    @Test
    fun `test signup returns customerdto`(){}

    /*
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<CustomerDto> {
        val loginResult = customerService.login(loginRequest)
        val session = httpServletRequest.getSession(true)
        session.setAttribute("user", loginResult)
        return ResponseEntity.ok(loginResult)
    }

    @DeleteMapping("/user")
    fun resign(@SessionLogin loggedinUser: CustomerDto): ResponseEntity<String> {
        customerService.resign(loggedinUser.id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/users")
    fun listUsers(pageable: Pageable): ResponseEntity<Page<CustomerDto>> =
        customerService.getAll(pageable).let { ResponseEntity.ok(it) }
    *
    * */
}