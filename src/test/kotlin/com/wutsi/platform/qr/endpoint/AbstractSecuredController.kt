package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.SubjectType.USER
import com.wutsi.platform.core.security.spring.SpringAuthorizationRequestInterceptor
import com.wutsi.platform.core.security.spring.jwt.JWTBuilder
import com.wutsi.platform.core.test.TestRSAKeyProvider
import com.wutsi.platform.core.test.TestTokenProvider
import com.wutsi.platform.core.test.TestTracingContext
import com.wutsi.platform.core.tracing.spring.SpringTracingRequestInterceptor
import org.junit.jupiter.api.BeforeEach
import org.springframework.web.client.RestTemplate

abstract class AbstractSecuredController {
    companion object {
        const val USER_ID = 1L
        const val TENANT_ID = 777L
    }

    protected lateinit var rest: RestTemplate

    @BeforeEach
    open fun setUp() {
        rest = createResTemplate()
    }

    protected fun createResTemplate(
        scope: List<String> = listOf("qr-read", "qr-manage"),
        subjectId: Long = USER_ID,
        subjectType: SubjectType = USER,
        admin: Boolean = false,
        tenantId: Long = TENANT_ID
    ): RestTemplate {
        val rest = RestTemplate()

        val tokenProvider = TestTokenProvider(
            JWTBuilder(
                subject = subjectId.toString(),
                name = "Ray Sponsible",
                subjectType = subjectType,
                scope = scope,
                keyProvider = TestRSAKeyProvider(),
                admin = admin,
            ).build()
        )

        rest.interceptors.add(SpringTracingRequestInterceptor(TestTracingContext(tenantId = tenantId.toString())))
        rest.interceptors.add(SpringAuthorizationRequestInterceptor(tokenProvider))
        return rest
    }
}
