package com.example.numbletimedealserver.request

import com.example.numbletimedealserver.domain.ROLE

data class SignUpRequest(val name:String, val pw:String, val role:ROLE) {
}