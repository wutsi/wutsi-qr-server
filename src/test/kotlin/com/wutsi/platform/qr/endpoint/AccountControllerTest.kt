package com.wutsi.platform.qr.endpoint

import com.auth0.jwt.JWT
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.GetAccountResponse
import com.wutsi.platform.account.dto.Phone
import com.wutsi.platform.qr.dto.CreateAccountQRCodeResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.Int
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/AccountController.sql"])
public class AccountControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @MockBean
    protected lateinit var accountApi: WutsiAccountApi

    @Test
    public fun invoke() {
        // GIVEN
        val account = Account(
            id = USER_ID,
            displayName = "Ray Sponsible",
            country = "CM",
            language = "en",
            status = "ACTIVE",
            phone = Phone(
                id = 7777,
                number = "+237699999999"
            ),
            pictureUrl = "https://www.picture.com/me/1234"
        )
        doReturn(GetAccountResponse(account)).whenever(accountApi).getAccount(any())

        // WHEN
        val url = "http://localhost:$port/v1/account"
        val response = rest.postForEntity(url, null, CreateAccountQRCodeResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val decoded = JWT.decode(response.body?.token)
        assertEquals(USER_ID.toString(), decoded.subject)
        assertEquals("1", decoded.keyId)
        assertEquals(account.displayName, decoded.claims["name"]?.asString())
        assertEquals(account.pictureUrl, decoded.claims["picture"]?.asString())
        assertEquals(TENANT_ID, decoded.claims["tenant_id"]?.asLong())
        assertEquals("ACCOUNT", decoded.claims["entity_type"]?.asString())
        assertNotNull(decoded.issuedAt)
        assertNull(decoded.expiresAt)
    }
}
