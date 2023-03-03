package com.example.numbletimedealserver.domain

import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.request.ProductRegisterRequest
import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(indexes = [
        Index(name = "idx_appointed_time", columnList = "appointed_time"),
        Index(name = "idx_admin_id", columnList = "admin_id")])
class Product(

    @Column(nullable = false)
    private var _name: String,
    @Column
    private var _description: String,
    @Column(nullable = false, name = "appointed_time")
    private var _appointedTime: LocalTime,
    @Column(nullable = false, name = "appointed_quantity")
    private var _appointedQuantity: Long,
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.ALL])
    @JoinColumn(name = "admin_id")
    val admin: Customer,
    @Column(nullable = false, name = "stock")
    private var _stockQuantity: Long = 0L,
) : PrimaryKeyEntity() {
    val name get() = _name
    val description get() = _description
    val stockQuantity get() = _stockQuantity
    val appointedTime get() = _appointedTime
    val appointedQuantity get() = _appointedQuantity
    fun dailyUpdate() {
        this._stockQuantity = appointedQuantity
    }

    fun updateTime(time: LocalTime) {
        _appointedTime = time
    }

    fun updateQuantity(quantity: Long) {
        _appointedQuantity = quantity
    }

    fun updateName(newName: String) {
        _name = newName
    }

    fun updateDesc(desc: String) {
        _description = desc
    }
}