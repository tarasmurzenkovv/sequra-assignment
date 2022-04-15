package com.sequra.sequraassignment.unit.comission

import com.sequra.sequraassignment.services.comission.HighAmountComission
import com.sequra.sequraassignment.services.comission.LowAmountComission
import com.sequra.sequraassignment.services.comission.MediumAmountComission
import com.sequra.sequraassignment.services.comission.OrderComissionPercentageStrategy
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ComissionUnitTest {
    private val commistionStrategy = OrderComissionPercentageStrategy(
        listOf(
            LowAmountComission(),
            MediumAmountComission(),
            HighAmountComission(),
        )
    )

    @Test
    fun `should properly calculate comission for low amount order`() {
        Assertions.assertThat(commistionStrategy.findPercentage(BigDecimal(49))).isEqualTo(0.01)
    }

    @Test
    fun `should properly calculate comission for medium amount order`() {
        Assertions.assertThat(commistionStrategy.findPercentage(BigDecimal(50))).isEqualTo(0.95)
    }

    @Test
    fun `should properly calculate comission for higher amount order`() {
        Assertions.assertThat(commistionStrategy.findPercentage(BigDecimal(301))).isEqualTo(0.85)
    }
}