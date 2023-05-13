package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.Service
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
                Icon(Icons.Filled.Add, "")
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
            itemList = serviceListUiState.serviceList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ServiceListScreenBody(
    itemList: List<Service>,
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
                .padding(10.dp)
                .weight(
                    weight = 1f, fill = false
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp),
            state = listState
        ) {
            items(itemList) { service ->
                ServiceRow(service, onClick = onItemClick)
            }
        }
    }
}

@Composable
fun ServiceRow(
    service: Service,
    onClick: (Long) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(service.serviceId) }
            .background(Color.LightGray)
            .padding(8.dp),
    ) {
        Text(
            service.serviceName + " / " + service.serviceUser,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
fun ServiceListScreenPreview() {
    HwKeyInfoTheme {
        ServiceListScreenContent(
            serviceListUiState = previewServiceListUiState
        )
    }
}

val previewServiceListUiState = ServiceListUiState(
    serviceList = listOf(
        Service(serviceName = "e-mail", serviceUser = "user@email.com"),
        Service(serviceName = "PGP Keys", serviceUser = "user@private-email.com"),
        Service(serviceName = "e-mail", serviceUser = "another.account@email.com"),
        Service(serviceName = "oath account", serviceUser = "user@email.com"),
    )
)

