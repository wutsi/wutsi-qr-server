package com.wutsi.platform.qr.`delegate`

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeRequest
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeResponse
import com.wutsi.platform.qr.service.RSAKeyProviderImpl
import com.wutsi.platform.qr.service.SecurityManager
import com.wutsi.platform.qr.service.TenantProvider
import org.springframework.stereotype.Service
import java.util.Date

@Service
public class PaymentDelegate(
    private val keyProvider: RSAKeyProviderImpl,
    private val tenantProvider: TenantProvider
) {
    public fun invoke(request: CreatePaymentQRCodeRequest): CreatePaymentQRCodeResponse {
        val now = System.currentTimeMillis()

        val builder = JWT.create()
            .withSubject(request.merchantId.toString())
            .withIssuedAt(Date(now))
            .withJWTId(keyProvider.privateKeyId)
            .withClaim("currency", request.currency)
            .withClaim("amount", request.amount)
            .withClaim("tenant_id", tenantProvider.id())
            .withClaim("entity_type", "PAYMENT")
            .withClaim("reference_id", request.referenceId)

        if (!request.invoiceId.isNullOrBlank())
            builder.withClaim("invoice_id", request.invoiceId)

        if (!request.description.isNullOrBlank())
            builder.withClaim("description", request.description)

        if (request.timeToLive != null)
            builder.withExpiresAt(Date(now + 1000*request.timeToLive))

        return CreatePaymentQRCodeResponse(
            token = builder.sign(Algorithm.RSA256(keyProvider)),
        )
    }
}
