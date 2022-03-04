package com.wutsi.platform.qr.entity

enum class EntityType(val timeToLiveSeconds: Long) {
    URL(100L * 365L * 84600L), // Static QR Code - 100 years
    ACCOUNT(100L * 365L * 84600L), // Static QR Code - 100 years
    PRODUCT(100L * 365L * 84600L), // Static QR Code - 100 years
    ORDER(100L * 365L * 84600L), // Static QR Code - 100 years
    PAYMENT_REQUEST(300), // Dynamic QR Code - 5 minutes
    TRANSACTION_APPROVAL(300), // Dynamic QR Code - 5 minutes
}
