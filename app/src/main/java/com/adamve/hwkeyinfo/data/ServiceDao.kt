package com.adamve.hwkeyinfo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {

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
    @Query("SELECT * FROM service")
    fun getAllServicesWithSecurityKeys(): Flow<List<ServiceWithSecurityKeys>>

    @Transaction
    @Query("SELECT * FROM service WHERE serviceId = :serviceId")
    fun getServiceWithSecurityKeys(serviceId: Long): Flow<ServiceWithSecurityKeys>

    @Insert
    suspend fun insertSecurityKeyServiceCrossRef(securityKeyServiceCrossRef: SecurityKeyServiceCrossRef)

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

    @Query("DELETE FROM SecurityKeyServiceCrossRef WHERE securityKeyId = :id")
    suspend fun deleteSecurityKeyServices(id: Long)

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
}