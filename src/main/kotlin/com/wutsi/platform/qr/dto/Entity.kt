package com.wutsi.platform.qr.dto

import kotlin.Long
import kotlin.String

public data class Entity(
    public val type: String = "",
    public val id: String = "",
    public val expires: Long = 0
)
