package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.Service
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SecurityKeyEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val securityKeyRepository: SecurityKeyRepository
) : ViewModel() {
    var securityKeyUiState by mutableStateOf(SecurityKeyUiState())
        private set

    val serviceListUiState: StateFlow<ServiceListUiState> =
        securityKeyRepository.getAllServicesStream()
            .map { ServiceListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ServiceListUiState()
            )

    private val securityKeyId: Long? =
        savedStateHandle[SecurityKeyEditDestination.securityKeyIdArg]

    init {
        viewModelScope.launch {
            if (securityKeyId != null) {
                securityKeyUiState = securityKeyRepository.getSecurityKeyStream(securityKeyId)
                    .filterNotNull()
                    .first()
                    .toSecurityKeyUiState(true)
            } else {
                securityKeyUiState = SecurityKeyUiState(isAddingNew = true)
            }
        }
    }

    suspend fun deleteSecurityKey() {
        securityKeyRepository.deleteSecurityKey(securityKeyUiState.details.toSecurityKey())
    }

    suspend fun createSecurityKey() {
        if (validateInput()) {
            val securityKey = securityKeyUiState.details.toSecurityKey()
            securityKeyRepository.insertSecurityKey(securityKey)
        }
    }

    suspend fun updateSecurityKey() {
        if (validateInput(securityKeyUiState.details)) {
            val securityKey = securityKeyUiState.details.toSecurityKey()
            securityKeyRepository.updateSecurityKey(securityKey)
        }
    }

    fun updateSecurityKeyUiState(details: SecurityKeyDetails, isAddingNew: Boolean) {
        securityKeyUiState =
            SecurityKeyUiState(
                details = details,
                isAddingNew = isAddingNew,
                isEntryValid = validateInput(details)
            )
    }

    private fun validateInput(uiState: SecurityKeyDetails = securityKeyUiState.details): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ServiceListUiState(
    val allServices: List<Service> = listOf()
)

data class SecurityKeyUiState(
    val details: SecurityKeyDetails = SecurityKeyDetails(),
    val isAddingNew: Boolean = false,
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