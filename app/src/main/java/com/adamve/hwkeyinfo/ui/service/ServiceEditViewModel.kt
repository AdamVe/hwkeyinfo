package com.adamve.hwkeyinfo.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.data.ServiceRepository
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiceEditViewModel(
    savedStateHandle: SavedStateHandle,
    securityKeyRepository: SecurityKeyRepository,
    private val serviceRepository: ServiceRepository

) : ViewModel() {
    var serviceUiState by mutableStateOf(ServiceUiState())
        private set

    val securityKeyListUiState: StateFlow<SecurityKeyListUiState> =
        securityKeyRepository.getAllSecurityKeysStream()
            .map { SecurityKeyListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SecurityKeyListUiState()
            )

    private val serviceId: Long? =
        savedStateHandle[ServiceEditDestination.serviceIdArg]

    init {
        viewModelScope.launch {
            serviceUiState = if (serviceId != null) {
                serviceRepository.getServiceWithSecurityKeysStream(serviceId)
                    .filterNotNull()
                    .first()
                    .toServiceUiState(true)
            } else {
                ServiceUiState(isAddingNew = true)
            }
        }
    }

    suspend fun deleteService() {
        serviceRepository.deleteService(serviceUiState.details.toService())
    }

    suspend fun saveService() {
        if (validateInput()) {
            val service = serviceUiState.details.toService()
            serviceRepository.insertService(service, serviceUiState.details.securityKeys)
        }
    }

    suspend fun updateService() {
        if (validateInput(serviceUiState.details)) {
            val service = serviceUiState.details.toService()
            serviceRepository.updateService(service, serviceUiState.details.securityKeys)
        }
    }

    fun updateServiceUiState(details: ServiceDetails, isAddingNew: Boolean) {
        serviceUiState =
            ServiceUiState(
                details = details,
                isAddingNew = isAddingNew,
                isEntryValid = validateInput(details)
            )
    }

    private fun validateInput(uiState: ServiceDetails = serviceUiState.details): Boolean {
        return with(uiState) {
            serviceName.isNotBlank() and serviceUser.isNotBlank()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SecurityKeyListUiState(
    val allSecurityKeys: List<SecurityKey> = listOf()
)

data class ServiceUiState(
    val details: ServiceDetails = ServiceDetails(),
    val isAddingNew: Boolean = false,
    val isEntryValid: Boolean = false
)

data class ServiceDetails(
    val serviceId: Long = 0L,
    val serviceName: String = "",
    val serviceUser: String = "",
    val serviceDetails: String? = null,
    val securityKeys: List<Long> = listOf()
)

fun ServiceDetails.toService(): Service = Service(
    serviceId = serviceId,
    serviceName = serviceName,
    serviceUser = serviceUser
)

fun ServiceWithSecurityKeys.toServiceUiState(isEntryValid: Boolean = false): ServiceUiState =
    ServiceUiState(
        details = this.toServiceDetails(),
        isEntryValid = isEntryValid
    )

fun ServiceWithSecurityKeys.toServiceDetails(): ServiceDetails = ServiceDetails(
    serviceId = service.serviceId,
    serviceName = service.serviceName,
    serviceUser = service.serviceUser,
    securityKeys = securityKeys.map { it.id }
)