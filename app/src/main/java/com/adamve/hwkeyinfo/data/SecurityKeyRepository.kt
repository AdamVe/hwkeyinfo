package com.adamve.hwkeyinfo.data

import kotlinx.coroutines.flow.Flow

class SecurityKeyRepository(private val securityKeyDao: SecurityKeyDao) {

    fun getAllSecurityKeysWithServicesStream(): Flow<List<SecurityKeyWithServices>> = securityKeyDao.getAllSecurityKeysWithServices()

    fun getAllSecurityKeysStream(): Flow<List<SecurityKey>> = securityKeyDao.getAllSecurityKeys()

    fun getSecurityKeyStream(id: Long): Flow<SecurityKey?> = securityKeyDao.getSecurityKey(id)

    suspend fun insertSecurityKey(newSecurityKey: SecurityKey) =
        securityKeyDao.insertSecurityKey(newSecurityKey)

    suspend fun deleteSecurityKey(securityKey: SecurityKey) =
        securityKeyDao.deleteSecurityKey(securityKey)

    suspend fun updateSecurityKey(securityKey: SecurityKey) =
        securityKeyDao.updateSecurityKey(securityKey)

    // Service
    fun getAllServicesStream(): Flow<List<Service>> = securityKeyDao.getAllServices()

    fun getServiceStream(id: Long): Flow<Service?> = securityKeyDao.getService(id)

    suspend fun insertService(newService: Service) = securityKeyDao.insertService(newService)

    suspend fun deleteService(service: Service) = securityKeyDao.deleteService(service)

    suspend fun updateService(service: Service) = securityKeyDao.updateService(service)
}