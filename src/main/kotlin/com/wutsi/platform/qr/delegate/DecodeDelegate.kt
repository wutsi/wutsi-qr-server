package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.qr.dto.DecodeQRCodeRequest
import com.wutsi.platform.qr.dto.DecodeQRCodeResponse
import com.wutsi.platform.qr.dto.Entity
import com.wutsi.platform.qr.error.ErrorURN
import org.springframework.stereotype.Service
import java.time.Clock

@Service
public class DecodeDelegate(private val clock: Clock) {
    public fun invoke(request: DecodeQRCodeRequest): DecodeQRCodeResponse {
        val parts = request.token.split(':')
        if (parts.size != 3)
            throw exception(ErrorURN.MALFORMED_TOKEN, request)

        try {
            val expires = parts[2].toLong()
            if (expires < clock.millis())
                throw exception(ErrorURN.EXPIRED, request)

            return DecodeQRCodeResponse(
                entity = Entity(
                    type = parts[0],
                    id = parts[1],
                    expires = expires
                )
            )
        } catch (ex: NumberFormatException) {
            throw exception(ErrorURN.MALFORMED_TOKEN, request)
        }
    }

    private fun exception(code: ErrorURN, request: DecodeQRCodeRequest): Exception =
        ConflictException(
            error = Error(
                code = code.urn,
                parameter = Parameter(
                    name = "token",
                    value = request.token,
                    type = ParameterType.PARAMETER_TYPE_PAYLOAD
                )
            )
        )
}
