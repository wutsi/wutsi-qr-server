package com.wutsi.platform.qr.dto

import javax.validation.constraints.NotBlank
import kotlin.String

public data class DecodeQRCodeRequest(
    @get:NotBlank
    public val token: String = ""
)
