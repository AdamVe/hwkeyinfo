package com.adamve.hwkeyinfo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SecurityKeyDao {
    @Query("SELECT * FROM security_keys")
    fun getAllSecurityKeys(): Flow<List<SecurityKey>>

    @Query("SELECT * FROM security_keys WHERE securityKeyId = :id")
    fun getSecurityKey(id: Long): Flow<SecurityKey>

    @Insert
    suspend fun insertSecurityKey(securityKey: SecurityKey): Long

    @Delete
    suspend fun deleteSecurityKey(securityKey: SecurityKey)

    @Update
    suspend fun updateSecurityKey(securityKey: SecurityKey)

    // relations
    @Transaction
    @Query("SELECT * FROM security_keys")
    fun getAllSecurityKeysWithServices(): Flow<List<SecurityKeyWithServices>>

    @Transaction
    @Query("SELECT * FROM security_keys WHERE securityKeyId = :id")
    fun getSecurityKeyWithServices(id: Long): Flow<SecurityKeyWithServices>

    @Insert
    suspend fun insertSecurityKeyServiceCrossRef(securityKeyServiceCrossRef: SecurityKeyServiceCrossRef)

    @Query("DELETE FROM SecurityKeyServiceCrossRef WHERE securityKeyId = :id")
    suspend fun deleteSecurityKeyServices(id: Long)

    @Transaction
    suspend fun insertSecurityKey(securityKey: SecurityKey, services: List<Long>) {
        val id = insertSecurityKey(securityKey)
        services.forEach {
            insertSecurityKeyServiceCrossRef(
                SecurityKeyServiceCrossRef(
                    id,
                    it
                )
            )
        }
    }

    @Transaction
    suspend fun updateSecurityKey(securityKey: SecurityKey, services: List<Long>) {
        updateSecurityKey(securityKey)
        deleteSecurityKeyServices(securityKey.id)
        services.forEach {
            insertSecurityKeyServiceCrossRef(
                SecurityKeyServiceCrossRef(
                    securityKey.id,
                    it
                )
            )
        }
    }

}