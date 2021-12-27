package com.wutsi.platform.qr.service

import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.stereotype.Service

@Service
class TenantProvider(
    private val tracingContext: TracingContext,
) {
    fun id(): Long =
        tracingContext.tenantId()!!.toLong()
}
