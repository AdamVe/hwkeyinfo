package com.adamve.hwkeyinfo.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.preview.PreviewData.Companion.keyWithServicesUiState
import com.adamve.hwkeyinfo.preview.PreviewData.Companion.servicesWithKeysUiState
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeyListUiState
import com.adamve.hwkeyinfo.ui.security_key.SecurityKeySearchResultCard
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
    navigateToSecurityKeyEntry: () -> Unit,
    navigateToService: (Long) -> Unit,
    navigateToServiceEntry: () -> Unit,
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
        navigateToSecurityKeyEntry = navigateToSecurityKeyEntry,
        navigateToService = navigateToService,
        navigateToServiceEntry = navigateToServiceEntry,
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
    navigateToSecurityKeyEntry: () -> Unit = {},
    navigateToService: (Long) -> Unit = {},
    navigateToServiceEntry: () -> Unit = {},
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
                    actions = {
                        IconButton(onClick = { searchActive = true }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            else {
                HomeScreenSearchBar(
                    searchQuery = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        searchQuery = it
                        searchActive = false
                    },
                    onActiveChange = {
                        searchActive = it
                        if (!searchActive) {
                            searchQuery = ""
                        }
                    }
                ) {
                    LazyColumn {
                        items(filteredList) { securityKeyWithServices ->
                            SecurityKeySearchResultCard(
                                securityKeyWithServices,
                                onClick = {
                                    searchActive = false
                                    navigateToSecurityKey(securityKeyWithServices.securityKey.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
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
            navigateToSecurityKeyEntry = navigateToSecurityKeyEntry,
            onServiceItemClick = navigateToService,
            navigateToServiceEntry = navigateToServiceEntry,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun KeyRow(
    securityKeyWithServicesList: List<SecurityKeyWithServices>,
    modifier: Modifier = Modifier,
    navigateToSecurityKeysScreen: () -> Unit = {},
    onKeyItemClick: (Long) -> Unit = {},
    navigateToSecurityKeyEntry: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.home_page_keys_row_title),
                    style = MaterialTheme.typography.titleLarge
                )
                if (securityKeyWithServicesList.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Filled.Add,
                        stringResource(id = R.string.security_key_action_add_content_description),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .clickable { navigateToSecurityKeyEntry() }
                    )
                }
            }
            Icon(
                Icons.Outlined.ArrowForward,
                "",
                modifier = Modifier.clickable { navigateToSecurityKeysScreen() }
            )
        }

        if (securityKeyWithServicesList.isEmpty()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                FloatingActionButton(
                    onClick = navigateToSecurityKeyEntry,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(id = R.string.security_key_action_add_content_description)
                    )
                }
            }
        } else {
            LazyRow(
                Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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
    }
}

@Composable
fun ServiceRow(
    serviceWithKeysList: List<ServiceWithSecurityKeys>,
    modifier: Modifier = Modifier,
    navigateToServiceScreen: () -> Unit = {},
    onServiceItemClick: (Long) -> Unit = {},
    navigateToServiceEntry: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.home_page_services_row_title),
                    style = MaterialTheme.typography.titleLarge
                )
                if (serviceWithKeysList.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Filled.Add,
                        stringResource(id = R.string.security_key_action_add_content_description),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .clickable { navigateToServiceEntry() }
                    )
                }
            }

            Icon(
                Icons.Outlined.ArrowForward,
                "",
                modifier = Modifier.clickable { navigateToServiceScreen() }
            )
        }

        if (serviceWithKeysList.isEmpty()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                FloatingActionButton(
                    onClick = navigateToServiceEntry,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(id = R.string.security_key_action_add_content_description)
                    )
                }
            }
        } else {
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
    navigateToSecurityKeyEntry: () -> Unit = {},
    onServiceItemClick: (Long) -> Unit = {},
    navigateToServiceEntry: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        KeyRow(
            securityKeyWithServicesList = securityKeyWithServicesList,
            navigateToSecurityKeysScreen = navigateToSecurityKeysScreen,
            onKeyItemClick = onKeyItemClick,
            navigateToSecurityKeyEntry = navigateToSecurityKeyEntry
        )
        ServiceRow(
            serviceWithKeysList = serviceWithKeysList,
            navigateToServiceScreen = navigateToServiceScreen,
            onServiceItemClick = onServiceItemClick,
            navigateToServiceEntry = navigateToServiceEntry
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    HwKeyInfoTheme {
        HomeScreenContent(
            securityKeyListUiState = keyWithServicesUiState,
            serviceListUiState = servicesWithKeysUiState
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
