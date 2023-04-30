package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import kotlinx.coroutines.launch

object SecurityKeyEditDestination : AppDestination {
    override val route = "security_key_edit"
    override val titleRes = R.string.app_page_title_key_edit
    const val securityKeyIdArg = "securityKeyId"
    val routeWithArgs = "$route/{$securityKeyIdArg}"
}

@Composable
fun CustomTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        label = { Text(title) },
        leadingIcon = leadingIcon,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityKeyEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityKeyEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = SecurityKeyEditDestination.titleRes)) },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.deleteSecurityKey()
                                navigateBack()
                            }
                        },
                        enabled = viewModel.securityKeyUiState.isEntryValid
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.security_key_edit_screen_action_delete)
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.updateSecurityKey()
                                navigateBack()
                            }
                        },
                        enabled = viewModel.securityKeyUiState.isEntryValid
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.security_key_edit_screen_action_update)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.security_key_edit_screen_navigation_back)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        val allServices by viewModel.serviceListUiState.collectAsState()

        SecurityKeyEntryBody(
            securityKeyUiState = viewModel.securityKeyUiState,
            serviceListUiState = allServices,
            onSecurityKeyValueChange = viewModel::updateSecurityKeyUiState,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SecurityKeyEntryBody(
    securityKeyUiState: SecurityKeyUiState,
    serviceListUiState: ServiceListUiState,
    onSecurityKeyValueChange: (SecurityKeyDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SecurityKeyInputForm(
            securityKeyDetails = securityKeyUiState.details,
            serviceListUiState = serviceListUiState,
            onValueChange = onSecurityKeyValueChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SecurityKeyInputForm(
    securityKeyDetails: SecurityKeyDetails,
    serviceListUiState: ServiceListUiState,
    modifier: Modifier = Modifier,
    onValueChange: (SecurityKeyDetails) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(text = stringResource(R.string.security_key_input_form_information_header))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                CustomTextField(
                    title = stringResource(R.string.security_key_input_form_key_title),
                    value = securityKeyDetails.name,
                    onValueChange = { onValueChange(securityKeyDetails.copy(name = it)) },
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.weight(2f)
                )
            }

            CustomTextField(
                title = stringResource(R.string.security_key_input_form_key_model_title),
                value = securityKeyDetails.type,
                onValueChange = { onValueChange(securityKeyDetails.copy(type = it)) },
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                title = stringResource(R.string.security_key_input_form_key_description_title),
                value = securityKeyDetails.description,
                onValueChange = { onValueChange(securityKeyDetails.copy(description = it)) },
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(text = stringResource(R.string.security_key_input_form_services_header))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            serviceListUiState.allServices.forEach { service ->
                FilterChip(
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                    selected = securityKeyDetails.services.any {
                        (it.serviceId == service.serviceId) and (it.usedByKey)
                    },
                    onClick = {},
                    label = {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                        ) {
                            Text(service.serviceName)
                            Text(service.serviceUser)
                        }
                    })
            }
        }
    }
}

@Preview
@Composable
fun InputFormPreview() {
    SecurityKeyInputForm(
        securityKeyDetails = SecurityKeyDetails(
            services = listOf(
                KeyServiceDetails(serviceId = 1, usedByKey = true),
                KeyServiceDetails(serviceId = 2, usedByKey = false),
                KeyServiceDetails(serviceId = 3, usedByKey = false),
                KeyServiceDetails(serviceId = 4, usedByKey = false),
                KeyServiceDetails(serviceId = 5, usedByKey = false),
                )
        ),
        serviceListUiState = ServiceListUiState(
            listOf(
                Service(serviceId = 1, serviceName = "Email service 1", serviceUser = "user@email1.com"),
                Service(serviceId = 2, serviceName = "Email 2", serviceUser = "user@email2.com"),
                Service(serviceId = 3, serviceName = "Local login / TOTP", serviceUser = "Something"),
                Service(serviceId = 4, serviceName = "Not enabled service", serviceUser = "user"),
                Service(serviceId = 5, serviceName = "Not enabled service", serviceUser = "user"),
            )
        ),
        onValueChange = {})
}