package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider

object ServiceListDestination : AppDestination {
    override val route = "services"
    override val titleRes = R.string.app_page_title_service_list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Long) -> Unit,
    navigateToSecurityKeyList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ServiceListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val serviceListUiState by viewModel.serviceListUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = ServiceListDestination.titleRes)) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(Icons.Filled.Add, "")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = navigateToSecurityKeyList,
                    label = { Text("Keys") },
                    icon = { Icon(Icons.Default.Lock, "Keys") })
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    label = { Text("Services") },
                    icon = { Icon(Icons.Default.List, "Services") })
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
                .padding(0.dp)
                .weight(
                    weight =
                    1f, fill = false
                ),
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
    onClick: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onClick(service.serviceId) },
    ) {
        Text(service.serviceName, modifier = Modifier.weight(0.2f), fontSize = 24.sp)
    }
}


