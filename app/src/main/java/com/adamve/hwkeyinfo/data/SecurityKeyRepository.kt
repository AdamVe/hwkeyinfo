package com.adamve.hwkeyinfo.data

import kotlinx.coroutines.flow.Flow

class SecurityKeyRepository(private val securityKeyDao: SecurityKeyDao) {

    fun getAllSecurityKeysStream(): Flow<List<SecurityKey>> = securityKeyDao.getAllSecurityKeys()

    fun getAllSecurityKeysWithServicesStream(): Flow<List<SecurityKeyWithServices>> =
        securityKeyDao.getAllSecurityKeysWithServices()

    fun getSecurityKeyWithServicesStream(id: Long): Flow<SecurityKeyWithServices?> =
        securityKeyDao.getSecurityKeyWithServices(id)

    suspend fun deleteSecurityKey(securityKey: SecurityKey) =
        securityKeyDao.deleteSecurityKey(securityKey)

    // Service
    fun getAllServicesStream(): Flow<List<Service>> = securityKeyDao.getAllServices()

    fun getAllServicesWithSecurityKeysStream(): Flow<List<ServiceWithSecurityKeys>> =
        securityKeyDao.getAllServicesWithSecurityKeys()

    fun getServiceWithSecurityKeysStream(serviceId: Long): Flow<ServiceWithSecurityKeys?> =
        securityKeyDao.getServiceWithSecurityKeys(serviceId)

    suspend fun deleteService(service: Service) = securityKeyDao.deleteService(service)

    // relations
    suspend fun insertSecurityKey(newSecurityKey: SecurityKey, services: List<Long>) =
        securityKeyDao.insertSecurityKey(newSecurityKey, services)

    suspend fun updateSecurityKey(securityKey: SecurityKey, services: List<Long>) =
        securityKeyDao.updateSecurityKey(securityKey, services)

    suspend fun insertService(newService: Service, securityKeys: List<Long>) =
        securityKeyDao.insertService(newService, securityKeys)

    suspend fun updateService(service: Service, securityKeys: List<Long>) =
        securityKeyDao.updateService(service, securityKeys)
}