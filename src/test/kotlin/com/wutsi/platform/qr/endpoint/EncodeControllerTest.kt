package com.wutsi.platform.qr.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.qr.dto.EncodeQRCodeRequest
import com.wutsi.platform.qr.dto.EncodeQRCodeResponse
import com.wutsi.platform.qr.entity.EntityType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import java.time.Clock
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EncodeControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @MockBean
    private lateinit var clock: Clock

    private lateinit var url: String

    @BeforeEach
    override fun setUp() {
        super.setUp()

        url = "http://localhost:$port/v1/encoder"

        doReturn(1000000L).whenever(clock).millis()
    }

    @Test
    fun encodeAccount() {
        // WHEN
        val request = createRequest(EntityType.ACCOUNT)
        val response = rest.postForEntity(url, request, EncodeQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals("account:7777:3087901000", response.body?.token)
    }

    @Test
    fun encodeOrder() {
        // WHEN
        val request = createRequest(EntityType.ORDER)
        val response = rest.postForEntity(url, request, EncodeQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals("order:7777:3087901000", response.body?.token)
    }

    @Test
    fun encodeProduct() {
        // WHEN
        val request = createRequest(EntityType.PRODUCT)
        val response = rest.postForEntity(url, request, EncodeQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals("product:7777:3087901000", response.body?.token)
    }

    @Test
    fun encodePaymentRequest() {
        // WHEN
        val request = createRequest(EntityType.PAYMENT_REQUEST)
        val response = rest.postForEntity(url, request, EncodeQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals("payment_request:7777:1300", response.body?.token)
    }

    private fun createRequest(type: EntityType) = EncodeQRCodeRequest(
        type = type.name,
        id = "7777",
    )
}
