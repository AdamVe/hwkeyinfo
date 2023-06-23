package com.adamve.hwkeyinfo.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.preview.PreviewData
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

@Composable
fun KeyIdentifier(
    identifier: String,
    modifier: Modifier = Modifier
) {
    Text(
        identifier,
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun Headline(
    securityKey: SecurityKey,
    modifier: Modifier = Modifier
) {

    if (securityKey.name.isNotBlank() && securityKey.type.isNotBlank()) {
        Column {
            KeyIdentifier(securityKey.name, modifier)
            Text(
                securityKey.type,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
            )
        }
    } else if (securityKey.name.isNotBlank()) {
        KeyIdentifier(securityKey.name)
    } else {
        KeyIdentifier(securityKey.type)
    }
}

@Composable
fun HomeSecurityKeyCard(
    securityKeyWithServices: SecurityKeyWithServices,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {

    HomeSecurityKeyCardContent(
        securityKeyWithServices = securityKeyWithServices,
        modifier = modifier,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSecurityKeyCardContent(
    securityKeyWithServices: SecurityKeyWithServices,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    val securityKey = securityKeyWithServices.securityKey
    Card(
        onClick = { onClick(securityKey.id) },
        modifier = modifier
            .size(width = 330.dp, height = 220.dp)
    ) {
        val hasServices = securityKeyWithServices.services.isNotEmpty()

        val servicesLabel: @Composable () -> Unit = {
            val label = if (hasServices) {
                stringResource(
                    id = R.string.security_key_card_header_services,
                    securityKeyWithServices.services.size
                )
            } else {
                stringResource(id = R.string.security_key_card_header_no_services)
            }

            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .padding(top = 16.dp)
            ) {
                Icon(Icons.Default.Key, "key", modifier = Modifier.size(64.dp))
                Icon(Icons.Default.MoreVert, "menu")
            }
            Headline(securityKey = securityKey)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = securityKey.description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Light
                )
            )

            servicesLabel()
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSecurityKeyCardPreview() {
    HwKeyInfoTheme {
        HomeSecurityKeyCard(
            SecurityKeyWithServices(
                securityKey = PreviewData.keyWithoutType,
                services = PreviewData.services
            )
        )
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSecurityKeyWithoutNameCardPreview() {
    HwKeyInfoTheme {
        HomeSecurityKeyCard(
            SecurityKeyWithServices(
                securityKey = PreviewData.keyWithoutName,
                services = PreviewData.services
            )
        )
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSecurityKeyWithDescriptionPreview() {
    HwKeyInfoTheme {
        HomeSecurityKeyCardContent(
            SecurityKeyWithServices(
                securityKey = PreviewData.keyWithDescription,
                services = PreviewData.services
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSecurityKeyWithoutServicesPreview() {
    HwKeyInfoTheme {
        HomeSecurityKeyCardContent(
            SecurityKeyWithServices(
                securityKey = PreviewData.keyWithDescription,
                services = listOf()
            )
        )
    }
}