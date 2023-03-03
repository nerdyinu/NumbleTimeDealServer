package com.example.numbletimedealserver.service.customer

import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.request.LoginRequest
import com.example.numbletimedealserver.request.SignUpRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CustomerService {
    fun signup(signupRequest: SignUpRequest):CustomerDto
    fun login(loginRequest: LoginRequest):CustomerDto
    fun resign(id:UUID)
    fun getAll(pageable: Pageable): Page<CustomerDto>
}