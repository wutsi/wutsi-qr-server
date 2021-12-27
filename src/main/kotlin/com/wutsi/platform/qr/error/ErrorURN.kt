package com.wutsi.platform.qr.error

enum class ErrorURN(val urn: String) {
    KEY_NOT_FOUND("urn:wutsi:error:qr:key-not-found"),
    ILLEGAL_TENANT_ACCESS("urn:wutsi:error:qr:illegal-tenant-access"),
}
