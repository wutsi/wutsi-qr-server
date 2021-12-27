package com.wutsi.platform.qr.service

import com.auth0.jwt.interfaces.RSAKeyProvider
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Service
class RSAKeyProviderImpl(private val service: KeyService) : RSAKeyProvider {
    override fun getPublicKeyById(keyId: String): RSAPublicKey {
        val key = service.getKey(keyId.toLong())
        val byteKey = Base64.getDecoder().decode(key.publicKey.toByteArray())
        val pk = X509EncodedKeySpec(byteKey)
        val keyFactory = KeyFactory.getInstance("RSA")

        return keyFactory.generatePublic(pk) as RSAPublicKey
    }

    override fun getPrivateKey(): RSAPrivateKey {
        val key = service.getKey()
        val byteKey = Base64.getDecoder().decode(key.privateKey.toByteArray())
        val pk = PKCS8EncodedKeySpec(byteKey)
        val keyFactory = KeyFactory.getInstance("RSA")

        return keyFactory.generatePrivate(pk) as RSAPrivateKey
    }

    override fun getPrivateKeyId(): String {
        return service.getKey().id.toString()
    }
}
