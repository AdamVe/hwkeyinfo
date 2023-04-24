package com.adamve.hwkeyinfo.ui.security_key

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SecurityKeyListViewModel(
    securityKeyRepository: SecurityKeyRepository
) : ViewModel() {
    val securityKeyListUiState: StateFlow<SecurityKeyListUiState> =
        securityKeyRepository.getAllSecurityKeysStream().map { SecurityKeyListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SecurityKeyListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SecurityKeyListUiState(val itemList: List<SecurityKey> = listOf())