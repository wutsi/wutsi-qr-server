package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.qr.dto.EncodeQRCodeRequest
import com.wutsi.platform.qr.dto.EncodeQRCodeResponse
import com.wutsi.platform.qr.entity.EntityType
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class EncodeDelegate(
    private val clock: Clock,
    private val logger: KVLogger
) {
    fun invoke(request: EncodeQRCodeRequest): EncodeQRCodeResponse {
        logger.add("entity_id", request.id)
        logger.add("entity_type", request.type)

        val type = EntityType.valueOf(request.type.replace('-', '_').uppercase())
        val expiry = clock.millis() / 1000 + type.timeToLiveSeconds
        val token = "${type.name}:${request.id}:$expiry".lowercase()

        logger.add("token", token)
        return EncodeQRCodeResponse(
            token = token
        )
    }
}
