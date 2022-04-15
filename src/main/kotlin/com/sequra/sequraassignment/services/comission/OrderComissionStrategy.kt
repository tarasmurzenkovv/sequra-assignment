package com.sequra.sequraassignment.services.comission

import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderComissionPercentageStrategy(
    val comissionPercentages: List<OrderComission>
) {
    fun findPercentage(amount: BigDecimal) = comissionPercentages.find { it.isApplicable(amount) }!!.percentage()
}

interface OrderComission {
    fun isApplicable(amount: BigDecimal): Boolean
    fun percentage(): Double
}

@Service
class LowAmountComission : OrderComission {
    private val AMOUNT = BigDecimal(50)
    private val PERCENTAGE = 0.01
    override fun isApplicable(amount: BigDecimal) = amount < AMOUNT

    override fun percentage() = PERCENTAGE
}

@Service
class MediumAmountComission : OrderComission {
    private val LOWER_AMOUNT = BigDecimal(50)
    private val HIGHER_AMOUNT = BigDecimal(300)
    private val PERCENTAGE = 0.95
    override fun isApplicable(amount: BigDecimal) = (amount >= LOWER_AMOUNT)
            && (amount < HIGHER_AMOUNT)

    override fun percentage() = PERCENTAGE
}

@Service
class HighAmountComission : OrderComission {
    private val AMOUNT = BigDecimal(300)
    private val PERCENTAGE = 0.85
    override fun isApplicable(amount: BigDecimal) = amount > AMOUNT

    override fun percentage() = PERCENTAGE
}