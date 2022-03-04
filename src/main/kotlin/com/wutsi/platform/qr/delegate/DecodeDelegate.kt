package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.qr.dto.DecodeQRCodeRequest
import com.wutsi.platform.qr.dto.DecodeQRCodeResponse
import com.wutsi.platform.qr.dto.Entity
import com.wutsi.platform.qr.error.ErrorURN
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class DecodeDelegate(
    private val clock: Clock,
    private val logger: KVLogger
) {
    fun invoke(request: DecodeQRCodeRequest): DecodeQRCodeResponse {
        logger.add("token", request.token)

        if (request.token.startsWith("http://") || request.token.startsWith("https://"))
            return DecodeQRCodeResponse(
                entity = Entity(
                    type = "url",
                    id = request.token,
                    expires = Long.MAX_VALUE
                )
            )

        val parts = request.token.split('-')
        if (parts.size != 3)
            throw malformedTokenException(request)

        try {
            val expires = parts[2].toLong()
            if (expires < clock.millis() / 1000)
                throw expiredException(request)

            val entity = Entity(
                type = parts[0],
                id = parts[1],
                expires = expires
            )

            logger.add("entity_id", entity.id)
            logger.add("entity_type", entity.type)
            logger.add("entity_expires", entity.expires)
            return DecodeQRCodeResponse(
                entity = entity
            )
        } catch (ex: NumberFormatException) {
            throw malformedTokenException(request)
        }
    }

    private fun malformedTokenException(request: DecodeQRCodeRequest): Exception =
        BadRequestException(
            error = Error(
                code = ErrorURN.MALFORMED_TOKEN.urn,
                parameter = Parameter(
                    name = "token",
                    value = request.token,
                    type = ParameterType.PARAMETER_TYPE_PAYLOAD
                )
            )
        )

    private fun expiredException(request: DecodeQRCodeRequest): Exception =
        ConflictException(
            error = Error(
                code = ErrorURN.EXPIRED.urn,
                parameter = Parameter(
                    name = "token",
                    value = request.token,
                    type = ParameterType.PARAMETER_TYPE_PAYLOAD
                )
            )
        )
}
