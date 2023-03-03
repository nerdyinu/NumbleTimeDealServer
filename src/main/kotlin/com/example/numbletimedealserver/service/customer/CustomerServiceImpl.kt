package com.example.numbletimedealserver.service.customer

import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.request.LoginRequest
import com.example.numbletimedealserver.request.SignUpRequest
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.util.*

class CustomerServiceImpl(private val customerRepository: CustomerRepository) : CustomerService {
    override fun signup(signupRequest: SignUpRequest): CustomerDto {
        val (name, pw, role) = signupRequest
        return customerRepository.save(Customer(name, pw, role)).let(::CustomerDto)
    }

    override fun login(loginRequest: LoginRequest): CustomerDto =
        customerRepository.findByUsernameAndPassword(loginRequest.username, loginRequest.pw)?.let(::CustomerDto)
            ?: throw CustomException.UserNotFoundException()


    override fun resign(id: UUID) {
        val customer=customerRepository.findById(id).orElse(null) ?: throw CustomException.UserNotFoundException()
        customerRepository.deleteById(id)
    }

    override fun getAll(pageable: Pageable): Page<CustomerDto> {
        val content= customerRepository.findAll().map(::CustomerDto)
        return PageableExecutionUtils.getPage(content,pageable){customerRepository.count()}
    }
}