package com.adamve.hwkeyinfo.ui.service

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(
    serviceWithSecurityKeys: ServiceWithSecurityKeys,
    modifier: Modifier = Modifier,
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
}