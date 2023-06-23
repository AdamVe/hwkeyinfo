package com.adamve.hwkeyinfo.ui.service

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.preview.PreviewData.Companion.servicesWithKeysUiState
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

@Composable
fun HomeServiceCard(
    serviceWithSecurityKeys: ServiceWithSecurityKeys,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    HomeServiceCardContent(
        serviceWithSecurityKeys = serviceWithSecurityKeys,
        modifier = modifier,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeServiceCardContent(
    serviceWithSecurityKeys: ServiceWithSecurityKeys,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    val service = serviceWithSecurityKeys.service

    Card(
        onClick = { onClick(service.serviceId) },
        modifier = modifier
            .size(width = 260.dp, height = 150.dp)
    ) {
        val securityKeys = serviceWithSecurityKeys.securityKeys
        val hasKeys = securityKeys.isNotEmpty()

        val keysLabel = if (hasKeys) {
            stringResource(id = R.string.service_card_header_keys, securityKeys.size)
        } else {
            stringResource(id = R.string.service_card_header_no_keys)
        }

        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 16.dp)
            ) {
                Icon(Icons.Outlined.Language, "Service", modifier = Modifier.size(32.dp))
                Icon(Icons.Default.MoreVert, "menu")
            }

            Text(
                service.serviceName,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                service.serviceUser,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            )

            Text(
                keysLabel,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeServiceCardPreview() {
    HwKeyInfoTheme {
        HomeServiceCard(serviceWithSecurityKeys = servicesWithKeysUiState.serviceList[0])
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeService1CardPreview() {
    HwKeyInfoTheme {
        HomeServiceCard(serviceWithSecurityKeys = servicesWithKeysUiState.serviceList[2])
    }
}