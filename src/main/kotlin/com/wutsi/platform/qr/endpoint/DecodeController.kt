package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.qr.`delegate`.DecodeDelegate
import com.wutsi.platform.qr.dto.DecodeQRCodeRequest
import com.wutsi.platform.qr.dto.DecodeQRCodeResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class DecodeController(
    private val `delegate`: DecodeDelegate
) {
    @PostMapping("/v1/decoder")
    @PreAuthorize(value = "hasAuthority('qr-manage')")
    public fun invoke(@Valid @RequestBody request: DecodeQRCodeRequest): DecodeQRCodeResponse =
        delegate.invoke(request)
}
