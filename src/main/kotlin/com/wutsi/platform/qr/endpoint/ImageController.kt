package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.qr.delegate.DecodeDelegate
import com.wutsi.platform.qr.dto.DecodeQRCodeRequest
import io.github.g0dkar.qrcode.QRCode
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


@RestController
class ImageController(
    private val decoder: DecodeDelegate,
    private val logger: KVLogger,
    private val tracingContext: TracingContext,
) {
    @GetMapping("/image/{token}.png")
    fun invoke(@PathVariable token: String): ResponseEntity<ByteArray> {
        logger.add("token", token)
        try {
            decoder.invoke(DecodeQRCodeRequest(token)).entity

            // QR code
            val qrcode = ByteArrayOutputStream()
            QRCode(token)
                .render(margin = 30, cellSize = 30)
                .writeImage(qrcode)

            // Logo
            val logo = getLogo()

            // Merge
            val combined = combine(ImageIO.read(ByteArrayInputStream(qrcode.toByteArray())), logo)
            val output = ByteArrayOutputStream()
            ImageIO.write(combined, "png", output)

//            val resource = ByteArrayResource(output.toByteArray(), MediaType.IMAGE_PNG_VALUE)
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(output.toByteArray())
        } catch (ex: ConflictException) {
            logger.setException(ex)
            return ResponseEntity.status(HttpStatus.GONE).build()
        } catch (ex: BadRequestException) {
            logger.setException(ex)
            return ResponseEntity.badRequest().build()
        }
    }

    private fun getLogo(): BufferedImage? {
        val tenantId = tracingContext.tenantId()
            ?: "1" //return null

        val input = ImageController::class.java.getResourceAsStream("/logos/$tenantId.png")
            ?: return null

        return ImageIO.read(input)
    }

    private fun combine(qrcode: BufferedImage, logo: BufferedImage?): BufferedImage {
        logo ?: return qrcode

        val deltaHeight: Int = qrcode.height - logo.height
        val deltaWidth: Int = qrcode.width - logo.width

        val combined = BufferedImage(qrcode.width, qrcode.height, BufferedImage.TYPE_INT_ARGB)
        val g2 = combined.graphics as Graphics2D
        g2.drawImage(qrcode, 0, 0, null)
//        g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f)
        g2.drawImage(
            logo,
            Math.round((deltaWidth / 2).toFloat()),
            Math.round((deltaHeight / 2).toFloat()),
            null
        )
        return combined
    }
}
