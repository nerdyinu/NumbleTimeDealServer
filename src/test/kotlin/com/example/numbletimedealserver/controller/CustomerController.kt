package com.example.numbletimedealserver.controller

import org.springframework.beans.factory.annotation.Autowired

//@ExtendWith(SpringExtension::class, MockKExtension::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
class CustomerController  @Autowired constructor(
//    @MockkBean private val customerService: CustomerService
){
    /*
    *     @PostMapping("/signup")
    fun signUp(
        @RequestBody signupRequest: SignUpRequest
    ): ResponseEntity<CustomerDto> = customerService.signup(signupRequest).let { ResponseEntity.ok(it) }
    */
//    @Test
//    fun `test signup returns customerdto`(){}
//
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