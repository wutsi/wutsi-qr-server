package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.qr.dto.CreateAccountQRCodeResponse
import com.wutsi.platform.qr.service.SecurityManager
import org.springframework.stereotype.Service
import java.time.Clock

@Service
public class AccountDelegate(
    private val securityManager: SecurityManager,
    private val clock: Clock
) {
    companion object {
        const val TTL = 300 // 300 seconds
    }

    public fun invoke(): CreateAccountQRCodeResponse {
        val id = securityManager.currentUserId()
        val expiry = clock.millis() + 1000 * TTL

        return CreateAccountQRCodeResponse(
            token = "account:$id:$expiry"
        )
    }
}
