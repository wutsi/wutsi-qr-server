package com.wutsi.platform.qr.endpoint

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.net.HttpURLConnection
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ImageControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @Test
    fun fetch() {
        // WHEN
        val ttl = Long.MAX_VALUE
        val cnn = URL("http://localhost:$port/image/account,2309209,$ttl.png").openConnection()

        // THEN
        try {
            assertTrue(cnn.getHeaderField("Content-Length").toLong() > 0)
            assertEquals("image/png", cnn.getHeaderField("Content-Type"))
        } finally {
            (cnn as HttpURLConnection).disconnect()
        }
    }
}
