package com.wutsi.platform.qr.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String

public data class CreatePaymentQRCodeRequest(
    @get:NotBlank
    public val referenceId: String = "",
    public val merchantId: Long = 0,
    @get:Min(0)
    public val amount: Double = 0.0,
    @get:NotBlank
    @get:Size(max = 3)
    public val currency: String = "",
    public val timeToLive: Int? = null
)
