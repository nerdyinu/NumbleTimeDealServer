package com.example.numbletimedealserver.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.hibernate.annotations.BatchSize

@Entity
class Customer (
    name:String,
    var password:String,
    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL])
    @BatchSize(size = 100)
    private val _orders:MutableList<Order> = mutableListOf(),
    @Column(nullable = false)
    val role:ROLE
):PrimaryKeyEntity(){
    @Column(nullable = false)
    var name:String = name
        protected set
    val orders:List<Order> get() = _orders
}