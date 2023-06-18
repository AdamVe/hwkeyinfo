package com.adamve.hwkeyinfo.preview

import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyListUiState
import com.adamve.hwkeyinfo.ui.service.ServiceListUiState

class PreviewData {

    companion object {

        val services = listOf(
            Service(
                serviceId = 0,
                serviceName = "E-mail Provider",
                serviceUser = "developer@email.com"
            ),
            Service(
                serviceId = 1,
                serviceName = "E-mail Provider",
                serviceUser = "private@email.com"
            ),
            Service(
                serviceId = 2,
                serviceName = "Note taking app",
                serviceUser = "private@email.com"
            ),
            Service(
                serviceId = 3,
                serviceName = "OATH service",
                serviceUser = "developer@email.com"
            ),
            Service(serviceId = 4, serviceName = "Music", serviceUser = "private@email.com"),
            Service(serviceId = 5, serviceName = "Another e-mail", serviceUser = "alias@email.com"),
            Service(
                serviceId = 6,
                serviceName = "Social network",
                serviceUser = "private@email.com"
            ),
            Service(
                serviceId = 7,
                serviceName = "Blogging platform",
                serviceUser = "private@email.com"
            ),
        )
        val keys = listOf(
            SecurityKey(id = 1, name = "Main key", type = "HW Key Type 1"),
            SecurityKey(id = 10, name = "Backup key", type = "HW Key Type 2"),
            SecurityKey(id = 100, name = "Work key", type = "HW Key Type 1"),
            SecurityKey(id = 200, name = "Main key 2", type = "HW Key Type 1"),
            SecurityKey(id = 21, name = "Backup key 3", type = "HW Key Type 2"),
            SecurityKey(id = 201, name = "Work key 5", type = "HW Key Type 1"),
        )
        val keyWithServices = listOf(
            SecurityKeyWithServices(
                keys[0], services.minus(services[1])
            ),

            SecurityKeyWithServices(
                keys[1], services
            ),

            SecurityKeyWithServices(
                keys[2], services
            ),

            SecurityKeyWithServices(
                keys[3], services
            ),

            SecurityKeyWithServices(
                keys[4], services
            ),
            SecurityKeyWithServices(
                keys[5], services
            ),
        )
        val keyWithServicesUiState = SecurityKeyListUiState(
            itemList = keyWithServices
        )
        private val servicesWithKeys = services.map {
            ServiceWithSecurityKeys(it, keys)
        }
        val servicesWithKeysUiState = ServiceListUiState(
            serviceList = servicesWithKeys
        )
    }
}