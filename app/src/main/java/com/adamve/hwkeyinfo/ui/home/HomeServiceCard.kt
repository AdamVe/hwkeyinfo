package com.adamve.hwkeyinfo.ui.service

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

@Composable
fun HomeServiceCard(
    serviceWithSecurityKeys: ServiceWithSecurityKeys,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    HomeServiceCardContent(
        serviceWithSecurityKeys = serviceWithSecurityKeys,
        isExpanded = expanded,
        onExpandButtonClick = { expanded = !expanded },
        modifier = modifier,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeServiceCardContent(
    serviceWithSecurityKeys: ServiceWithSecurityKeys,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onExpandButtonClick: () -> Unit = {},
    onClick: (Long) -> Unit = {},
) {
    val service = serviceWithSecurityKeys.service

    Card(
        onClick = { onClick(service.serviceId) },
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Language,
                            "",
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .height(with(LocalDensity.current) {
                                    MaterialTheme.typography.titleLarge.fontSize.toDp()
                                })
                        )
                        Text(
                            service.serviceName,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Text(
                        service.serviceUser,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                    )
                }

            }
            HomeSecurityKeysSection(
                securityKeys = serviceWithSecurityKeys.securityKeys,
                isExpanded = isExpanded,
                onExpandButtonClick = onExpandButtonClick
            )
        }
    }
}

@Composable
fun HomeSecurityKeysSection(
    securityKeys: List<SecurityKey> = listOf(),
    isExpanded: Boolean = false,
    onExpandButtonClick: () -> Unit = {}
) {
    val hasKeys = securityKeys.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                enabled = hasKeys,
                onClick = onExpandButtonClick
            )
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
            .padding(8.dp)
    ) {

        val keysLabel: @Composable () -> Unit = {
            val label = if (hasKeys) {
                stringResource(id = R.string.service_card_header_keys, securityKeys.size)
            } else {
                stringResource(id = R.string.service_card_header_no_keys)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }
        }

        keysLabel()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeServiceCardPreview() {
    HwKeyInfoTheme {
        HomeServiceCard(serviceWithSecurityKeys = previewServicesWithSecurityKeys[0])
    }
}