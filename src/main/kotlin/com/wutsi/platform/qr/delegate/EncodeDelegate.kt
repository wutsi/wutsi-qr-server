package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.ForbiddenException
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.qr.dao.KeyRepository
import com.wutsi.platform.qr.dto.EncodeQRCodeRequest
import com.wutsi.platform.qr.dto.EncodeQRCodeResponse
import com.wutsi.platform.qr.entity.EntityType
import com.wutsi.platform.qr.entity.KeyEntity
import com.wutsi.platform.qr.error.ErrorURN
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.Base64.getEncoder

@Service
class EncodeDelegate(
    private val clock: Clock,
    private val logger: KVLogger,
    private val dao: KeyRepository,
) : AbstractDelegate() {
    fun invoke(request: EncodeQRCodeRequest): EncodeQRCodeResponse {
        logger.add("entity_id", request.id)
        logger.add("entity_type", request.type)

        val token = generateToken(request)
        val key = findKey()
        val signature = sign(token, key)

        val encoder = getEncoder()
        return EncodeQRCodeResponse(
            token = encoder.encodeToString(token.toByteArray()) + "." +
                encoder.encodeToString(key.id.toString().toByteArray()) + "." +
                encoder.encodeToString(signature.toByteArray())
        )
    }

    private fun generateToken(request: EncodeQRCodeRequest): String {
        val type = EntityType.valueOf(request.type.replace('-', '_').uppercase())
        val expiry = clock.millis() / 1000 + type.timeToLiveSeconds
        return "${type.name},${request.id},$expiry".lowercase()
    }

    private fun findKey(): KeyEntity {
        val keys = dao.findByActive(true)
        if (keys.isEmpty())
            throw ForbiddenException(
                error = Error(
                    code = ErrorURN.SECRET_KEY_NOT_FOUND.urn
                )
            )
        return keys[0]
    }
}
