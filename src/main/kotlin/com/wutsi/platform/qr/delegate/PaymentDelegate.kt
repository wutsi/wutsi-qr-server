package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.qr.dto.CreatePaymentQRCodeRequest
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeResponse
import org.springframework.stereotype.Service
import java.time.Clock

@Service
public class PaymentDelegate(
    private val clock: Clock,
) {
    companion object {
        const val TTL = 300 // 300 seconds
    }

    public fun invoke(request: CreatePaymentQRCodeRequest): CreatePaymentQRCodeResponse {
        val now = clock.millis()
        val expiry = now + (request.timeToLive?.let { it } ?: TTL) * 1000
        val amount = (100.0 * request.amount).toLong()
        return CreatePaymentQRCodeResponse(
            token = "payment:${request.merchantId}:${request.referenceId}:$amount:${request.currency}:$expiry"
        )
    }
}
