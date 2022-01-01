package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.qr.dto.EncodeQRCodeRequest
import com.wutsi.platform.qr.dto.EncodeQRCodeResponse
import org.springframework.stereotype.Service
import java.time.Clock

@Service
public class EncodeDelegate(
    private val clock: Clock,
    private val logger: KVLogger
) {
    public fun invoke(request: EncodeQRCodeRequest): EncodeQRCodeResponse {
        logger.add("entity_id", request.id)
        logger.add("entity_type", request.type)
        logger.add("time_to_live", request.timeToLive)

        val expiry = clock.millis() + 1000 * request.timeToLive
        val token = "${request.type}:${request.id}:$expiry".lowercase()

        logger.add("token", token)
        return EncodeQRCodeResponse(
            token = token
        )
    }
}
