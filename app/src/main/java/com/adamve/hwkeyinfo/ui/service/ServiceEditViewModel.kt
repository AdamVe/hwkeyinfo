package com.adamve.hwkeyinfo.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import com.adamve.hwkeyinfo.data.Service
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ServiceEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SecurityKeyRepository
) : ViewModel() {
    var serviceUiState by mutableStateOf(ServiceUiState())
        private set

    private val serviceId: Long? =
        savedStateHandle[ServiceEditDestination.serviceIdArg]

    init {
        if (serviceId != null) {
            viewModelScope.launch {
                serviceUiState = repository.getServiceStream(serviceId)
                    .filterNotNull()
                    .first()
                    .toServiceUiState(true)
            }
        } else {
            serviceUiState = ServiceUiState(isAddingNew = true)
        }
    }

    suspend fun deleteService() {
        repository.deleteService(serviceUiState.details.toService())
    }

    suspend fun saveService() {
        if (validateInput()) {
            val service = serviceUiState.details.toService()
            repository.insertService(service)
        }
    }

    suspend fun updateService() {
        if (validateInput(serviceUiState.details)) {
            val service = serviceUiState.details.toService()
            repository.updateService(service)
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
}


data class ServiceUiState(
    val details: ServiceDetails = ServiceDetails(),
    val isAddingNew: Boolean = false,
    val isEntryValid: Boolean = false
)

data class ServiceDetails(
    val serviceId: Long = 0L,
    val serviceName: String = "",
    val serviceUser: String = "",
    val serviceDetails: String? = null
)

fun ServiceDetails.toService(): Service = Service(
    serviceId = serviceId,
    serviceName = serviceName,
    serviceUser = serviceUser,
    serviceDetails = serviceDetails
)

fun Service.toServiceUiState(isEntryValid: Boolean = false): ServiceUiState =
    ServiceUiState(
        details = this.toServiceDetails(),
        isEntryValid = isEntryValid
    )

fun Service.toServiceDetails(): ServiceDetails = ServiceDetails(
    serviceId = serviceId,
    serviceName = serviceName,
    serviceUser = serviceUser,
    serviceDetails = serviceDetails
)