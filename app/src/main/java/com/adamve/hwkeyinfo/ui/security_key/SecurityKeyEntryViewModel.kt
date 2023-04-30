package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.Service

class SecurityKeyEntryViewModel(
    private val securityKeyRepository: SecurityKeyRepository
) : ViewModel() {

    var securityKeyUiState by mutableStateOf(SecurityKeyUiState())
        private set

    var serviceListUiState by mutableStateOf(ServiceListUiState())
        private set

    fun updateSecurityKeyUiState(details: SecurityKeyDetails) {
        securityKeyUiState =
            SecurityKeyUiState(details = details, isEntryValid = validateInput(details))
    }

    suspend fun saveSecurityKey() {
        if (validateInput()) {
            val securityKey = securityKeyUiState.details.toSecurityKey()
            securityKeyRepository.insertSecurityKey(securityKey)
        }
    }

    private fun validateInput(uiState: SecurityKeyDetails = securityKeyUiState.details): Boolean {
        return with(uiState) {
            name.isNotBlank() or type.isNotBlank()
        }
    }
}

data class ServiceListUiState(
    val allServices: List<Service> = listOf()
)

data class SecurityKeyUiState(
    val details: SecurityKeyDetails = SecurityKeyDetails(),
    val isEntryValid: Boolean = false
)

data class KeyServiceDetails(
    val serviceId: Long = 0L,
    val serviceName: String = "",
    val serviceUser: String = "",
    val usedByKey: Boolean = false,
)

data class SecurityKeyDetails(
    val id: Long = 0L,
    val name: String = "",
    val type: String = "",
    val description: String = "",
    val services: List<KeyServiceDetails> = listOf()
)

fun SecurityKeyDetails.toSecurityKey(): SecurityKey = SecurityKey(
    id = id,
    name = name,
    type = type,
    description = description
)

fun SecurityKeyDetails.toSecurityKeyWithServices(): SecurityKeyWithServices =
    SecurityKeyWithServices(
        SecurityKey(
            id = id,
            name = name,
            type = type,
            description = description
        ),
        services = services
            .filter { it.usedByKey }
            .map {
                Service(
                    serviceId = it.serviceId,
                    serviceName = it.serviceName,
                    serviceUser = it.serviceUser
                )
            }
    )

fun SecurityKey.toSecurityKeyUiState(isEntryValid: Boolean = false): SecurityKeyUiState =
    SecurityKeyUiState(
        details = this.toSecurityKeyDetails(),
        isEntryValid = isEntryValid
    )

fun SecurityKey.toSecurityKeyDetails(): SecurityKeyDetails = SecurityKeyDetails(
    id = id,
    name = name,
    type = type,
    description = description
)
