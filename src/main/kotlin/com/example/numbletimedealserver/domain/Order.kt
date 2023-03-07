package com.example.numbletimedealserver.domain

import jakarta.persistence.*

@Entity
@Table(name = "ORDER_TABLE")
class Order(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    val customer: Customer,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    val product: Product,
    @Column(nullable = false)
    val quantity:Int = 1
) :PrimaryKeyEntity(){

}