package com.example.numbletimedealserver.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(indexes = [Index(name = "idx_appointed_time", columnList = "appointed_time")] )
class Product(
    @Column(nullable = false, name = "appointed_time")
    val appointedTime: LocalTime,
    @Column(nullable = false, name = "appointed_quantity")
    var appointedQuantity :Int,
    @Column(nullable = false, name = "stock")
    private var _stockQuantity:Int,
    @Column(nullable = false)
    var name:String,
    @Column
    var description:String
) :PrimaryKeyEntity(){
    val stockQuantity get() = _stockQuantity

    fun dailyUpdate(){this._stockQuantity+=appointedQuantity}
}