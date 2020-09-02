package com.example.tracer

import brave.Tracer
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.BeanFactory
import org.springframework.cloud.sleuth.instrument.async.LazyTraceThreadPoolTaskExecutor
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.AsyncConfigurerSupport
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.util.ErrorHandler
import java.util.concurrent.Executor
import java.util.concurrent.Executors

//@Configuration
class ThreadConfig(
        val tracer: Tracer,
        val beanFactory: BeanFactory
) : AsyncConfigurerSupport(), SchedulingConfigurer {
    private val logger = LogFactory.getLog(ThreadConfig::class.java)

    override fun configureTasks(scheduledTaskRegistrar: ScheduledTaskRegistrar) {
        val scheduler = ConcurrentTaskScheduler()
        scheduler.setErrorHandler(logErrHandler(tracer))

        scheduledTaskRegistrar.setTaskScheduler(scheduler)
//        scheduledTaskRegistrar.setScheduler(schedulingExecutor())
    }

    @Bean
    fun schedulingExecutor(): Executor {
        val newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        return newSingleThreadScheduledExecutor
    }

    fun logErrHandler(tracer: Tracer): ErrorHandler {
        val e = ErrorHandler {
            val currentSpan = tracer.currentSpan()
            tracer.withSpanInScope(currentSpan ?: tracer.newTrace()).use { scope ->
                logger.error(it, it)
            }
        }
        return e
    }

    @Bean
    fun taskExecutor(beanFactory: BeanFactory): LazyTraceThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()

        return LazyTraceThreadPoolTaskExecutor(beanFactory, executor)
    }
}