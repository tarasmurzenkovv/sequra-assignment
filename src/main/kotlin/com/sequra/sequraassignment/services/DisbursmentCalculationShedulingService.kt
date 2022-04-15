package com.sequra.sequraassignment.services

import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.PeriodicTrigger
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.SECONDS

@Service
class DisbursmentCalculationShedulingService(
    val disbursementService: DisbursementService,
    val taskScheduler: TaskScheduler
) {
    @EventListener(ContextRefreshedEvent::class)
    fun scheduleCalculation() {
        taskScheduler.schedule({
            disbursementService.calculateAndStoreDisbursmentComission()
        }, PeriodicTrigger(1, SECONDS))
    }
}