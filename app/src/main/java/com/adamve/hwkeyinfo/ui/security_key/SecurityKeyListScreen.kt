package com.adamve.hwkeyinfo.ui.security_key

import android.content.res.Configuration.UI_MODE_NIGHT_NO
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
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.ui.AppDestination
import com.adamve.hwkeyinfo.ui.AppViewModelProvider
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

object SecurityKeyListDestination : AppDestination {
    override val route = "security_keys"
    override val titleRes = R.string.app_page_title_key_list
}

val securityKeyComparator = Comparator<SecurityKey> { l, r ->
    (l.name + l.type).uppercase()
        .compareTo((r.name + r.type).uppercase())
}

val securityKeyWithServicesComparator = Comparator<SecurityKeyWithServices> { l, r ->
    (l.securityKey.name + l.securityKey.type).uppercase()
        .compareTo((r.securityKey.name + r.securityKey.type).uppercase())
}

@Composable
fun SecurityKeyListScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityKeyListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val securityKeyListUiState by viewModel.securityKeyListUiState.collectAsState()
    SecurityKeyListScreenContent(
        modifier = modifier,
        navigateToItemEntry = navigateToItemEntry,
        navigateToItemUpdate = navigateToItemUpdate,
        navigateBack = navigateBack,
        securityKeyListUiState = securityKeyListUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityKeyListScreenContent(
    modifier: Modifier = Modifier,
    navigateToItemEntry: () -> Unit = {},
    navigateToItemUpdate: (Long) -> Unit = {},
    navigateBack: () -> Unit = {},
    securityKeyListUiState: SecurityKeyListUiState = SecurityKeyListUiState(),
) {

    val itemList = securityKeyListUiState
        .itemList
        .sortedWith(securityKeyWithServicesComparator)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_page_title_key_list)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                }
            )
        },
        floatingActionButton = {
            if (itemList.isNotEmpty()) {
                SmallFloatingActionButton(
                    onClick = navigateToItemEntry,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        stringResource(id = R.string.security_key_action_add_content_description)
                    )
                }
            }
        },
    ) { innerPadding ->
        if (itemList.isEmpty()) {
            SecurityKeyEmptyListScreenBody(
                navigateToItemEntry = navigateToItemEntry,
                modifier = modifier.padding(innerPadding)
            )
        } else {
            SecurityKeyListScreenBody(
                itemList,
                onItemClick = navigateToItemUpdate,
                modifier = modifier.padding(innerPadding)
            )
        }
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
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

@Composable
fun SecurityKeyEmptyListScreenBody(
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
fun SecurityKeyListPreview() {
    HwKeyInfoTheme {
        SecurityKeyListScreenContent(
            securityKeyListUiState = previewSecurityKeyListUiState,
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EmptySecurityKeyListPreview() {
    HwKeyInfoTheme {
        SecurityKeyListScreenContent(
            securityKeyListUiState = SecurityKeyListUiState(listOf()),
        )
    }
}


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
        )

    )
)

