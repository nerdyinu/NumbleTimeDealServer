package com.example.numbletimedealserver.domain

import jakarta.persistence.*

@Entity
//@Table(indexes = [Index(name = "idx_customer_id", columnList = "customer_id"),])
class Order(
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.ALL])
    @JoinColumn(name = "customer_id")
    val customer: Customer,
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.ALL])
    @JoinColumn(name = "product_id")
    val product: Product,
    @Column(nullable = false)
    val quantity:Int = 1
) :PrimaryKeyEntity(){
}