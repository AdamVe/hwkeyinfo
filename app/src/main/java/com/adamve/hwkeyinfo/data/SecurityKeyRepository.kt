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

    // relations
    suspend fun insertSecurityKey(newSecurityKey: SecurityKey, services: List<Long>) =
        securityKeyDao.insertSecurityKey(newSecurityKey, services)

    suspend fun updateSecurityKey(securityKey: SecurityKey, services: List<Long>) =
        securityKeyDao.updateSecurityKey(securityKey, services)
}