package com.sequra.sequraassignment.controllers

import com.sequra.sequraassignment.services.DisbursementService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DisbursementController(
    val disbursementService: DisbursementService
) {
    @GetMapping("/api/v0/disbursement/{weekNumber}")
    fun findDisbursementsForMerchantAndWeek(
        @RequestParam merchantId: Int?,
        @PathVariable weekNumber: Int
    ) = disbursementService.findDisbursementsForMerchantAndWeek(merchantId, weekNumber)
}