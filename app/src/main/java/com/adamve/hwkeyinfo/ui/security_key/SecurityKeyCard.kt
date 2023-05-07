package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme
import com.adamve.hwkeyinfo.ui.theme.SecurityKeyBackground

@Composable
fun ServiceTag(
    service: Service
) {
    Column(
        modifier = Modifier
            .padding(bottom = 2.dp)
            .clip(RoundedCornerShape(3.dp))
            .padding(horizontal = 1.dp)
            .border(
                border = BorderStroke(0.1.dp, color = Color.Black),
                shape = RoundedCornerShape(corner = CornerSize(3.dp))
            )
            .padding(2.dp)
            .widthIn(max = 100.dp)
    ) {
        Text(
            service.serviceName,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(service.serviceUser, fontSize = 8.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun KeyIdentifier(identifier: String, modifier: Modifier = Modifier) {
    Text(identifier, modifier = modifier, fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SecurityKeyCard(
    securityKeyWithServices: SecurityKeyWithServices,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    val securityKey = securityKeyWithServices.securityKey
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .defaultMinSize(minHeight = 80.dp)
            .fillMaxWidth()
            .clickable { onClick(securityKey.id) }
            .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
            .background(color = SecurityKeyBackground)
//            .border(
//                border = BorderStroke(0.5.dp, color = Color.Black),
//                shape = RoundedCornerShape(corner = CornerSize(4.dp))
//            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            if (securityKey.name.isNotBlank() && securityKey.type.isNotBlank()) {
                KeyIdentifier(securityKey.name, Modifier.alignByBaseline())
                //Text(" / ", modifier = Modifier.alignByBaseline(), fontSize = 14.sp)
                Text(
                    " " + securityKey.type,
                    modifier = Modifier.alignByBaseline(),
                    fontSize = 12.sp
                )
            } else if (securityKey.name.isNotBlank()) {
                KeyIdentifier(securityKey.name)
            } else {
                KeyIdentifier(securityKey.type)
            }
        }

        if (securityKey.description.isNotBlank()) {
            Text(
                securityKey.description,
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
        }

        if (securityKeyWithServices.services.isNotEmpty()) {
            Text(
                "Services: ${securityKeyWithServices.services.size}",
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )

            FlowRow(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                securityKeyWithServices.services.forEach {
                    ServiceTag(it)
                }
            }
        } else {
            Text(
                "No services",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun SecurityKeyCardPreview() {
    val key = SecurityKey(
        0,
        "Key name",
        "Key nickname",
        "Key type",
        "ref",
        "Key description"
    )

    HwKeyInfoTheme {
        SecurityKeyCard(SecurityKeyWithServices(securityKey = key, services = listOf()))
    }

}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun SecurityKeyWithoutNameCardPreview() {
    val key = SecurityKey(0, "", "Key nickname", "Key type", "ref", "Key description")

    HwKeyInfoTheme {
        SecurityKeyCard(SecurityKeyWithServices(securityKey = key, services = listOf()))
    }

}

@Preview(showBackground = true, widthDp = 320, showSystemUi = true)
@Composable
fun SecurityKeyWithoutDescriptionPreview() {
    val key = SecurityKey(0, "Key name", "Key nickname", "", "ref", "")
    val services = listOf(
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

    HwKeyInfoTheme {
        SecurityKeyCard(SecurityKeyWithServices(securityKey = key, services = services))
    }

}