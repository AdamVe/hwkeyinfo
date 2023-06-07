package com.adamve.hwkeyinfo.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyListUiState
import com.adamve.hwkeyinfo.ui.service.HomeServiceCard
import com.adamve.hwkeyinfo.ui.service.ServiceListUiState
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

object HomeScreenDestination : AppDestination {
    override val route = "home"
    override val titleRes = R.string.app_page_title_home
}


val securityKeyWithServicesComparator = Comparator<SecurityKeyWithServices> { l, r ->
    (l.securityKey.name + l.securityKey.type).uppercase()
        .compareTo((r.securityKey.name + r.securityKey.type).uppercase())
}

@Composable
fun HomeScreen(
    navigateToSecurityKeysScreen: () -> Unit,
    navigateToServiceScreen: () -> Unit,
    navigateToSecurityKey: (Long) -> Unit,
    navigateToService: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val securityKeyListUiState by viewModel.securityKeyUiState.collectAsState()
    val serviceListUiState by viewModel.serviceListUiState.collectAsState()
    HomeScreenContent(
        modifier = modifier,
        navigateToSecurityKeysScreen = navigateToSecurityKeysScreen,
        navigateToServiceScreen = navigateToServiceScreen,
        navigateToSecurityKey = navigateToSecurityKey,
        navigateToService = navigateToService,
        securityKeyListUiState = securityKeyListUiState,
        serviceListUiState = serviceListUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    navigateToSecurityKeysScreen: () -> Unit = {},
    navigateToServiceScreen: () -> Unit = {},
    navigateToSecurityKey: (Long) -> Unit = {},
    navigateToService: (Long) -> Unit = {},
    securityKeyListUiState: SecurityKeyListUiState = SecurityKeyListUiState(),
    serviceListUiState: ServiceListUiState = ServiceListUiState(),
) {

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchActive by rememberSaveable { mutableStateOf(false) }

    val itemList = securityKeyListUiState
        .itemList
        .sortedWith(securityKeyWithServicesComparator)

    val filteredList = itemList.filter {
        searchQuery.isNotBlank() && (it.securityKey.name.contains(searchQuery, ignoreCase = true)
                || it.securityKey.type.contains(searchQuery, ignoreCase = true)
                || it.securityKey.description.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            if (!searchActive)
                TopAppBar(
                    title = { Text(stringResource(id = R.string.app_page_title_key_list)) },
//                    actions = {
//                        IconButton(onClick = { searchActive = true }) {
//                            Icon(
//                                imageVector = Icons.Filled.Search,
//                                contentDescription = "Localized description"
//                            )
//                        }
//                    }
                )

        },
        floatingActionButton = {
            if (itemList.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {}, //navigateToItemEntry,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(id = R.string.security_key_action_add_content_description)
                    )
                }
            }
        },
//        bottomBar = {
//            NavigationBar {
//                NavigationBarItem(
//                    selected = true,
//                    onClick = { },
//                    label = { Text(stringResource(R.string.bottom_navigation_keys_label)) },
//                    icon = {
//                        Icon(
//                            Icons.Default.Lock,
//                            stringResource(R.string.bottom_navigation_keys_content_description)
//                        )
//                    })
//                NavigationBarItem(
//                    selected = false,
//                    onClick = navigateToServiceList,
//                    label = { Text(stringResource(R.string.bottom_navigation_services_label)) },
//                    icon = {
//                        Icon(
//                            Icons.Default.List,
//                            stringResource(R.string.bottom_navigation_services_content_description)
//                        )
//                    })
//            }
//
//        }
    ) { innerPadding ->
        if (itemList.isEmpty()) {
            EmptyHomeScreenBody(
                navigateToItemEntry = {}, //navigateToItemEntry,
                modifier = modifier.padding(innerPadding)
            )
        } else {
            HomeScreenBody(
                securityKeyWithServicesList = if (searchQuery.isBlank()) {
                    itemList
                } else {
                    filteredList
                },
                serviceWithKeysList = serviceListUiState.serviceList,
                navigateToSecurityKeysScreen = navigateToSecurityKeysScreen,
                navigateToServiceScreen = navigateToServiceScreen,
                onKeyItemClick = navigateToSecurityKey,
                onServiceItemClick = navigateToService,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun KeyRow(
    securityKeyWithServicesList: List<SecurityKeyWithServices>,
    modifier: Modifier = Modifier,
    navigateToSecurityKeysScreen: () -> Unit = {},
    onKeyItemClick: (Long) -> Unit = {},
) {
    val listState = rememberLazyListState()
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 8.dp).fillMaxWidth()) {
        Text(
            text = "Keys",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Show all",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.clickable {
                navigateToSecurityKeysScreen()
            }
        )
    }
    LazyRow(
        Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        state = listState
    ) {
        items(securityKeyWithServicesList) {
            HomeSecurityKeyCard(
                it,
                onClick = onKeyItemClick
            )
        }
    }
}

@Composable
fun ServiceRow(
    serviceWithKeysList: List<ServiceWithSecurityKeys>,
    modifier: Modifier = Modifier,
    navigateToServiceScreen: () -> Unit = {},
    onServiceItemClick: (Long) -> Unit = {},
) {
    val listState = rememberLazyListState()
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 8.dp).fillMaxWidth()) {
        Text(
            text = "Services",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Show all",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.clickable {
                navigateToServiceScreen()
            }
        )
    }

    LazyRow(
        Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        state = listState
    ) {
        items(serviceWithKeysList) {
            HomeServiceCard(
                it,
                onClick = onServiceItemClick
            )
        }
    }
}

@Composable
fun HomeScreenBody(
    securityKeyWithServicesList: List<SecurityKeyWithServices>,
    serviceWithKeysList: List<ServiceWithSecurityKeys>,
    modifier: Modifier = Modifier,
    navigateToSecurityKeysScreen: () -> Unit,
    navigateToServiceScreen: () -> Unit,
    onKeyItemClick: (Long) -> Unit = {},
    onServiceItemClick: (Long) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        KeyRow(
            securityKeyWithServicesList = securityKeyWithServicesList,
            modifier = modifier,
            navigateToSecurityKeysScreen = navigateToSecurityKeysScreen,
            onKeyItemClick = onKeyItemClick
        )
        ServiceRow(
            serviceWithKeysList = serviceWithKeysList,
            modifier = modifier,
            navigateToServiceScreen = navigateToServiceScreen,
            onServiceItemClick = onServiceItemClick
        )
    }
}

@Composable
fun EmptyHomeScreenBody(
    modifier: Modifier = Modifier,
    navigateToItemEntry: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(id = R.string.security_key_list_screen_add_label),
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
        FilledIconButton(
            onClick = navigateToItemEntry,
            shape = RoundedCornerShape(8.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            )
        ) {
            Icon(
                Icons.Filled.Add,
                stringResource(id = R.string.security_key_action_add_content_description)
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    HwKeyInfoTheme {
        HomeScreenContent(
            securityKeyListUiState = previewSecurityKeyListUiState,
            serviceListUiState = previewServiceListUiState
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EmptyHomeScreenPreview() {
    HwKeyInfoTheme {
        HomeScreenContent(
            securityKeyListUiState = SecurityKeyListUiState(listOf())
        )
    }
}

val previewServiceListUiState = ServiceListUiState(
    serviceList = listOf(
        ServiceWithSecurityKeys(
            Service(serviceId = 0, serviceName = "Service 1", serviceUser = "user"),
            emptyList()
        ),
        ServiceWithSecurityKeys(
            Service(serviceId = 0, serviceName = "Service 1", serviceUser = "user"),
            emptyList()
        ),
        ServiceWithSecurityKeys(
            Service(serviceId = 0, serviceName = "Service 1", serviceUser = "user"),
            emptyList()
        ),
        ServiceWithSecurityKeys(
            Service(serviceId = 0, serviceName = "Service 1", serviceUser = "user"),
            emptyList()
        )
    )
)

val previewSecurityKeyListUiState = SecurityKeyListUiState(
    itemList = listOf(
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
        ),
        SecurityKeyWithServices(
            SecurityKey(name = "Main key 2", type = "HW Key Type 1"),
            listOf()
        ),
        SecurityKeyWithServices(
            SecurityKey(name = "Backup key 3", type = "HW Key Type 2"),
            listOf()
        ),
        SecurityKeyWithServices(
            SecurityKey(name = "Work key 5", type = "HW Key Type 1"),
            listOf()
        )


    )
)


// TODO
// Searchbar in AppTopBar
//else {
//    SearchBar(
//        query = searchQuery,
//        onQueryChange = { searchQuery = it },
//        onSearch = {
//            searchQuery = it
//            searchActive = false
//        },
//        active = searchActive,
//        onActiveChange = { searchActive = it },
//        leadingIcon = {
//            if (searchActive) {
//                Icon(Icons.Outlined.ArrowBack, "",
//                    modifier = Modifier.clickable {
//                        searchActive = false
//                        searchQuery = ""
//                    })
//            } else {
//                Icon(Icons.Outlined.Search, "")
//            }
//        },
//        trailingIcon = {
//            if (searchQuery.isNotBlank()) {
//                Icon(
//                    Icons.Outlined.Close,
//                    "",
//                    modifier = Modifier.clickable {
//                        searchActive = false
//                        searchQuery = ""
//                    })
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(
//                vertical = 8.dp,
//                horizontal = if (searchActive) {
//                    0.dp
//                } else {
//                    16.dp
//                }
//            ),
//        placeholder = {
//            Text("Search for key")
//        }
//    ) {
//        LazyColumn {
//            items(filteredList) { securityKeyWithServices ->
//                SecurityKeySearchResultCard(
//                    securityKeyWithServices,
//                    onClick = {
//                        searchActive = false
//                        navigateToItemUpdate(securityKeyWithServices.securityKey.id)
//                    }
//                )
//            }
//        }
//    }
//}