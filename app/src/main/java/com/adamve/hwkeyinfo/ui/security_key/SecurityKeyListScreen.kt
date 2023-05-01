package com.adamve.hwkeyinfo.ui.security_key

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

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
                    label = { Text(stringResource(R.string.bottom_navigation_keys_label)) },
                    icon = {
                        Icon(
                            Icons.Default.Lock,
                            stringResource(R.string.bottom_navigation_keys_content_description)
                        )
                    })
                NavigationBarItem(
                    selected = false,
                    onClick = navigateToServiceList,
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
        SecurityKeyListScreenBody(
            itemList = securityKeyListUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SecurityKeyListScreenBody(
    itemList: List<SecurityKeyWithServices>,
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit = {},
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp),
            state = listState
        ) {
            items(itemList) {
                SecurityKeyCard(
                    it,
                    onClick = onItemClick
                )
            }
        }
    }
}

@Preview(showSystemUi = true, widthDp = 320)
@Composable
fun SecurityKeyListPreview() {

    val items = listOf(
        SecurityKeyWithServices(
            SecurityKey(name = "Main key", type = "HW Key Type 1"),
            listOf()
        ),
        SecurityKeyWithServices(
            SecurityKey(name = "Backup key", type = "HW Key Type 2"),
            listOf()
        ),
        SecurityKeyWithServices(
            SecurityKey(name = "Work key", type = "HW Key Type 1"),
            listOf()
        )

    )

    HwKeyInfoTheme {
        SecurityKeyListScreenBody(
            itemList = items
        )
    }
}


