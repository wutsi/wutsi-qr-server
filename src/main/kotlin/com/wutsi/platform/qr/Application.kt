package com.wutsi.platform.qr

import com.wutsi.platform.core.WutsiApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@WutsiApplication
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
class Application

fun main(vararg args: String) {
    org.springframework.boot.runApplication<Application>(*args)
}
