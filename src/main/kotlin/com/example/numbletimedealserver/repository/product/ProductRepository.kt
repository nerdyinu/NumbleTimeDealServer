package com.example.numbletimedealserver.repository.product

import com.example.numbletimedealserver.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*
import jakarta.persistence.LockModeType


interface ProductRepository: JpaRepository<Product, UUID>, ProductRepositoryCustom
{
//    @Query(value = "select p from Product p where p.id = :productId", nativeQuery = true)
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    fun findByIdLockOption(@Param("productId") productId: UUID): Product?
}