package com.wutsi.platform.qr.service

import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.ForbiddenException
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.qr.dao.KeyRepository
import com.wutsi.platform.qr.entity.KeyEntity
import com.wutsi.platform.qr.error.ErrorURN
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.time.OffsetDateTime
import java.util.Base64

@Service
class KeyService(
    private val dao: KeyRepository,
    private val tenantProvider: TenantProvider
) {
    companion object {
        const val ALGO = "RSA"
        const val KEY_SIZE = 2048
    }

    fun getKey(): KeyEntity {
        val tenantId = tenantProvider.id()
        val keys = dao.findByTenantIdAndActive(tenantId, true)
            .sortedByDescending { it.id }
        return if (keys.isEmpty())
            initializeStore(tenantId)
        else
            keys[0]
    }

    fun getKey(id: Long): KeyEntity {
        val key = dao.findById(id)
            .orElseThrow {
                throw NotFoundException(
                    error = Error(code = ErrorURN.KEY_NOT_FOUND.urn)
                )
            }

        if (key.tenantId != tenantProvider.id())
            throw ForbiddenException(
                error = Error(code = ErrorURN.ILLEGAL_TENANT_ACCESS.urn)
            )

        return key
    }

    private fun initializeStore(tenantId: Long): KeyEntity {
        val key = createKey(tenantId)
        deactivatePreviousKey(key)
        return key
    }

    private fun createKey(tenantId: Long): KeyEntity {
        val keyPair = createKeyPair()
        val encoder = Base64.getEncoder()
        return dao.save(
            KeyEntity(
                algorithm = ALGO,
                tenantId = tenantId,
                active = true,
                created = OffsetDateTime.now(),
                expired = null,
                privateKey = encoder.encodeToString(keyPair.private.encoded),
                publicKey = encoder.encodeToString(keyPair.public.encoded)
            )
        )
    }

    private fun createKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance(ALGO)
        generator.initialize(KEY_SIZE)
        return generator.generateKeyPair()
    }

    private fun deactivatePreviousKey(current: KeyEntity) {
        val now = OffsetDateTime.now()
        val keys = dao.findByTenantIdAndActive(current.tenantId, true)
            .filter { it.id != current.id }
        if (keys.isEmpty())
            return

        keys.forEach {
            it.active = false
            it.expired = now
        }
        dao.saveAll(keys)
    }
}
