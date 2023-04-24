package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
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
                                viewModel.updateSecurityKey()
                                navigateBack()
                            }
                        },
                        enabled = viewModel.securityKeyUiState.isEntryValid
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

        SecurityKeyEntryBody(
            securityKeyUiState = viewModel.securityKeyUiState,
            onSecurityKeyValueChange = viewModel::updateSecurityKeyUiState,
            onDeleteClick = {
                coroutineScope.launch {
                    viewModel.deleteSecurityKey()
                    navigateBack()
                }

            },
            modifier = modifier.padding(innerPadding)
        )

    }
}

@Composable
fun SecurityKeyInputForm(
    securityKeyDetails: SecurityKeyDetails,
    modifier: Modifier = Modifier,
    onValueChange: (SecurityKeyDetails) -> Unit,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            CustomTextField(
                title = "Security Key Name",
                value = securityKeyDetails.name,
                onValueChange = { onValueChange(securityKeyDetails.copy(name = it)) },
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
            title = "Description",
            value = securityKeyDetails.description,
            onValueChange = { onValueChange(securityKeyDetails.copy(description = it)) },
            keyboardType = KeyboardType.Text
        )
    }
}

@Composable
fun SecurityKeyEntryBody(
    securityKeyUiState: SecurityKeyUiState,
    onSecurityKeyValueChange: (SecurityKeyDetails) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        SecurityKeyInputForm(
            securityKeyDetails = securityKeyUiState.details,
            onValueChange = onSecurityKeyValueChange
        )
        Row {
            onDeleteClick?.let {
                Button(
                    onClick = it,
                    enabled = securityKeyUiState.isEntryValid,
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
