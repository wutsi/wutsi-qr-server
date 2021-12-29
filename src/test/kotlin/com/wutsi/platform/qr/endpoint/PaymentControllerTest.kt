package com.wutsi.platform.qr.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeRequest
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import java.time.Clock
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @MockBean
    private lateinit var clock: Clock

    private lateinit var url: String

    @BeforeEach
    override fun setUp() {
        super.setUp()

        url = "http://localhost:$port/v1/payment"

        doReturn(1000000L).whenever(clock).millis()
    }

    @Test
    public fun generate() {
        val request = CreatePaymentQRCodeRequest(
            referenceId = "aaaaa",
            currency = "XAF",
            amount = 7999.0,
            merchantId = 111,
            timeToLive = 100
        )
        val response = rest.postForEntity(url, request, CreatePaymentQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)
        assertEquals("payment:111:aaaaa:799900:XAF:1100000", response.body.token)
    }

    @Test
    public fun generateNoTTL() {
        val request = CreatePaymentQRCodeRequest(
            referenceId = "aaaaa",
            currency = "XAF",
            amount = 7999.0,
            merchantId = 111,
            timeToLive = null
        )
        val response = rest.postForEntity(url, request, CreatePaymentQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)
        assertEquals("payment:111:aaaaa:799900:XAF:1300000", response.body.token)
    }
}
