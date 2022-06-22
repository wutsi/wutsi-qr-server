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
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import java.time.Clock
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/DecodeController.sql"])
class DecodeControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

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
    fun decode() {
        // account,7777,3087901000
        val request = DecodeQRCodeRequest(
            token = "YWNjb3VudCw3Nzc3LDMwODc5MDEwMDA=.MTAw.YjQ2OWY2YWM0MWNjNzFhNmZmMjlkMjYwZDZiYjMxNDA="
        )
        val response = rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val entity = response.body!!.entity
        assertEquals("account", entity.type)
        assertEquals("7777", entity.id)
        assertEquals(3087901000, entity.expires)
    }

    @Test
    fun decodeUrl() {
        val request = DecodeQRCodeRequest(
            token = "https://www.google.com"
        )
        val response = rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val entity = response.body!!.entity
        assertEquals("url", entity.type)
        assertEquals(request.token, entity.id)
        assertEquals(Long.MAX_VALUE, entity.expires)
    }

    @Test
    fun malformed() {
        val request = DecodeQRCodeRequest(
            token = "YWNjb3VudCw3Nzc3LDMwODc5MDEwMDA=.MTAw"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.MALFORMED_TOKEN.urn, response.error.code)
    }

    @Test
    fun badSignature() {
        val request = DecodeQRCodeRequest(
            token = "YWNjb3VudCw3Nzc3LDMwODc5MDEwMDA=.MTAw.YjQ2OWY2YWM0MWNjNzFhNmZmMjlkMjYwZDZiYjMxxDA="
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.CORRUPTED_TOKEN.urn, response.error.code)
    }

    @Test
    fun badKey() {
        val request = DecodeQRCodeRequest(
            token = "YWNjb3VudCw3Nzc3LDMwODc5MDEwMDA=.MQ==.YjQ2OWY2YWM0MWNjNzFhNmZmMjlkMjYwZDZiYjMxNDA="
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)
        }

        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.SECRET_KEY_NOT_FOUND.urn, response.error.code)
    }

//    @Test
//    fun expired() {
//        val request = DecodeQRCodeRequest(
//            token = "account,1111,11"
//        )
//        val ex = assertThrows<HttpClientErrorException> {
//            rest.postForEntity(url, request, DecodeQRCodeResponse::class.java)
//        }
//
//        assertEquals(409, ex.rawStatusCode)
//
//        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
//        assertEquals(ErrorURN.EXPIRED.urn, response.error.code)
//    }
}
