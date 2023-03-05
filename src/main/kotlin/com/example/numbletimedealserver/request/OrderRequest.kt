package com.example.numbletimedealserver.request

import java.io.Serializable
import java.util.UUID

data class OrderRequest(val customerId:UUID, val productId:UUID): Serializable
