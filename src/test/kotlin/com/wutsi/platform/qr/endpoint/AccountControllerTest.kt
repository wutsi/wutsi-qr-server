package com.wutsi.platform.qr.endpoint

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.qr.dto.CreateAccountQRCodeResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import java.time.Clock
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @MockBean
    private lateinit var clock: Clock

    private lateinit var url: String

    @BeforeEach
    override fun setUp() {
        super.setUp()

        url = "http://localhost:$port/v1/account"

        doReturn(1000000L).whenever(clock).millis()
    }

    @Test
    public fun generateWithKeystore() {
        // WHEN
        val response = rest.postForEntity(url, null, CreateAccountQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        assertEquals("account:$USER_ID:1300000", response.body?.token)
    }
}
