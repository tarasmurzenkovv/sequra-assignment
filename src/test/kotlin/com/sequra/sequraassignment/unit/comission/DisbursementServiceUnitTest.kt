package com.sequra.sequraassignment.unit.comission

import com.sequra.sequraassignment.Disburse
import com.sequra.sequraassignment.repositories.DisburseProjection
import com.sequra.sequraassignment.repositories.DisburseRepository
import com.sequra.sequraassignment.repositories.OrderRepository
import com.sequra.sequraassignment.services.DisbursementService
import com.sequra.sequraassignment.services.comission.OrderComissionService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal

class DisbursementServiceUnitTest {

    val disburseRepository = mock(DisburseRepository::class.java)
    val orderRepository = mock(OrderRepository::class.java)
    val orderComissionService = mock(OrderComissionService::class.java)
    private val disbursementService = DisbursementService(
        disburseRepository,
        orderRepository,
        orderComissionService,
    )

    @Test
    fun `when merchant id and week number is specifued then they are used to find disburses`() {
        `when`(disburseRepository.find(eq(1), eq(15)))
            .thenReturn(
                listOf(
                    DisburseProjectionTestFixture(id = 1, amount = BigDecimal(1), merchantId = 15),
                    DisburseProjectionTestFixture(id = 1, amount = BigDecimal(2), merchantId = 15),
                )
            )
        val findDisbursementsForMerchantAndWeek = disbursementService.findDisbursementsForMerchantAndWeek(1, 15)
        assertThat(findDisbursementsForMerchantAndWeek).hasSize(2)
        assertThat(findDisbursementsForMerchantAndWeek)
            .containsExactlyInAnyOrder(
                Disburse(id = 1, amount = BigDecimal(1), merchantId = 15),
                Disburse(id = 1, amount = BigDecimal(2), merchantId = 15),
            )
        verify(disburseRepository).find(eq(1), eq(15))
    }

    @Test
    fun `when merchant id and week number is specifued and no disburses are found then empty list is returned`() {
        `when`(disburseRepository.find(eq(1), eq(15))).thenReturn(null)
        val findDisbursementsForMerchantAndWeek = disbursementService.findDisbursementsForMerchantAndWeek(1, 15)
        assertThat(findDisbursementsForMerchantAndWeek).hasSize(0)
        assertThat(findDisbursementsForMerchantAndWeek).isNotNull
        verify(disburseRepository).find(eq(1), eq(15))
    }

    @Test
    fun `when week number is specifued then they are used to find disburses`() {
        `when`(disburseRepository.find(eq(15)))
            .thenReturn(
                listOf(
                    DisburseProjectionTestFixture(id = 1, amount = BigDecimal(1), merchantId = 15),
                    DisburseProjectionTestFixture(id = 1, amount = BigDecimal(2), merchantId = 15),
                )
            )
        val findDisbursementsForMerchantAndWeek = disbursementService.findDisbursementsForMerchantAndWeek(merchantId = null, weekNumber = 15)
        assertThat(findDisbursementsForMerchantAndWeek).hasSize(2)
        assertThat(findDisbursementsForMerchantAndWeek)
            .containsExactlyInAnyOrder(
                Disburse(id = 1, amount = BigDecimal(1), merchantId = 15),
                Disburse(id = 1, amount = BigDecimal(2), merchantId = 15),
            )
        verify(disburseRepository).find(eq(15))
    }

    @Test
    fun `when week number is specifued and no disburses are found then empty list is returned`() {
        `when`(disburseRepository.find(eq(15))).thenReturn(null)
        val findDisbursementsForMerchantAndWeek = disbursementService.findDisbursementsForMerchantAndWeek(merchantId = null, weekNumber = 15)
        assertThat(findDisbursementsForMerchantAndWeek).hasSize(0)
        assertThat(findDisbursementsForMerchantAndWeek).isNotNull
        verify(disburseRepository).find(eq(15))
    }

    @Test
    fun `should lock orders, calculate comission and insert into disbursment proper entries`() {
        `when`(orderRepository.lockUnProcessedOrders()).thenReturn(listOf(Pair(1, BigDecimal(1))))
        `when`(orderComissionService.calculateComission(BigDecimal(1)))
            .thenReturn(BigDecimal(1))
        doNothing().`when`(disburseRepository).saveAll(anyList())
        disbursementService.calculateAndStoreDisbursmentComission()
        verify(orderRepository).lockUnProcessedOrders()
        verify(disburseRepository).saveAll(listOf(Pair(1, BigDecimal(1))))
        verify(orderComissionService).calculateComission(BigDecimal(1))
    }

    private class DisburseProjectionTestFixture(
        override val id: Int,
        override val amount: BigDecimal,
        override val merchantId: Int
    ) : DisburseProjection
}