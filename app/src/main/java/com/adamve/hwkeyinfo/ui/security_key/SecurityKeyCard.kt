package com.adamve.hwkeyinfo.ui.security_key

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.UnfoldLess
import androidx.compose.material.icons.outlined.UnfoldMore
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.ui.service.serviceComparator
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

@Composable
fun ServiceTag(
    service: Service,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 2.dp)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp))
            .padding(4.dp)
    ) {
        Text(
            service.serviceName,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            service.serviceUser,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Light
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun KeyIdentifier(
    identifier: String,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth()) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            onClick = onAction
        ) {
            Icon(
                Icons.Outlined.Edit,
                stringResource(id = R.string.security_key_card_action_edit_content_description),
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Text(
            identifier,
            modifier = modifier,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun Headline(
    securityKey: SecurityKey,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = {}
) {

    if (securityKey.name.isNotBlank() && securityKey.type.isNotBlank()) {
        Column {
            KeyIdentifier(securityKey.name, modifier, onAction = onAction)
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
        KeyIdentifier(securityKey.name, onAction = onAction)
    } else {
        KeyIdentifier(securityKey.type, onAction = onAction)
    }
}

@Composable
fun SecurityKeyCard(
    securityKeyWithServices: SecurityKeyWithServices,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    SecurityKeyCardContent(
        securityKeyWithServices = securityKeyWithServices,
        isExpanded = expanded,
        onExpandButtonClick = { expanded = !expanded },
        modifier = modifier,
        onClick = onClick
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SecurityKeyCardContent(
    securityKeyWithServices: SecurityKeyWithServices,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onExpandButtonClick: () -> Unit,
    onClick: (Long) -> Unit = {},
) {
    val securityKey = securityKeyWithServices.securityKey
    Card(
        onClick = { },
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Headline(securityKey, onAction = { onClick(securityKey.id) })
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )

                        if (hasServices) {
                            if (isExpanded)
                                Icon(
                                    Icons.Outlined.UnfoldLess,
                                    stringResource(id = R.string.security_key_card_action_fold_service_list_content_description)
                                )
                            else
                                Icon(
                                    Icons.Outlined.UnfoldMore,
                                    stringResource(id = R.string.security_key_card_action_unfold_service_list_content_description)
                                )
                        }
                    }
                }

                servicesLabel()

                val density = LocalDensity.current
                AnimatedVisibility(
                    visible = isExpanded,
                    enter =
                    slideInVertically {
                        // Slide in from 20 dp from the top.
                        with(density) { -20.dp.roundToPx() }
                    } + expandVertically(
                        // Expand from the top.
                        expandFrom = Alignment.Top,
                    ) + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f,
                    ),
                    exit = shrinkVertically() + fadeOut()

                ) {
                    FlowRow(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        securityKeyWithServices
                            .services
                            .sortedWith(serviceComparator)
                            .forEach {
                                ServiceTag(
                                    it,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SecurityKeyCardPreview() {
    HwKeyInfoTheme {
        SecurityKeyCard(
            SecurityKeyWithServices(
                securityKey = previewSecurityKey,
                services = listOf()
            )
        )
    }

}

@Preview
@Composable
fun SecurityKeyWithoutNameCardPreview() {
    val key = SecurityKey(0, "", "Key type", "Key description")

    HwKeyInfoTheme {
        SecurityKeyCard(SecurityKeyWithServices(securityKey = key, services = listOf()))
    }

}

@Preview
@Composable
fun SecurityKeyWithoutDescriptionPreview() {
    HwKeyInfoTheme {
        SecurityKeyCardContent(
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
fun ExpandedSecurityKeyCardPreview() {
    HwKeyInfoTheme {
        SecurityKeyCardContent(
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