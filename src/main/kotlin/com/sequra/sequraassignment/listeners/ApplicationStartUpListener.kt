package com.sequra.sequraassignment.listeners

import com.sequra.sequraassignment.services.DisbursmentCalculationShedulingService
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class ApplicationStartUpListener(
    val disbursmentCalculationShedulingService: DisbursmentCalculationShedulingService
) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        disbursmentCalculationShedulingService.scheduleCalculation()
    }
}