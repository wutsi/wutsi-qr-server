package com.wutsi.platform.qr.error

enum class ErrorURN(val urn: String) {
    MALFORMED_TOKEN("urn:wutsi:error:qr:malformed-token"),
    EXPIRED("urn:wutsi:error:qr:expired"),
    SECRET_KEY_NOT_FOUND("urn:wutsi:error:qr:secret-key-not-found"),
    CORRUPTED_TOKEN("urn:wutsi:error:qr:corrupted-token"),
}
