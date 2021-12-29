package com.wutsi.platform.qr.`delegate`

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.qr.dto.CreateAccountQRCodeResponse
import com.wutsi.platform.qr.service.RSAKeyProviderImpl
import com.wutsi.platform.qr.service.SecurityManager
import com.wutsi.platform.qr.service.TenantProvider
import org.springframework.stereotype.Service
import java.util.Date

@Service
public class AccountDelegate(
    private val securityManager: SecurityManager,
    private val keyProvider: RSAKeyProviderImpl,
    private val accountApi: WutsiAccountApi,
    private val tenantProvider: TenantProvider
) {
    companion object {
        const val TTL = 300 // 300 seconds
    }

    public fun invoke(): CreateAccountQRCodeResponse {
        val now = System.currentTimeMillis()
        val id = securityManager.currentUserId()
        val account = accountApi.getAccount(id).account

        val builder = JWT.create()
            .withSubject(id.toString())
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + 1000 * TTL))
            .withJWTId(keyProvider.privateKeyId)
            .withClaim("name", account.displayName)
            .withClaim("tenant_id", tenantProvider.id())
            .withClaim("entity_type", "ACCOUNT")

        return CreateAccountQRCodeResponse(
            token = builder.sign(Algorithm.RSA256(keyProvider)),
        )
    }
}
