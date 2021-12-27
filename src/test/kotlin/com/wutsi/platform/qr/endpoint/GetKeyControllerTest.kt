package com.wutsi.platform.qr.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.qr.error.ErrorURN
import com.wutsi.platform.security.dto.GetKeyResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetKeyController.sql"])
public class GetKeyControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun getKey() {
        val url = "http://localhost:$port/v1/keys/300"
        val response = rest.getForEntity(url, GetKeyResponse::class.java)
        assertEquals(200, response.statusCodeValue)

        assertEquals("RSA", response.body.key.algorithm)
        assertEquals("public-3", response.body.key.content)
    }

    @Test
    public fun keyNotFound() {
        val url = "http://localhost:$port/v1/keys/9999"
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, ErrorResponse::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.KEY_NOT_FOUND.urn, response.error.code)
    }

    @Test
    public fun invalidTenant() {
        val url = "http://localhost:$port/v1/keys/800"
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, ErrorResponse::class.java)
        }

        assertEquals(403, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.ILLEGAL_TENANT_ACCESS.urn, response.error.code)
    }
}
