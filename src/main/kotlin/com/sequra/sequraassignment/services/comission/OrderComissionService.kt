package com.sequra.sequraassignment.services.comission

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.MathContext

@Service
class OrderComissionService(
    val orderComissionPercentageStrategy: OrderComissionPercentageStrategy
) {
    fun calculateComission(amount: BigDecimal) =
        orderComissionPercentageStrategy.findPercentage(amount)
            .let { amount.multiply(BigDecimal(it)).round(MathContext(2)) }
}