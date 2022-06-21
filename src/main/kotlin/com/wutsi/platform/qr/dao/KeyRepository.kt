package com.wutsi.platform.qr.dao

import com.wutsi.platform.qr.entity.KeyEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KeyRepository : CrudRepository<KeyEntity, Long> {
    fun findByActive(active: Boolean): List<KeyEntity>
}
