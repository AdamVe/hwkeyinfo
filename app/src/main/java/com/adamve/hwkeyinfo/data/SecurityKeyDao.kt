package com.adamve.hwkeyinfo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SecurityKeyDao {
    @Query("SELECT * FROM security_keys")
    fun getAllSecurityKeys(): Flow<List<SecurityKey>>

    @Query("SELECT * FROM security_keys WHERE securityKeyId = :id")
    fun getSecurityKey(id: Long): Flow<SecurityKey>

    @Insert
    suspend fun insertSecurityKey(securityKey: SecurityKey)

    @Delete
    suspend fun deleteSecurityKey(securityKey: SecurityKey)

    @Update
    suspend fun updateSecurityKey(securityKey: SecurityKey)


    // Service
    @Query("SELECT * FROM service")
    fun getAllServices(): Flow<List<Service>>

    @Query("SELECT * FROM service WHERE serviceId = :id")
    fun getService(id: Long): Flow<Service>

    @Insert
    suspend fun insertService(service: Service)

    @Delete
    suspend fun deleteService(service: Service)

    @Update
    suspend fun updateService(service: Service)

    // relations
    @Query("SELECT * FROM security_keys")
    fun getAllSecurityKeysWithServices(): Flow<List<SecurityKeyWithServices>>
}