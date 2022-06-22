package com.wutsi.platform.qr.endpoint

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import java.net.HttpURLConnection
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/ImageController.sql"])
internal class ImageControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @Test
    fun fetch() {
        // WHEN
        val ttl = Long.MAX_VALUE
        val cnn =
            URL("http://localhost:$port/image/YWNjb3VudCw3Nzc3LDMwODc5MDEwMDA=.MTAw.YjQ2OWY2YWM0MWNjNzFhNmZmMjlkMjYwZDZiYjMxNDA=.png").openConnection()

        // THEN
        try {
            assertTrue(cnn.getHeaderField("Content-Length").toLong() > 0)
            assertEquals("image/png", cnn.getHeaderField("Content-Type"))
        } finally {
            (cnn as HttpURLConnection).disconnect()
        }
    }

    @Test
    fun fetchWithLogo() {
        // WHEN
        val ttl = Long.MAX_VALUE
        val cnn =
            URL("http://localhost:$port/image/YWNjb3VudCw3Nzc3LDMwODc5MDEwMDA=.MTAw.YjQ2OWY2YWM0MWNjNzFhNmZmMjlkMjYwZDZiYjMxNDA=.png?tenant-id=1").openConnection()

        // THEN
        try {
            assertTrue(cnn.getHeaderField("Content-Length").toLong() > 0)
            assertEquals("image/png", cnn.getHeaderField("Content-Type"))
        } finally {
            (cnn as HttpURLConnection).disconnect()
        }
    }
}
