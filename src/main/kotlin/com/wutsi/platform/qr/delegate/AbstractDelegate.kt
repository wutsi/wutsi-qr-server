package com.wutsi.platform.qr.delegate

import com.wutsi.platform.qr.entity.KeyEntity
import org.apache.commons.codec.digest.DigestUtils

abstract class AbstractDelegate {
    protected fun sign(token: String, key: KeyEntity): String =
        DigestUtils.md5Hex(token + "-" + key.privateKey)
}
