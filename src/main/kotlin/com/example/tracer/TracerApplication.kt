package com.example.tracer

import org.apache.commons.logging.LogFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.Scheduled

@SpringBootApplication
class TracerApplication(
//        val tracer: Tracer
) {
    private val logger = LogFactory.getLog(TracerApplication::class.java)

    @Scheduled(fixedDelay = 5000)
    fun test() {
        logger.info("traceId test")
//        try {
        throw RuntimeException("error test")
//        } catch (e: Exception) {
//        logger.error(e)
//        }
    }
}

fun main(args: Array<String>) {
    runApplication<TracerApplication>(*args)
}
