package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.qr.dto.EncodeQRCodeRequest
import com.wutsi.platform.qr.dto.EncodeQRCodeResponse
import org.springframework.stereotype.Service
import java.time.Clock

@Service
public class EncodeDelegate(
    private val clock: Clock
) {
    public fun invoke(request: EncodeQRCodeRequest): EncodeQRCodeResponse {
        val expiry = clock.millis() + 1000 * request.timeToLive

        return EncodeQRCodeResponse(
            token = "${request.type}:${request.id}:$expiry".lowercase()
        )
    }
}
