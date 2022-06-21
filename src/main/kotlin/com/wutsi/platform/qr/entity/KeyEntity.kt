package com.wutsi.platform.qr.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_KEY")
data class KeyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val algorithm: String = "",
    val privateKey: String = "",
    var active: Boolean = true,
    val created: OffsetDateTime = OffsetDateTime.now(),
    var expired: OffsetDateTime? = null
)
