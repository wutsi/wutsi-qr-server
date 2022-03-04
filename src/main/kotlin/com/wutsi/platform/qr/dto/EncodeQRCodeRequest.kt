package com.wutsi.platform.qr.dto

import javax.validation.constraints.NotBlank
import kotlin.String

public data class EncodeQRCodeRequest(
    @get:NotBlank
    public val type: String = "",
    @get:NotBlank
    public val id: String = ""
)
