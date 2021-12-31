package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.qr.`delegate`.EncodeDelegate
import com.wutsi.platform.qr.dto.EncodeQRCodeRequest
import com.wutsi.platform.qr.dto.EncodeQRCodeResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class EncodeController(
    private val `delegate`: EncodeDelegate
) {
    @PostMapping("/v1/encoder")
    @PreAuthorize(value = "hasAuthority('qr-manage')")
    public fun invoke(@Valid @RequestBody request: EncodeQRCodeRequest): EncodeQRCodeResponse =
        delegate.invoke(request)
}
