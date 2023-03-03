package com.example.numbletimedealserver.dto

import com.example.numbletimedealserver.domain.ROLE

data class SignUpRequest(val name:String, val pw:String, val role:ROLE) {
}