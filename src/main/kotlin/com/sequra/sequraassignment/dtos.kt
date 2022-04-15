package com.sequra.sequraassignment

import java.math.BigDecimal
import java.time.LocalDateTime

class Merchant(
    var id: Int?,
    val name: String,
    val email: String,
    val cif: String
)

class Shopper(
    var id: Int?,
    val name: String,
    val email: String,
    val nif: String
)

class Order(
    var id: Int?,
    val amount: BigDecimal,
    val merchantId: Int,
    val shopperId: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val completedAt: LocalDateTime?,
)

data class Disburse(
    val id: Int?,
    val amount: BigDecimal,
    val merchantId: Int
)

data class DisburseCalculationRequest(
    val merchantId: Int,
    val weekNumber: Int
)

