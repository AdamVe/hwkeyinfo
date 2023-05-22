package com.adamve.hwkeyinfo.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "security_keys")
data class SecurityKey(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "securityKeyId")
    var id: Long = 0,

    @ColumnInfo(name = "securityKeyName")
    var name: String = "",

    @ColumnInfo(name = "securityKeyType")
    var type: String = "",

    @ColumnInfo(name = "securityKeyDescription")
    var description: String = "",
)

@Entity(primaryKeys = ["securityKeyId", "serviceId"])
data class SecurityKeyServiceCrossRef(
    @ColumnInfo(index = true)
    val securityKeyId: Long,
    @ColumnInfo(index = true)
    val serviceId: Long
)

data class SecurityKeyWithServices(
    @Embedded val securityKey: SecurityKey,
    @Relation(
        parentColumn = "securityKeyId",
        entityColumn = "serviceId",
        associateBy = Junction(SecurityKeyServiceCrossRef::class)
    )
    val services: List<Service>
)
