package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.qr.`delegate`.PaymentDelegate
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeRequest
import com.wutsi.platform.qr.dto.CreatePaymentQRCodeResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class PaymentController(
    private val `delegate`: PaymentDelegate
) {
    @PostMapping("/v1/payment")
    @PreAuthorize(value = "hasAuthority('qr-manage')")
    public fun invoke(@Valid @RequestBody request: CreatePaymentQRCodeRequest):
        CreatePaymentQRCodeResponse = delegate.invoke(request)
}
