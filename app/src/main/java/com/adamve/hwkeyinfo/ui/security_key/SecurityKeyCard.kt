package com.adamve.hwkeyinfo.ui.security_key

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
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
                fontWeight = FontWeight.ExtraLight
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun KeyIdentifier(identifier: String, modifier: Modifier = Modifier) {
    Text(
        identifier,
        modifier = modifier,
        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        fontWeight = MaterialTheme.typography.headlineLarge.fontWeight
    )
}

@Composable
fun Headline(securityKey: SecurityKey, modifier: Modifier = Modifier) {
    if (securityKey.name.isNotBlank() && securityKey.type.isNotBlank()) {
        Column {
            KeyIdentifier(securityKey.name, modifier)
            Text(
                securityKey.type,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        onClick = { onClick(securityKey.id) },
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    .padding(8.dp)
            ) {

                val hasServices = securityKeyWithServices.services.isNotEmpty()

                val servicesLabel: @Composable () -> Unit = {
                    val label = if (hasServices) {
                        "Services: ${securityKeyWithServices.services.size}"
                    } else {
                        "No services"
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

                        if (hasServices) {
                            IconButton(
                                onClick = onExpandButtonClick,
                                modifier = Modifier.height(18.dp)
                            ) {
                                Icon(
                                    painterResource(
                                        if (isExpanded)
                                            R.drawable.baseline_unfold_less_24
                                        else
                                            R.drawable.baseline_unfold_more_24
                                    ), ""
                                )
                            }
                        }
                    }
                }

                servicesLabel()

                val density = LocalDensity.current
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = slideInVertically {
                        // Slide in from 40 dp from the top.
                        with(density) { -40.dp.roundToPx() }
                    } + expandVertically(
                        // Expand from the top.
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()

                ) {
                    FlowRow(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        securityKeyWithServices
                            .services
                            .sortedWith(serviceComparator)
                            .forEach {
                                ServiceTag(
                                    it,
                                    modifier = Modifier.fillMaxWidth(fraction = 0.49f)
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
    val key = SecurityKey(0, "", "Key nickname", "Key type", "ref", "Key description")

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
    "HW Key Nick",
    "HW Key Type",
    "ref",
    ""
)

val previewSecurityKey = SecurityKey(
    0,
    "Private backup Key",
    "HW Key Nick",
    "HW Key Type",
    "ref",
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