package com.example.numbletimedealserver.request

import java.io.Serializable

data class LoginRequest(val username:String, val pw:String): Serializable
