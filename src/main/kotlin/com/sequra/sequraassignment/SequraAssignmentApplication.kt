package com.sequra.sequraassignment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SequraAssignmentApplication

fun main(args: Array<String>) {
	runApplication<SequraAssignmentApplication>(*args)
}
