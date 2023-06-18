package com.adamve.hwkeyinfo.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.Service
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

    var expanded by rememberSaveable { mutableStateOf(false) }
    HomeSecurityKeyCardContent(
        securityKeyWithServices = securityKeyWithServices,
        isExpanded = expanded,
        onExpandButtonClick = { expanded = !expanded },
        modifier = modifier,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSecurityKeyCardContent(
    securityKeyWithServices: SecurityKeyWithServices,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onExpandButtonClick: () -> Unit,
    onClick: (Long) -> Unit = {},
) {
    val securityKey = securityKeyWithServices.securityKey
    Card(
        onClick = { onClick(securityKey.id) },
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Headline(securityKey)
            if (securityKey.description.isNotBlank()) {
                Text(
                    securityKey.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                )
            }

            val hasServices = securityKeyWithServices.services.isNotEmpty()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        enabled = hasServices,
                        onClick = onExpandButtonClick
                    )
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    .padding(8.dp)
            ) {
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

                servicesLabel()
            }
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSecurityKeyCardPreview() {
    HwKeyInfoTheme {
        HomeSecurityKeyCard(
            SecurityKeyWithServices(
                securityKey = previewSecurityKey,
                services = listOf()
            )
        )
    }

}

@Preview
@Composable
fun HomeSecurityKeyWithoutNameCardPreview() {
    val key = SecurityKey(0, "", "Key type", "Key description")

    HwKeyInfoTheme {
        HomeSecurityKeyCard(SecurityKeyWithServices(securityKey = key, services = listOf()))
    }

}

@Preview
@Composable
fun HomeSecurityKeyWithoutDescriptionPreview() {
    HwKeyInfoTheme {
        HomeSecurityKeyCardContent(
            SecurityKeyWithServices(
                securityKey = previewSecurityKeyWithoutDescription,
                services = previewServices
            ),
            isExpanded = false,
            onExpandButtonClick = {})
    }
}

@Preview
@Composable
fun ExpandedHomeSecurityKeyCardPreview() {
    HwKeyInfoTheme {
        HomeSecurityKeyCardContent(
            SecurityKeyWithServices(
                securityKey = previewSecurityKey,
                services = previewServices
            ),
            isExpanded = true,
            onExpandButtonClick = {})
    }
}

val previewSecurityKeyWithoutDescription = SecurityKey(
    0,
    "Private backup Key",
    "HW Key Type",
    ""
)

val previewSecurityKey = SecurityKey(
    0,
    "Private backup Key",
    "HW Key Type",
    "This is my main backup key and it is stored in bank X ref 123."
)

val previewServices = listOf(
    Service(
        serviceName = "Email provider with long name",
        serviceUser = "service_with_long@user.com"
    ),
    Service(
        serviceName = "Email provider with long name",
        serviceUser = "service_with_long@user.com"
    ),
    Service(
        serviceName = "Email provider with long name",
        serviceUser = "service_with_long@user.com"
    ),
    Service(
        serviceName = "Email provider with long name",
        serviceUser = "service_with_long@user.com"
    ),
    Service(
        serviceName = "Email provider with long name",
        serviceUser = "service_with_long@user.com"
    ),
    Service(
        serviceName = "Email provider with long name",
        serviceUser = "service_with_long@user.com"
    )
)