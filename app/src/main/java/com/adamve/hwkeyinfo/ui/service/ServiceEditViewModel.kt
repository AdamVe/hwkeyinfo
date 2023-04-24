package com.adamve.hwkeyinfo.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ServiceEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SecurityKeyRepository
) : ViewModel() {
    var serviceUiState by mutableStateOf(ServiceUiState())
        private set

    private val serviceId: Long =
        checkNotNull(savedStateHandle[ServiceEditDestination.serviceIdArg])

    init {
        viewModelScope.launch {
            serviceUiState = repository.getServiceStream(serviceId)
                .filterNotNull()
                .first()
                .toServiceUiState(true)
        }
    }

    suspend fun deleteService() {
        repository.deleteService(serviceUiState.details.toService())
    }

    suspend fun updateService() {
        if (validateInput(serviceUiState.details)) {
            val service = serviceUiState.details.toService()
            repository.updateService(service)
        }
    }

    fun updateServiceUiState(details: ServiceDetails) {
        serviceUiState =
            ServiceUiState(details = details, isEntryValid = validateInput(details))
    }

    private fun validateInput(uiState: ServiceDetails = serviceUiState.details): Boolean {
        return with(uiState) {
            serviceName.isNotBlank() and serviceUser.isNotBlank()
        }
    }
}