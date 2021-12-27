package com.wutsi.platform.qr.`delegate`

import com.wutsi.platform.qr.dto.GetKeyResponse
import com.wutsi.platform.qr.dto.Key
import com.wutsi.platform.qr.service.KeyService
import org.springframework.stereotype.Service
import kotlin.Long

@Service
public class GetKeyDelegate(private val service: KeyService) {
    public fun invoke(id: Long): GetKeyResponse {
        val key = service.getKey(id)
        return GetKeyResponse(
            key = Key(
                algorithm = key.algorithm,
                content = key.publicKey
            )
        )
    }
}
