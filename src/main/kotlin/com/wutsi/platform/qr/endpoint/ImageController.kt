package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.qr.delegate.DecodeDelegate
import com.wutsi.platform.qr.dto.DecodeQRCodeRequest
import io.github.g0dkar.qrcode.QRCode
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream

@RestController
class ImageController(private val decoder: DecodeDelegate, private val logger: KVLogger) {
    @GetMapping("/image/{token}.png")
    fun invoke(@PathVariable token: String): ResponseEntity<ByteArray> {
        try {
            decoder.invoke(
                DecodeQRCodeRequest(token)
            ).entity

            val image = ByteArrayOutputStream()
            QRCode(token).render(margin = 30, cellSize = 30).writeImage(image)
            val resource = ByteArrayResource(image.toByteArray(), MediaType.IMAGE_PNG_VALUE)

            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource.byteArray)
        } catch (ex: ConflictException) {
            logger.setException(ex)
            return ResponseEntity.status(HttpStatus.GONE).build()
        } catch (ex: BadRequestException) {
            logger.setException(ex)
            return ResponseEntity.badRequest().build()
        }
    }
}
