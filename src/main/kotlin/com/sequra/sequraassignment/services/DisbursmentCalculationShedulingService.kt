package com.sequra.sequraassignment.services

import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.PeriodicTrigger
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class DisbursmentCalculationShedulingService(
    val disbursementService: DisbursementService,
    val taskScheduler: TaskScheduler
) {
    fun scheduleCalculation() {
        taskScheduler.schedule({
            disbursementService.calculateAndStoreDisbursmentComission()
        }, PeriodicTrigger(1, TimeUnit.SECONDS))
    }
}