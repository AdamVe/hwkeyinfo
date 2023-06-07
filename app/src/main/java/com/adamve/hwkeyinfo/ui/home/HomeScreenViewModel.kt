package com.adamve.hwkeyinfo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import com.adamve.hwkeyinfo.data.ServiceRepository
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyListUiState
import com.adamve.hwkeyinfo.ui.service.ServiceListUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(
    securityKeyRepository: SecurityKeyRepository,
    serviceRepository: ServiceRepository
) : ViewModel() {
    val securityKeyUiState: StateFlow<SecurityKeyListUiState> =
        securityKeyRepository.getAllSecurityKeysWithServicesStream()
            .map { SecurityKeyListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SecurityKeyListUiState()
            )

    val serviceListUiState: StateFlow<ServiceListUiState> =
        serviceRepository.getAllServicesWithSecurityKeysStream().map { ServiceListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ServiceListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
