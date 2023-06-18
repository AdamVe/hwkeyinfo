package com.adamve.hwkeyinfo.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.adamve.hwkeyinfo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenSearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = true,
        onActiveChange = onActiveChange,
        leadingIcon = {
            Icon(
                Icons.Outlined.ArrowBack,
                stringResource(id = R.string.home_page_search_back),
                modifier = Modifier.clickable {
                    onActiveChange(false)
                })
        },
        trailingIcon = {
            Icon(
                Icons.Outlined.Close,
                stringResource(id = R.string.home_page_search_close),
                modifier = Modifier.clickable {
                    onActiveChange(false)
                })
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text(stringResource(id = R.string.home_page_search_placeholder))
        },
        content = content
    )
}