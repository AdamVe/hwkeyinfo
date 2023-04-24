package com.adamve.hwkeyinfo.ui.security_key

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
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider

object SecurityKeyListDestination : AppDestination {
    override val route = "security_keys"
    override val titleRes = R.string.app_page_title_key_list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityKeyListScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Long) -> Unit,
    navigateToServiceList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityKeyListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val securityKeyListUiState by viewModel.securityKeyListUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = SecurityKeyListDestination.titleRes)) },
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
                    selected = true,
                    onClick = { },
                    label = { Text("Keys") },
                    icon = { Icon(Icons.Default.Lock, "Keys") })
                NavigationBarItem(
                    selected = false,
                    onClick = navigateToServiceList,
                    label = { Text("Services") },
                    icon = { Icon(Icons.Default.List, "Services") })
            }

        }
    ) { innerPadding ->
        SecurityKeyListScreenBody(
            itemList = securityKeyListUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SecurityKeyListScreenBody(
    itemList: List<SecurityKey>,
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
            items(itemList) { securityKey ->
                SecurityKeyRow(securityKey, onClick = onItemClick)
            }
        }
    }
}

@Composable
fun SecurityKeyRow(
    securityKey: SecurityKey,
    onClick: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onClick(securityKey.id) },
    ) {
        Text(securityKey.name, modifier = Modifier.weight(0.2f), fontSize = 24.sp)
    }
}


