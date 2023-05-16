package com.adamve.hwkeyinfo.data

import kotlinx.coroutines.flow.Flow

class SecurityKeyRepository(private val securityKeyDao: SecurityKeyDao) {

    fun getAllSecurityKeysWithServicesStream(): Flow<List<SecurityKeyWithServices>> =
        securityKeyDao.getAllSecurityKeysWithServices()

    fun getSecurityKeyWithServicesStream(id: Long): Flow<SecurityKeyWithServices?> =
        securityKeyDao.getSecurityKeyWithServices(id)

    suspend fun deleteSecurityKey(securityKey: SecurityKey) =
        securityKeyDao.deleteSecurityKey(securityKey)

    // Service
    fun getAllServicesWithSecurityKeysStream(): Flow<List<ServiceWithSecurityKeys>> =
        securityKeyDao.getAllServicesWithSecurityKeys()

    fun getAllServicesStream(): Flow<List<Service>> = securityKeyDao.getAllServices()

    fun getServiceStream(id: Long): Flow<Service?> = securityKeyDao.getService(id)

    suspend fun insertService(newService: Service) = securityKeyDao.insertService(newService)

    suspend fun deleteService(service: Service) = securityKeyDao.deleteService(service)

    suspend fun updateService(service: Service) = securityKeyDao.updateService(service)

    // relations
    suspend fun insertSecurityKey(newSecurityKey: SecurityKey, services: List<Long>) =
        securityKeyDao.insertSecurityKey(newSecurityKey, services)

    suspend fun updateSecurityKey(securityKey: SecurityKey, services: List<Long>) =
        securityKeyDao.updateSecurityKey(securityKey, services)
}