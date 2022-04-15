package com.sequra.sequraassignment

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name ="merchant")
class MerchantEntity(
    @Id
    @GeneratedValue
    val id: Int?,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val cif: String,

    @OneToMany(mappedBy = "merchant")
    val orders: List<OrderEntity>?
)

@Entity
@Table(name ="shopper")
class ShopperEntity(
    @Id
    @GeneratedValue
    val id: Int?,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val nif: String,

    @OneToMany(mappedBy = "shopper")
    val orders: List<OrderEntity>?
)

@Entity
@Table(name ="order")
class OrderEntity(
    @Id
    @GeneratedValue
    val id: Int?,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "completed_at")
    val completedAt: LocalDateTime?,

    @OneToOne(mappedBy = "order")
    val disburse: DisburseEntity?,

    @ManyToOne
    @JoinColumn(name = "shopper_id", nullable = false)
    val shopper: ShopperEntity,

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    val merchant: MerchantEntity
)

@Entity
@Table(name ="disburse")
class DisburseEntity(
    @Id
    @GeneratedValue
    val id: Int?,

    @Column(nullable = false)
    val amount: BigDecimal,

    @OneToOne
    @JoinColumn(name = "order_id")
    val order: OrderEntity
)