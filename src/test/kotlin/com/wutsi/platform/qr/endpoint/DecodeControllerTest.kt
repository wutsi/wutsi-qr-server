package com.wutsi.platform.qr.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.qr.dto.DecodeQRCodeRequest
import com.wutsi.platform.qr.dto.DecodeQRCodeResponse
import com.wutsi.platform.qr.error.ErrorURN
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.web.client.HttpClientErrorException
import java.time.Clock
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DecodeControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @MockBean
    private lateinit var clock: Clock

    private lateinit var url: String

    @BeforeEach
    override fun setUp() {
        super.setUp()

        url = "http://localhost:$port/v1/decoder"

        doReturn(1000000L).whenever(clock).millis()
    }

    @Test
    public fun decode() {
        val request = DecodeQRCodeRequest(
            token = "account:1111:2000000"
        )
        val response = rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val entity = response.body!!.entity
        assertEquals("account", entity.type)
        assertEquals("1111", entity.id)
        assertEquals(2000000, entity.expires)
    }

    @Test
    public fun malformed() {
        val request = DecodeQRCodeRequest(
            token = "account:1111:2000000:xxx"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.MALFORMED_TOKEN.urn, response.error.code)
    }

    @Test
    public fun invalidExpiryDate() {
        val request = DecodeQRCodeRequest(
            token = "account:1111:xxx"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.MALFORMED_TOKEN.urn, response.error.code)
    }

    @Test
    public fun expired() {
        val request = DecodeQRCodeRequest(
            token = "account:1111:11"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.EXPIRED.urn, response.error.code)
    }
}