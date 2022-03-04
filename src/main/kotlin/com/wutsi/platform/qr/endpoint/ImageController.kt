package com.wutsi.platform.qr.endpoint

import io.github.g0dkar.qrcode.QRCode
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream

@RestController
class ImageController {
    @GetMapping("/image/{token}.png")
    fun invoke(@PathVariable token: String): ResponseEntity<ByteArray> {
        val image = ByteArrayOutputStream()
        QRCode(token).render(margin = 30, cellSize = 30).writeImage(image)
        val resource = ByteArrayResource(image.toByteArray(), MediaType.IMAGE_PNG_VALUE)

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(resource.byteArray)
    }
}
