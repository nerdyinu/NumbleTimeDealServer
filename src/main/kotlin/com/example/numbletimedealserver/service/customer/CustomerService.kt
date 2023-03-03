package com.example.numbletimedealserver.service.customer

import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.SignUpRequest

interface CustomerService {
    fun signup(signupRequest:SignUpRequest)
    fun getAll():List<CustomerDto>
}