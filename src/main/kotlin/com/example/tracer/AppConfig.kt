package com.example.tracer

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling


@Configuration
@EnableScheduling
class AppConfig {
    private val logger: Log = LogFactory.getLog(AppConfig::class.java)
}