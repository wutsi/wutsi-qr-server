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

@Service
public class DecodeDelegate() {
    public fun invoke(request: DecodeQRCodeRequest): DecodeQRCodeResponse {
        val parts = request.token.split(':')
        if (parts.size != 3)
            throw malformedException(request)

        try {
            return DecodeQRCodeResponse(
                entity = Entity(
                    type = parts[0],
                    id = parts[1],
                    expires = parts[2].toLong()
                )
            )
        } catch (ex: Exception) {
            throw malformedException(request)
        }
    }

    private fun malformedException(request: DecodeQRCodeRequest): Exception =
        ConflictException(
            error = Error(
                code = ErrorURN.MALFORMED_TOKEN.urn,
                parameter = Parameter(
                    name = "token",
                    value = request.token,
                    type = ParameterType.PARAMETER_TYPE_PAYLOAD
                )
            )
        )
}
