package com.wutsi.platform.qr.endpoint

import com.auth0.jwt.JWT
import com.wutsi.platform.qr.delegate.PaymentDelegate
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeRequest
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/PaymentController.sql"])
public class PaymentControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun generateWithExistingKey() {
        val url = "http://localhost:$port/v1/payment"
        val request = CreatePaymentQRCodeRequest(
            invoiceId = "123",
            referenceId = UUID.randomUUID().toString(),
            currency = "XAF",
            amount = 7999.0,
            description = "Sample payment",
            merchantId = 111,
            timeToLive = 300
        )
        val response = rest.postForEntity(url, request, CreatePaymentQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val decoded = JWT.decode(response.body?.token)
        assertEquals(request.merchantId.toString(), decoded.subject)
        assertEquals("100", decoded.keyId)
        assertEquals(request.amount, decoded.claims["amount"]?.asDouble())
        assertEquals(request.currency, decoded.claims["currency"]?.asString())
        assertEquals(request.description, decoded.claims["description"]?.asString())
        assertEquals(request.referenceId, decoded.claims["reference_id"]?.asString())
        assertEquals(request.invoiceId, decoded.claims["invoice_id"]?.asString())
        assertEquals(TENANT_ID, decoded.claims["tenant_id"]?.asLong())
        assertNotNull(decoded.issuedAt)
        assertNotNull(decoded.expiresAt)
        assertEquals(request.timeToLive!!.toLong() * 1000L, decoded.expiresAt.time - decoded.issuedAt.time)
    }

    @Test
    public fun generateWithoutKeystore() {
        val url = "http://localhost:$port/v1/payment"
        val request = CreatePaymentQRCodeRequest(
            invoiceId = "123",
            referenceId = UUID.randomUUID().toString(),
            currency = "XAF",
            amount = 7999.0,
            description = "Sample payment",
            merchantId = 111,
            timeToLive = 300
        )
        val rest = createResTemplate(tenantId = 3333L)
        val response = rest.postForEntity(url, request, CreatePaymentQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val decoded = JWT.decode(response.body?.token)
        assertEquals(request.merchantId.toString(), decoded.subject)
        assertNotNull(decoded.keyId)
        assertEquals(request.amount, decoded.claims["amount"]?.asDouble())
        assertEquals(request.currency, decoded.claims["currency"]?.asString())
        assertEquals(request.description, decoded.claims["description"]?.asString())
        assertEquals(request.referenceId, decoded.claims["reference_id"]?.asString())
        assertEquals(request.invoiceId, decoded.claims["invoice_id"]?.asString())
        assertEquals(3333L, decoded.claims["tenant_id"]?.asLong())
        assertNotNull(decoded.issuedAt)
        assertNotNull(decoded.expiresAt)
        assertEquals(request.timeToLive!!.toLong() * 1000L, decoded.expiresAt.time - decoded.issuedAt.time)
    }

    @Test
    public fun generateWithoutTTL() {
        val url = "http://localhost:$port/v1/payment"
        val request = CreatePaymentQRCodeRequest(
            invoiceId = "123",
            referenceId = UUID.randomUUID().toString(),
            currency = "XAF",
            amount = 7999.0,
            description = "Sample payment",
            merchantId = 111,
            timeToLive = null
        )
        val response = rest.postForEntity(url, request, CreatePaymentQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val decoded = JWT.decode(response.body?.token)
        assertNotNull(decoded.issuedAt)
        assertNotNull(decoded.expiresAt)
        assertEquals(PaymentDelegate.TTL * 1000L, decoded.expiresAt.time - decoded.issuedAt.time)
    }
}
