package com.wutsi.platform.qr.endpoint

import com.auth0.jwt.JWT
import com.wutsi.platform.qr.dto.CreateAccountQRCodeResponse
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeRequest
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import java.util.UUID
import kotlin.Int
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/PaymentController.sql"])
public class PaymentControllerTest : AbstractSecuredController(){
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun invoke() {
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
        assertEquals("1", decoded.keyId)
        assertEquals(request.amount, decoded.claims["amount"]?.asDouble())
        assertEquals(request.currency, decoded.claims["currency"]?.asString())
        assertEquals(request.description, decoded.claims["description"]?.asString())
        assertEquals(request.referenceId, decoded.claims["reference_id"]?.asString())
        assertEquals(request.invoiceId, decoded.claims["invoice_id"]?.asString())
        assertNotNull(decoded.issuedAt)
        assertNotNull(decoded.expiresAt)
        assertEquals(request.timeToLive!!.toLong()*1000L, decoded.expiresAt.time - decoded.issuedAt.time)
    }
}
