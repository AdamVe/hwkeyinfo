package com.adamve.hwkeyinfo.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "service")
data class Service(
    @PrimaryKey(autoGenerate = true)
    var serviceId: Long = 0L,

    var serviceName: String,

    var serviceUser: String,
)


data class ServiceWithSecurityKeys(
    @Embedded val service: Service,
    @Relation(
        parentColumn = "serviceId",
        entityColumn = "securityKeyId",
        associateBy = Junction(SecurityKeyServiceCrossRef::class)
    )
    val securityKeys: List<SecurityKey>
)
