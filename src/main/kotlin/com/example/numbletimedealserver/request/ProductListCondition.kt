package com.example.numbletimedealserver.request

import java.io.Serializable
import java.time.LocalDate

data class ProductListCondition(val from:LocalDate?, val to:LocalDate= LocalDate.now()): Serializable
