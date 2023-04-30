package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.adamve.hwkeyinfo.ui.security_key.CustomTextField
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme
import kotlinx.coroutines.launch

object ServiceEntryDestination : AppDestination {
    override val route = "service_add"
    override val titleRes = R.string.app_page_title_service_entry
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ServiceEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = ServiceEntryDestination.titleRes)) },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.saveService()
                                navigateBack()
                            }
                        },
                        enabled = viewModel.serviceUiState.isEntryValid
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.service_entry_screen_action_create)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.service_entry_screen_navigation_back)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        ServiceEntryBody(
            serviceUiState = viewModel.serviceUiState,
            onServiceValueChange = viewModel::updateServiceUiState,
            modifier = modifier.padding(innerPadding)
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
    serviceUiState: ServiceUiState,
    onServiceValueChange: (ServiceDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ServiceInputForm(
            serviceDetails = serviceUiState.details,
            onValueChange = onServiceValueChange
        )
    }
}

@Preview(widthDp = 320)
@Composable
fun ServiceInputFormPreview() {
    HwKeyInfoTheme {
        ServiceInputForm(
            serviceDetails = ServiceDetails(
                serviceName = "Email provider",
                serviceUser = "User account"
            )
        )
    }
}

