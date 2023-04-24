package com.adamve.hwkeyinfo.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import com.adamve.hwkeyinfo.data.Service

class ServiceEntryViewModel(
    private val repository: SecurityKeyRepository
) : ViewModel() {

    var serviceUiState by mutableStateOf(ServiceUiState())
        private set

    fun updateServiceUiState(details: ServiceDetails) {
        serviceUiState =
            ServiceUiState(details = details, isEntryValid = validateInput(details))
    }

    suspend fun saveService() {
        if (validateInput()) {
            val service = serviceUiState.details.toService()
            repository.insertService(service)
        }
    }

    private fun validateInput(uiState: ServiceDetails = serviceUiState.details): Boolean {
        return with(uiState) {
            serviceName.isNotBlank() and serviceUser.isNotBlank()
        }
    }
}

data class ServiceUiState(
    val details: ServiceDetails = ServiceDetails(),
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