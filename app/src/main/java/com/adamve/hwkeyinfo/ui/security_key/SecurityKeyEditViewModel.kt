package com.adamve.hwkeyinfo.ui.security_key

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

class SecurityKeyEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val securityKeyRepository: SecurityKeyRepository
) : ViewModel() {
    var securityKeyUiState by mutableStateOf(SecurityKeyUiState())
        private set

    private val securityKeyId: Long =
        checkNotNull(savedStateHandle[SecurityKeyEditDestination.securityKeyIdArg])

    init {
        viewModelScope.launch {
            securityKeyUiState = securityKeyRepository.getSecurityKeyStream(securityKeyId)
                .filterNotNull()
                .first()
                .toSecurityKeyUiState(true)
        }
    }

    suspend fun deleteSecurityKey() {
        securityKeyRepository.deleteSecurityKey(securityKeyUiState.details.toSecurityKey())
    }

    suspend fun updateSecurityKey() {
        if (validateInput(securityKeyUiState.details)) {
            val securityKey = securityKeyUiState.details.toSecurityKey()
            securityKeyRepository.updateSecurityKey(securityKey)
        }
    }

    fun updateSecurityKeyUiState(details: SecurityKeyDetails) {
        securityKeyUiState =
            SecurityKeyUiState(details = details, isEntryValid = validateInput(details))
    }

    private fun validateInput(uiState: SecurityKeyDetails = securityKeyUiState.details): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}