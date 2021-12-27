package com.wutsi.platform.qr.service

import com.wutsi.platform.core.security.WutsiPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityManager {
    fun currentUserId(): Long =
        principal().id.toLong()

    fun principal(): WutsiPrincipal =
        SecurityContextHolder.getContext().authentication.principal as WutsiPrincipal
}
