package com.sequra.sequraassignment.services

import com.sequra.sequraassignment.*
import com.sequra.sequraassignment.repositories.DisburseRepository
import com.sequra.sequraassignment.repositories.OrderRepository
import com.sequra.sequraassignment.services.comission.OrderComissionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime.now

@Service
class DisbursementService(
    val disburseRepository: DisburseRepository,
    val orderRepository: OrderRepository,
    val orderComissionService: OrderComissionService
) {
    fun findDisbursementsForMerchantAndWeek(merchantId: Int?, weekNumber: Int): List<Disburse>? {
        return if (merchantId != null) {
            disburseRepository.find(merchantId, weekNumber)
                ?.map { Disburse(it.id, it.amount, it.merchantId) }
                ?: emptyList()
        } else {
            disburseRepository.find(weekNumber)
                ?.map { Disburse(it.id, it.amount, it.merchantId) }
                ?: emptyList()
        }
    }

    @Transactional
    fun calculateAndStoreDisbursmentComission() {
        orderRepository.lockUnProcessedOrders()
            .map { Pair(it.first, orderComissionService.calculateComission(it.second)) }
            .let { disburseRepository.saveAll(it) }
        orderRepository.markOrdersAsProcessed(now())
    }
}