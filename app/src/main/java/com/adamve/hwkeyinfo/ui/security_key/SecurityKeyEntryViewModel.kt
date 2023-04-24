package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyRepository
class SecurityKeyEntryViewModel(
    private val securityKeyRepository: SecurityKeyRepository
) : ViewModel() {

    var securityKeyUiState by mutableStateOf(SecurityKeyUiState())
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
            name.isNotBlank()
        }
    }
}

data class SecurityKeyUiState(
    val details: SecurityKeyDetails = SecurityKeyDetails(),
    val isEntryValid: Boolean = false
)

data class SecurityKeyDetails(
    val id: Long = 0L,
    val name: String = "",
    val description: String = ""
)

fun SecurityKeyDetails.toSecurityKey(): SecurityKey = SecurityKey(
    id = id,
    name = name,
    description = description
)

fun SecurityKey.toSecurityKeyUiState(isEntryValid: Boolean = false): SecurityKeyUiState =
    SecurityKeyUiState(
        details = this.toSecurityKeyDetails(),
        isEntryValid = isEntryValid
    )

fun SecurityKey.toSecurityKeyDetails(): SecurityKeyDetails = SecurityKeyDetails(
    id = id,
    name = name,
    description = description
)