package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.security_key.securityKeyComparator
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
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ServiceEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val securityKeyListUiState by viewModel.securityKeyListUiState.collectAsState()

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
        serviceUiState = viewModel.serviceUiState,
        securityKeyListUiState = securityKeyListUiState
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
    securityKeyListUiState: SecurityKeyListUiState = SecurityKeyListUiState()
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
            serviceUiState = serviceUiState,
            securityKeyListUiState = securityKeyListUiState,
            modifier = modifier.padding(innerPadding),
            onServiceValueChange = onDetailsChanged
        )

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServiceInputForm(
    serviceUiState: ServiceUiState,
    securityKeyListUiState: SecurityKeyListUiState,
    modifier: Modifier = Modifier,
    onValueChange: (ServiceDetails) -> Unit = {}
) {

    val focusRequester = FocusRequester()
    val serviceDetails = serviceUiState.details

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
            .padding(8.dp)
    ) {
        Text(text = stringResource(R.string.service_input_form_security_keys_header))
        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            securityKeyListUiState.allSecurityKeys.sortedWith(securityKeyComparator).forEach { securityKey ->
                SecurityKeyItem(
                    securityKey = securityKey,
                    modifier = Modifier.fillMaxWidth(),
                    value = serviceDetails.securityKeys.contains(securityKey.id),
                    onValueChange = {
                        onValueChange(
                            serviceDetails.copy(
                                securityKeys = if (serviceDetails.securityKeys.contains(securityKey.id)) {
                                    serviceDetails.securityKeys.minus(securityKey.id)
                                } else {
                                    serviceDetails.securityKeys.plus(securityKey.id)
                                }
                            )
                        )
                    }
                )
            }
        }
    }


    LaunchedEffect(Unit) {
        if (serviceUiState.isAddingNew) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun ServiceEntryBody(
    serviceUiState: ServiceUiState,
    securityKeyListUiState: SecurityKeyListUiState,
    modifier: Modifier = Modifier,
    onServiceValueChange: (ServiceDetails) -> Unit
) {
    Column(modifier = modifier) {
        ServiceInputForm(
            serviceUiState = serviceUiState,
            securityKeyListUiState = securityKeyListUiState,
            onValueChange = onServiceValueChange
        )
    }
}


@Composable
fun SecurityKeyItem(
    securityKey: SecurityKey,
    modifier: Modifier = Modifier,
    value: Boolean = false,
    onValueChange: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(bottom = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 2.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                securityKey.name,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                securityKey.type,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Light
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Checkbox(
            checked = value,
            onCheckedChange = { onValueChange() },
            modifier = Modifier.height(14.dp)
        )
    }
}

@Preview
@Composable
fun CreateServiceEditScreenPreview() {
    HwKeyInfoTheme {
        ServiceEditScreenContent(
            serviceUiState = previewServiceUiState.copy(
                isAddingNew = true,
                details = previewServiceUiState.details.copy(
                    securityKeys = listOf(1, 200)
                )),
            securityKeyListUiState = previewSecurityListUiState
        )
    }
}

@Preview
@Composable
fun EditServiceEditScreenPreview() {
    HwKeyInfoTheme {
        ServiceEditScreenContent(
            serviceUiState = previewServiceUiState.copy(
                isAddingNew = false,
                details = previewServiceUiState.details.copy(
                    securityKeys = listOf(100)
                )),
            securityKeyListUiState = previewSecurityListUiState
        )
    }
}

val previewServiceUiState = ServiceUiState(
    details = ServiceDetails(
        serviceName = "Some service",
        serviceUser = "user@service.com"
    )
)

val previewSecurityListUiState = SecurityKeyListUiState(
    listOf(
        SecurityKey(id = 1, name = "Key 1", type = "Key type 1"),
        SecurityKey(id = 100, name = "Key 2", type = "Key type 100"),
        SecurityKey(id = 200, name = "Key 3", type = "Key type 200"),
    )
)