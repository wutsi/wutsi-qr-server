package com.wutsi.platform.qr.endpoint

import com.wutsi.platform.qr.`delegate`.GetKeyDelegate
import com.wutsi.platform.qr.dto.GetKeyResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class GetKeyController(
    private val `delegate`: GetKeyDelegate
) {
    @GetMapping("/v1/keys/{id}")
    @PreAuthorize(value = "hasAuthority('qr-read')")
    public fun invoke(@PathVariable(name = "id") id: Long): GetKeyResponse = delegate.invoke(id)
}
