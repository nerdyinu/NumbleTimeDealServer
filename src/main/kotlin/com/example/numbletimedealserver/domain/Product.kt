package com.example.numbletimedealserver.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
class Product(
    @Column(nullable = false)
    val appointedTime: LocalTime,
    @Column(nullable = false)
    var appointedQuantity :Int,
    @Column(nullable = false, name = "stock")
    private var _stockQuantity:Int,
    @Column(nullable = false)
    var name:String,
    @Column
    var description:String
) :PrimaryKeyEntity(){
    val stockQuantity get() = _stockQuantity

    fun dailyUpdate(){this._stockQuantity=appointedQuantity}
}