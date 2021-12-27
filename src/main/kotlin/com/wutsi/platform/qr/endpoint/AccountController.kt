package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.qr.`delegate`.AccountDelegate
import com.wutsi.platform.qr.dto.CreateAccountQRCodeResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class AccountController(
    private val `delegate`: AccountDelegate
) {
    @PostMapping("/v1/account")
    @PreAuthorize(value = "hasAuthority('qr-manage')")
    public fun invoke(): CreateAccountQRCodeResponse = delegate.invoke()
}
