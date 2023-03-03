package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface ProductRepository: JpaRepository<Product, UUID>, ProductRepositoryCustom
{

}