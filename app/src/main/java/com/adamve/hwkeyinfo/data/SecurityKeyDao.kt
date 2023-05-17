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

    // Service
    @Query("SELECT * FROM service")
    fun getAllServices(): Flow<List<Service>>

    @Query("SELECT * FROM service WHERE serviceId = :id")
    fun getService(id: Long): Flow<Service>

    @Insert
    suspend fun insertService(service: Service): Long

    @Delete
    suspend fun deleteService(service: Service)

    @Update
    suspend fun updateService(service: Service)

    @Transaction
    suspend fun insertService(service: Service, securityKeys: List<Long>) {
        val id = insertService(service)
        securityKeys.forEach {
            insertSecurityKeyServiceCrossRef(
                SecurityKeyServiceCrossRef(
                    it,
                    id
                )
            )
        }
    }

    @Transaction
    suspend fun updateService(service: Service, securityKeys: List<Long>) {
        updateService(service)
        deleteSecurityKeyServices(service.serviceId)
        securityKeys.forEach {
            insertSecurityKeyServiceCrossRef(
                SecurityKeyServiceCrossRef(
                    it,
                    service.serviceId
                )
            )
        }
    }

    // relations
    @Transaction
    @Query("SELECT * FROM security_keys")
    fun getAllSecurityKeysWithServices(): Flow<List<SecurityKeyWithServices>>

    @Transaction
    @Query("SELECT * FROM security_keys WHERE securityKeyId = :id")
    fun getSecurityKeyWithServices(id: Long): Flow<SecurityKeyWithServices>

    @Transaction
    @Query("SELECT * FROM service")
    fun getAllServicesWithSecurityKeys(): Flow<List<ServiceWithSecurityKeys>>

    @Transaction
    @Query("SELECT * FROM service WHERE serviceId = :serviceId")
    fun getServiceWithSecurityKeys(serviceId: Long): Flow<ServiceWithSecurityKeys>

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