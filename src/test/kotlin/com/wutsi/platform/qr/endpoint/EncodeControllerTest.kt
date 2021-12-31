package com.wutsi.platform.qr.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.qr.dto.EncodeQRCodeRequest
import com.wutsi.platform.qr.dto.EncodeQRCodeResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import java.time.Clock
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EncodeControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

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
    public fun encode() {
        // WHEN
        val request = EncodeQRCodeRequest(
            type = "ACCOUNT",
            id = "7777",
            timeToLive = 300
        )
        val response = rest.postForEntity(url, request, EncodeQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals("account:7777:1300000", response.body?.token)
    }
}
