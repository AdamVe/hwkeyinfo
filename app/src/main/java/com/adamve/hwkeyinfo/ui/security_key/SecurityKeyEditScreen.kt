package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.preview.PreviewData.Companion.keyWithServices
import com.adamve.hwkeyinfo.preview.PreviewData.Companion.services
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.service.serviceComparator
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme
import com.adamve.hwkeyinfo.ui.widgets.CustomTextField
import kotlinx.coroutines.launch

object SecurityKeyEditDestination : AppDestination {
    override val route = "security_key_edit"
    override val titleRes = R.string.app_page_title_key_edit
    const val securityKeyIdArg = "securityKeyId"
    val routeWithArgs = "$route/{$securityKeyIdArg}"
    const val addKeyRoute = "security_key_add"
    val addKeyTitleRes = R.string.app_page_title_key_add
}

@Composable
fun SecurityKeyEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityKeyEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val serviceListUiState by viewModel.serviceListUiState.collectAsState()

    SecurityKeyEditScreenContent(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        onCreate = {
            coroutineScope.launch {
                viewModel.createSecurityKey()
                navigateBack()
            }
        },
        onDelete = {
            coroutineScope.launch {
                viewModel.deleteSecurityKey()
                navigateBack()
            }
        },
        onUpdate = {
            coroutineScope.launch {
                viewModel.updateSecurityKey()
                navigateBack()
            }
        },
        onDetailsChanged = {
            viewModel.updateSecurityKeyUiState(
                details = it,
                isAddingNew = viewModel.securityKeyUiState.isAddingNew
            )
        },
        securityKeyUiState = viewModel.securityKeyUiState,
        serviceListUiState = serviceListUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityKeyEditScreenContent(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onCreate: () -> Unit = {},
    onDelete: () -> Unit = {},
    onUpdate: () -> Unit = {},
    onDetailsChanged: (SecurityKeyDetails) -> Unit = {},
    securityKeyUiState: SecurityKeyUiState = SecurityKeyUiState(),
    serviceListUiState: ServiceListUiState = ServiceListUiState(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            id = if (securityKeyUiState.isAddingNew) {
                                SecurityKeyEditDestination.addKeyTitleRes
                            } else {
                                SecurityKeyEditDestination.titleRes
                            }
                        )
                    )
                },
                actions = {
                    if (securityKeyUiState.isAddingNew) {
                        IconButton(
                            onClick = onCreate,
                            enabled = securityKeyUiState.isEntryValid
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.security_key_edit_screen_action_add)
                            )
                        }
                    } else { // edit
                        IconButton(
                            onClick = onDelete,
                            enabled = securityKeyUiState.isEntryValid
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.security_key_edit_screen_action_delete)
                            )
                        }
                        IconButton(
                            onClick = onUpdate,
                            enabled = securityKeyUiState.isEntryValid
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(R.string.security_key_edit_screen_action_update)
                            )
                        }
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

        SecurityKeyEntryBody(
            securityKeyUiState = securityKeyUiState,
            serviceListUiState = serviceListUiState,
            onSecurityKeyValueChange = onDetailsChanged,
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
    Column(modifier = modifier
        .padding(horizontal = 16.dp)) {
        SecurityKeyInputForm(
            securityKeyUiState = securityKeyUiState,
            serviceListUiState = serviceListUiState,
            onValueChange = onSecurityKeyValueChange
        )
    }
}

@Composable
fun ServiceItem(
    service: Service,
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
                service.serviceName,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                service.serviceUser,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SecurityKeyInputForm(
    securityKeyUiState: SecurityKeyUiState,
    serviceListUiState: ServiceListUiState,
    onValueChange: (SecurityKeyDetails) -> Unit,
    modifier: Modifier = Modifier
) {

    val focusRequester = FocusRequester()
    val securityKeyDetails = securityKeyUiState.details

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                CustomTextField(
                    title = stringResource(R.string.security_key_input_form_key_title),
                    value = securityKeyDetails.name,
                    onValueChange = {
                        onValueChange(securityKeyDetails.copy(name = it))
                    },
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier
                        .weight(2f)
                        .focusRequester(focusRequester),
                    showExisting = !securityKeyUiState.isAddingNew
                )
            }

            CustomTextField(
                title = stringResource(R.string.security_key_input_form_key_model_title),
                value = securityKeyDetails.type,
                onValueChange = { onValueChange(securityKeyDetails.copy(type = it)) },
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth(),
                showExisting = !securityKeyUiState.isAddingNew
            )

            CustomTextField(
                title = stringResource(R.string.security_key_input_form_key_description_title),
                value = securityKeyDetails.description,
                onValueChange = { onValueChange(securityKeyDetails.copy(description = it)) },
                keyboardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth(),
                showExisting = !securityKeyUiState.isAddingNew
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.security_key_input_form_services_header))
            FlowRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                serviceListUiState.allServices.sortedWith(serviceComparator).forEach { service ->
                    ServiceItem(
                        service = service,
                        modifier = Modifier.fillMaxWidth(),
                        value = securityKeyDetails.services.contains(service.serviceId),
                        onValueChange = {
                            onValueChange(
                                securityKeyDetails.copy(
                                    services = if (securityKeyDetails.services.contains(service.serviceId)) {
                                        securityKeyDetails.services.minus(service.serviceId)
                                    } else {
                                        securityKeyDetails.services.plus(service.serviceId)
                                    }
                                )
                            )
                        }
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            if (securityKeyUiState.isAddingNew) {
                focusRequester.requestFocus()
            }
        }
    }
}

@Preview
@Composable
fun AddSecuritySecurityKeyScreenPreview() {
    HwKeyInfoTheme {
        SecurityKeyEditScreenContent(
            securityKeyUiState = previewSecurityKeyUiState.copy(isAddingNew = true),
            serviceListUiState = ServiceListUiState(services)
        )
    }
}

@Preview
@Composable
fun EditSecuritySecurityKeyScreenPreview() {
    HwKeyInfoTheme {
        SecurityKeyEditScreenContent(
            securityKeyUiState = previewSecurityKeyUiState.copy(isAddingNew = false),
            serviceListUiState = ServiceListUiState(services)
        )
    }
}

val previewSecurityKeyUiState = SecurityKeyUiState(
    SecurityKeyDetails(
        name = keyWithServices[0].securityKey.name,
        description = keyWithServices[0].securityKey.description,
        type = keyWithServices[0].securityKey.type,
        services = keyWithServices[0].services.map { it.serviceId }
    )
)
