package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.security_key.CustomTextField
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyDetails
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyInputForm
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyUiState
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
                            contentDescription = "Back"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack /*{ navController.navigateUp() }*/) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
    onValueChange: (ServiceDetails) -> Unit,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            CustomTextField(
                title = "Service Name",
                value = serviceDetails.serviceName,
                onValueChange = { onValueChange(serviceDetails.copy(serviceName = it)) },
                keyboardType = KeyboardType.Text,
                leadingIcon = { Icon(Icons.Filled.Favorite, "") }
            )

            IconButton(
                onClick = {},
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 10.dp)),

                ) {

                Icon(Icons.Filled.Add, "")

            }
        }

        CustomTextField(
            title = "User/Account",
            value = serviceDetails.serviceUser,
            onValueChange = { onValueChange(serviceDetails.copy(serviceUser = it)) },
            keyboardType = KeyboardType.Text
        )
    }
}


@Composable
fun ServiceEntryBody(
    serviceUiState: ServiceUiState,
    onServiceValueChange: (ServiceDetails) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        ServiceInputForm(
            serviceDetails = serviceUiState.details,
            onValueChange = onServiceValueChange
        )
        Row {
            onDeleteClick?.let {
                Button(
                    onClick = it,
                    enabled = serviceUiState.isEntryValid,
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

