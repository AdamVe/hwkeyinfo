package com.adamve.hwkeyinfo.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ServiceListViewModel(
    repository: SecurityKeyRepository
) : ViewModel() {
    val serviceListUiState: StateFlow<ServiceListUiState> =
        repository.getAllServicesWithSecurityKeysStream().map { ServiceListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ServiceListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ServiceListUiState(val serviceList: List<ServiceWithSecurityKeys> = listOf())