package com.example.numbletimedealserver.controller


import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.request.LoginRequest
import com.example.numbletimedealserver.request.SignUpRequest
import com.example.numbletimedealserver.service.customer.CustomerService
import com.example.numbletimedealserver.service.product.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.http.MediaType.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@ExtendWith(SpringExtension::class, MockKExtension::class)
@WebMvcTest(CustomerController::class)
class CustomerControllerUnitTest @Autowired constructor(val mapper:ObjectMapper){
    @Autowired
    lateinit var mockMvc: MockMvc
    @MockkBean
    lateinit var customerService: CustomerService
    @MockkBean
    lateinit var productService: ProductService

    /*
    *     @PostMapping("/signup")
    fun signUp(
        @RequestBody signupRequest: SignUpRequest
    ): ResponseEntity<CustomerDto> = customerService.signup(signupRequest).let { ResponseEntity.ok(it) }
    */
    val signUpRequest = SignUpRequest("inu", "12345", ROLE.ADMIN)

    @Test
    fun `test signup returns customerdto`() {
        val random = UUID.randomUUID()
        every{customerService.signup(signUpRequest)} returns CustomerDto(random,signUpRequest.name,signUpRequest.role)
        mockMvc.post("/signup") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(signUpRequest)
            accept = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(APPLICATION_JSON) }
            jsonPath("$.id") { isString() }
            jsonPath("$.name") { value(signUpRequest.name) }
            jsonPath("$.role") { value(signUpRequest.role.toString()) }
        }
        verify(exactly = 1) { customerService.signup(signUpRequest) }
    }

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
    }*/
    @Test
    fun `login test`(){
        val loginRequest = LoginRequest(signUpRequest.name,signUpRequest.pw)
        val returnDto = CustomerDto(UUID.randomUUID(), signUpRequest.name,signUpRequest.role)
        every { customerService.login(loginRequest) } returns returnDto
        val result =mockMvc.post("/login"){
            contentType= APPLICATION_JSON
            content = mapper.writeValueAsString(loginRequest)
            accept= APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(APPLICATION_JSON) }
            jsonPath("$.id") { isString() }
            jsonPath("$.name") { value(signUpRequest.name) }
            jsonPath("$.role") { value(signUpRequest.role.toString()) }
        }.andReturn()
        verify(exactly = 1) { customerService.login(loginRequest) }
        val session = result.request.session
        assertThat(session?.getAttribute("user")).isNotNull
        assertThat(session?.getAttribute("user")).isEqualTo(returnDto)
    }
    /*

    @DeleteMapping("/user")
    fun resign(@SessionLogin loggedinUser: CustomerDto): ResponseEntity<String> {
        customerService.resign(loggedinUser.id)
        return ResponseEntity.ok().build()
    }
    */
    @Test
    fun `delete test`(){
        val id = UUID.randomUUID()
        every { customerService.resign(id) } returns Unit
        val result =mockMvc.delete("/user"){
            sessionAttrs = mapOf("user" to CustomerDto(id,"inu", ROLE.ADMIN))
        }.andExpect {
            status { isOk() }
        }
        verify(exactly = 1) { customerService.resign(id) }
    }
    /*
    @GetMapping("/users")
    fun listUsers(pageable: Pageable): ResponseEntity<Page<CustomerDto>> =
        customerService.getAll(pageable).let { ResponseEntity.ok(it) }
    *
    * */
    @Test
    fun `user list`(){
        val request = PageRequest.of(0,10)
        val list = List<CustomerDto>(10){ it->CustomerDto(UUID.randomUUID(),"$it name", ROLE.USER) }
        every { customerService.getAll(request) } returns PageableExecutionUtils.getPage(list,request){10L}
        mockMvc.get("/users"){
            param("page", "0")
            param("size", "10")
        }.andExpect {
            status { isOk() }
            content { contentType(APPLICATION_JSON) }
            jsonPath("$.content[0].id"){value(list[0].id.toString())}
            jsonPath("$.content[0].role"){value(list[0].role.toString())}
            jsonPath("$.content[0].name"){value(list[0].name)}
        }
        verify(exactly = 1) { customerService.getAll(request) }
    }
}