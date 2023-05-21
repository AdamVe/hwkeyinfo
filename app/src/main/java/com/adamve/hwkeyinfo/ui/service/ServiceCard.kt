package com.adamve.hwkeyinfo.ui.service

import android.content.res.Configuration.UI_MODE_NIGHT_NO
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamve.hwkeyinfo.R
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys
import com.adamve.hwkeyinfo.ui.security_key.securityKeyComparator
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme


@Composable
fun SecurityKeyTag(
    securityKey: SecurityKey,
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
            securityKey.name + " " + securityKey.type,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            securityKey.description,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Light
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(
    serviceWithSecurityKeys: ServiceWithSecurityKeys,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ServiceCardContent(
        serviceWithSecurityKeys = serviceWithSecurityKeys,
        isExpanded = expanded,
        onExpandButtonClick = { expanded = !expanded },
        modifier = modifier,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCardContent(
    serviceWithSecurityKeys: ServiceWithSecurityKeys,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onExpandButtonClick: () -> Unit = {},
    onClick: (Long) -> Unit = {},
) {
    val service = serviceWithSecurityKeys.service

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
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    onClick = { onClick(service.serviceId) }
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        stringResource(id = R.string.security_key_card_action_edit_content_description),
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                Column {
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
                }
            }
            SecurityKeysSection(
                securityKeys = serviceWithSecurityKeys.securityKeys,
                isExpanded = isExpanded,
                onExpandButtonClick = onExpandButtonClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SecurityKeysSection(
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

                if (hasKeys) {
                    if (isExpanded)
                        Icon(
                            Icons.Outlined.UnfoldLess,
                            stringResource(id = R.string.service_card_action_unfold_key_list_content_description)
                        )
                    else
                        Icon(
                            Icons.Outlined.UnfoldMore,
                            stringResource(id = R.string.service_card_action_fold_key_list_content_description)
                        )
                }
            }
        }

        keysLabel()

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
                securityKeys
                    .sortedWith(securityKeyComparator)
                    .forEach {
                        SecurityKeyTag(
                            it,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ServiceCardPreview() {
    HwKeyInfoTheme {
        ServiceCard(serviceWithSecurityKeys = previewServicesWithSecurityKeys[0])
    }
}