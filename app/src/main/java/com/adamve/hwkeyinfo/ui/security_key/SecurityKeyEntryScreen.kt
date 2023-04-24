package com.adamve.hwkeyinfo.ui.security_key

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import kotlinx.coroutines.launch

object SecurityKeyEntryDestination : AppDestination {
    override val route = "security_key_add"
    override val titleRes = R.string.app_page_title_key_entry
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityKeyEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityKeyEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = SecurityKeyEntryDestination.titleRes)) },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.saveSecurityKey()
                                navigateBack()
                            }
                        },
                        enabled = viewModel.securityKeyUiState.isEntryValid
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

        SecurityKeyEntryBody(
            securityKeyUiState = viewModel.securityKeyUiState,
            onSecurityKeyValueChange = viewModel::updateSecurityKeyUiState,
            modifier = modifier.padding(innerPadding)
        )

    }
}

