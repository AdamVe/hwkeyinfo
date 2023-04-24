package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import kotlinx.coroutines.launch

object ServiceEditDestination : AppDestination {
    override val route = "service_edit"
    override val titleRes = R.string.app_page_title_service_edit
    const val serviceIdArg = "serviceId"
    val routeWithArgs = "$route/{$serviceIdArg}"
}

@Composable
fun CustomTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    leadingIcon: @Composable (() -> Unit)? = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        label = { Text(title) },
        leadingIcon = leadingIcon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ServiceEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = ServiceEditDestination.titleRes)) },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.updateService()
                                navigateBack()
                            }
                        },
                        enabled = viewModel.serviceUiState.isEntryValid
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Update"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
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
            onDeleteClick = {
                coroutineScope.launch {
                    viewModel.deleteService()
                    navigateBack()
                }

            },
            modifier = modifier.padding(innerPadding)
        )

    }
}

