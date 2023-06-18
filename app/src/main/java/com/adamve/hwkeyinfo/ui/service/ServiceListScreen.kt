package com.adamve.hwkeyinfo.ui.service

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.preview.PreviewData.Companion.servicesWithKeysUiState
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
    navigateBack: () -> Unit,
    viewModel: ServiceListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val serviceListUiState by viewModel.serviceListUiState.collectAsState()
    ServiceListScreenContent(
        modifier = modifier,
        navigateToItemEntry = navigateToItemEntry,
        navigateToItemUpdate = navigateToItemUpdate,
        navigateBack = navigateBack,
        serviceListUiState = serviceListUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreenContent(
    modifier: Modifier = Modifier,
    navigateToItemEntry: () -> Unit = {},
    navigateToItemUpdate: (Long) -> Unit = {},
    navigateBack: () -> Unit = {},
    serviceListUiState: ServiceListUiState = ServiceListUiState()
) {
    val itemList = serviceListUiState
        .serviceList
        .sortedWith(serviceWithSecurityKeysComparator)

    Scaffold(
        topBar = {
            if (itemList.isNotEmpty()) {
                TopAppBar(
                    title = { Text(stringResource(id = ServiceListDestination.titleRes)) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(Icons.Filled.ArrowBack, "backIcon")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (itemList.isNotEmpty()) {
                SmallFloatingActionButton(
                    onClick = navigateToItemEntry,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(id = R.string.service_action_add_content_description)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (itemList.isEmpty()) {
            ServiceEmptyListScreenBody(
                modifier = modifier.padding(innerPadding),
                navigateToItemEntry = navigateToItemEntry,
            )
        } else {
            ServiceListScreenBody(
                itemList = itemList,
                onItemClick = navigateToItemUpdate,
                modifier = modifier.padding(innerPadding)
            )
        }
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

@Composable
fun ServiceEmptyListScreenBody(
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
            stringResource(id = R.string.service_list_screen_add_label),
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
                stringResource(id = R.string.service_action_add_content_description)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ServiceListScreenPreview() {
    HwKeyInfoTheme {
        ServiceListScreenContent(
            serviceListUiState = servicesWithKeysUiState,
        )
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ServiceEmptyListScreenPreview() {
    HwKeyInfoTheme {
        ServiceListScreenContent(
            serviceListUiState = ServiceListUiState(listOf()),
        )
    }
}

