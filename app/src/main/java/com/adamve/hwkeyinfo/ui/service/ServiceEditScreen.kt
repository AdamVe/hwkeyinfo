package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
                                viewModel.deleteService()
                                navigateBack()
                            }
                        },
                        enabled = viewModel.serviceUiState.isEntryValid
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.service_edit_screen_action_delete)
                        )
                    }
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
                            contentDescription = stringResource(R.string.service_edit_screen_action_update)
                        )
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
            serviceUiState = viewModel.serviceUiState,
            onServiceValueChange = viewModel::updateServiceUiState,
            modifier = modifier.padding(innerPadding)
        )

    }
}

