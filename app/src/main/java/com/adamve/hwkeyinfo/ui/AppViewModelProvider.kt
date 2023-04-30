package com.adamve.hwkeyinfo.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.adamve.hwkeyinfo.HwKeyInfoApplication
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyEditViewModel
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyListViewModel
import com.adamve.hwkeyinfo.ui.service.ServiceEditViewModel
import com.adamve.hwkeyinfo.ui.service.ServiceEntryViewModel
import com.adamve.hwkeyinfo.ui.service.ServiceListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SecurityKeyEditViewModel(
                this.createSavedStateHandle(),
                hwKeyInfoApplication().container.securityKeyRepository
            )
        }

        initializer {
            SecurityKeyListViewModel(hwKeyInfoApplication().container.securityKeyRepository)
        }

        initializer {
            ServiceListViewModel(hwKeyInfoApplication().container.securityKeyRepository)
        }

        initializer {
            ServiceEntryViewModel(hwKeyInfoApplication().container.securityKeyRepository)
        }

        initializer {
            ServiceEditViewModel(
                this.createSavedStateHandle(),
                hwKeyInfoApplication().container.securityKeyRepository
            )
        }
    }
}

fun CreationExtras.hwKeyInfoApplication(): HwKeyInfoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as HwKeyInfoApplication)