package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.widgets.CustomTextField
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme
import kotlinx.coroutines.launch

object ServiceEditDestination : AppDestination {
    override val route = "service_edit"
    override val titleRes = R.string.app_page_title_service_edit
    const val serviceIdArg = "serviceId"
    val routeWithArgs = "$route/{$serviceIdArg}"
    const val addServiceRoute = "service_add"
    val addServiceTitleRes = R.string.app_page_title_service_add
}

@Composable
fun ServiceEditScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: ServiceEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    ServiceEditScreenContent(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        onCreate = {
            coroutineScope.launch {
                viewModel.saveService()
                navigateBack()
            }
        },
        onDelete = {
            coroutineScope.launch {
                viewModel.deleteService()
                navigateBack()
            }
        },
        onUpdate = {
            coroutineScope.launch {
                viewModel.updateService()
                navigateBack()
            }
        },
        onDetailsChanged = {
            viewModel.updateServiceUiState(
                it,
                viewModel.serviceUiState.isAddingNew
            )
        },
        serviceUiState = viewModel.serviceUiState
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEditScreenContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onCreate: () -> Unit = {},
    onDelete: () -> Unit = {},
    onUpdate: () -> Unit = {},
    onDetailsChanged: (ServiceDetails) -> Unit = {},
    serviceUiState: ServiceUiState = ServiceUiState(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            id = if (serviceUiState.isAddingNew) {
                                ServiceEditDestination.addServiceTitleRes
                            } else {
                                ServiceEditDestination.titleRes
                            }
                        )
                    )
                },
                actions = {
                    if (serviceUiState.isAddingNew) {
                        IconButton(
                            onClick = onCreate,
                            enabled = serviceUiState.isEntryValid
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.service_edit_screen_action_create)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = onDelete,
                            enabled = serviceUiState.isEntryValid
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.service_edit_screen_action_delete)
                            )
                        }
                        IconButton(
                            onClick = onUpdate,
                            enabled = serviceUiState.isEntryValid
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.service_edit_screen_action_update)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.service_edit_screen_navigation_back)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        ServiceEntryBody(
            modifier = modifier.padding(innerPadding),
            serviceUiState = serviceUiState,
            onServiceValueChange = onDetailsChanged
        )

    }
}

@Composable
fun ServiceInputForm(
    serviceDetails: ServiceDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ServiceDetails) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(text = stringResource(R.string.security_key_input_form_information_header))
            Row {
                CustomTextField(
                    title = stringResource(R.string.service_input_form_name_title),
                    value = serviceDetails.serviceName,
                    onValueChange = { onValueChange(serviceDetails.copy(serviceName = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                )
            }

            CustomTextField(
                title = stringResource(R.string.service_input_form_account_title),
                value = serviceDetails.serviceUser,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { onValueChange(serviceDetails.copy(serviceUser = it)) },
                keyboardType = KeyboardType.Text
            )
        }
    }
}

@Composable
fun ServiceEntryBody(
    modifier: Modifier = Modifier,
    serviceUiState: ServiceUiState,
    onServiceValueChange: (ServiceDetails) -> Unit
) {
    Column(modifier = modifier) {
        ServiceInputForm(
            serviceDetails = serviceUiState.details,
            onValueChange = onServiceValueChange
        )
    }
}

@Preview
@Composable
fun CreateServiceEditScreenPreview() {
    HwKeyInfoTheme {
        ServiceEditScreenContent(
            serviceUiState = previewServiceUiState.copy(isAddingNew = true)
        )
    }
}
@Preview
@Composable
fun EditServiceEditScreenPreview() {
    HwKeyInfoTheme {
        ServiceEditScreenContent(
            serviceUiState = previewServiceUiState.copy(isAddingNew = false)
        )
    }
}

val previewServiceUiState = ServiceUiState(
    details = ServiceDetails(
        serviceName = "Some service",
        serviceUser = "user@service.com"
    )
)