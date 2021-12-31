package com.wutsi.platform.qr.error

enum class ErrorURN(val urn: String) {
    MALFORMED_TOKEN("urn:wutsi:error:qr:malformed-token"),
    EXPIRED("urn:wutsi:error:qr:expired"),
}
