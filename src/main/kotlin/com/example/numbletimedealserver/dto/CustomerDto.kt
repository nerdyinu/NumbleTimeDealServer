package com.example.numbletimedealserver.dto

import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.ROLE
import java.util.*

data class CustomerDto(val id: UUID,val name:String, val role:ROLE){
    constructor(customer:Customer):this(customer.id,customer.name,customer.role)
}
