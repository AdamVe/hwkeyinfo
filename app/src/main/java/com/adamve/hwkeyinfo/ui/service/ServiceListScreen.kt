package com.adamve.hwkeyinfo.ui.service

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

object ServiceListDestination : AppDestination {
    override val route = "services"
    override val titleRes = R.string.app_page_title_service_list
}

@Composable
fun ServiceListScreen(
    modifier: Modifier = Modifier,
    navigateToItemEntry: () -> Unit = {},
    navigateToItemUpdate: (Long) -> Unit = {},
    navigateToSecurityKeyList: () -> Unit = {},
    viewModel: ServiceListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val serviceListUiState by viewModel.serviceListUiState.collectAsState()
    ServiceListScreenContent(
        modifier = modifier,
        navigateToItemEntry = navigateToItemEntry,
        navigateToItemUpdate = navigateToItemUpdate,
        navigateToSecurityKeyList = navigateToSecurityKeyList,
        serviceListUiState = serviceListUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreenContent(
    modifier: Modifier = Modifier,
    navigateToItemEntry: () -> Unit = {},
    navigateToItemUpdate: (Long) -> Unit = {},
    navigateToSecurityKeyList: () -> Unit = {},
    serviceListUiState: ServiceListUiState = ServiceListUiState()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = ServiceListDestination.titleRes)) },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToItemEntry,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.service_action_add_content_description))
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = navigateToSecurityKeyList,
                    label = { Text(stringResource(R.string.bottom_navigation_keys_label)) },
                    icon = {
                        Icon(
                            Icons.Default.Lock,
                            stringResource(R.string.bottom_navigation_keys_content_description)
                        )
                    })
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    label = { Text(stringResource(R.string.bottom_navigation_services_label)) },
                    icon = {
                        Icon(
                            Icons.Default.List,
                            stringResource(R.string.bottom_navigation_services_content_description)
                        )
                    })
            }

        }
    ) { innerPadding ->
        ServiceListScreenBody(
            itemList = serviceListUiState
                .serviceList
                .sortedWith(serviceWithSecurityKeysComparator),
            onItemClick = navigateToItemUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ServiceListScreenBody(
    itemList: List<ServiceWithSecurityKeys>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(
                    weight = 1f, fill = false
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp),
            state = listState
        ) {
            items(itemList) { service ->
                ServiceCard(service, onClick = onItemClick)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ServiceListScreenPreview() {
    HwKeyInfoTheme {
        ServiceListScreenContent(
            serviceListUiState = previewServiceListUiState
        )
    }
}

val previewSecurityKeys = listOf(
    SecurityKey(id = 1, name = "Key1"),
    SecurityKey(id = 2, name = "Key2"),
    SecurityKey(id = 3, name = "Key3"),
)

val previewServicesWithSecurityKeys = listOf(
    ServiceWithSecurityKeys(
        Service(serviceName = "e-mail", serviceUser = "user@email.com"),
        listOf(previewSecurityKeys[0], previewSecurityKeys[1])
    ),
    ServiceWithSecurityKeys(
        Service(serviceName = "PGP Keys", serviceUser = "user@private-email.com"),
        listOf(previewSecurityKeys[0], previewSecurityKeys[1])
    ),
    ServiceWithSecurityKeys(
        Service(serviceName = "e-mail", serviceUser = "another.account@email.com"),
        listOf(previewSecurityKeys[0], previewSecurityKeys[1])
    ),
    ServiceWithSecurityKeys(
        Service(serviceName = "oath account", serviceUser = "user@email.com"),
        listOf(previewSecurityKeys[0], previewSecurityKeys[1])
    )
)

val previewServiceListUiState = ServiceListUiState(
    previewServicesWithSecurityKeys
)

