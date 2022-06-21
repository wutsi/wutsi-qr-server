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
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import java.time.Clock
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/EncodeController.sql"])
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

        assertEquals(
            "YWNjb3VudCw3Nzc3LDMwODc5MDEwMDA=.MTAw.YjQ2OWY2YWM0MWNjNzFhNmZmMjlkMjYwZDZiYjMxNDA=",
            response.body?.token
        )
    }

    @Test
    fun encodeOrder() {
        // WHEN
        val request = createRequest(EntityType.ORDER)
        val response = rest.postForEntity(url, request, EncodeQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals(
            "b3JkZXIsNzc3NywzMDg3OTAxMDAw.MTAw.NDFiMmRmNjAyNTU3Yzc1MzE1NTEzNDNlY2FhMjdiMWM=",
            response.body?.token
        )
    }

    @Test
    fun encodeProduct() {
        // WHEN
        val request = createRequest(EntityType.PRODUCT)
        val response = rest.postForEntity(url, request, EncodeQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals(
            "cHJvZHVjdCw3Nzc3LDMwODc5MDEwMDA=.MTAw.NTFhNDJhNTI3ODhiODllMWI5YTM0MWI1NzY3OTVhNzU=",
            response.body?.token
        )
    }

    private fun createRequest(type: EntityType) = EncodeQRCodeRequest(
        type = type.name,
        id = "7777",
    )
}
