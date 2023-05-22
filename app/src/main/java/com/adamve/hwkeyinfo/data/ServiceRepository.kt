package com.adamve.hwkeyinfo.data

import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val serviceDao: ServiceDao) {
    // Service
    fun getAllServicesStream(): Flow<List<Service>> = serviceDao.getAllServices()

    fun getAllServicesWithSecurityKeysStream(): Flow<List<ServiceWithSecurityKeys>> =
        serviceDao.getAllServicesWithSecurityKeys()

    fun getServiceWithSecurityKeysStream(serviceId: Long): Flow<ServiceWithSecurityKeys?> =
        serviceDao.getServiceWithSecurityKeys(serviceId)

    suspend fun deleteService(service: Service) = serviceDao.deleteService(service)

    // relations
    suspend fun insertService(newService: Service, securityKeys: List<Long>) =
        serviceDao.insertService(newService, securityKeys)

    suspend fun updateService(service: Service, securityKeys: List<Long>) =
        serviceDao.updateService(service, securityKeys)
}