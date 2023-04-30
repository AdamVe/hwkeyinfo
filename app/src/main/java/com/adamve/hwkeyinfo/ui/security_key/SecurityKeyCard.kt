package com.adamve.hwkeyinfo.ui.security_key

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamve.hwkeyinfo.data.SecurityKey
import com.adamve.hwkeyinfo.data.SecurityKeyWithServices
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

@Composable
fun SecurityKeyCard(
    securityKeyWithServices: SecurityKeyWithServices,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit = {},
) {
    val securityKey = securityKeyWithServices.securityKey
    Column(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(securityKey.id) }
            .background(Color.LightGray)
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            if (securityKey.name.isNotBlank() && securityKey.type.isNotBlank()) {
                Text(securityKey.name, modifier = Modifier.alignByBaseline(), fontSize = 18.sp)
                Text(" / ", modifier = Modifier.alignByBaseline(), fontSize = 14.sp)
                Text(securityKey.type, modifier = Modifier.alignByBaseline(), fontSize = 14.sp)
            } else if (securityKey.name.isNotBlank()) {
                Text(securityKey.name, fontSize = 18.sp)
            } else {
                Text(securityKey.type, fontSize = 18.sp)
            }
        }

        if (securityKey.description.isNotBlank()) {
            Text(
                securityKey.description,
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
        }

        Text(
            "Services:",
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
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

@Preview(showBackground = true, widthDp = 320)
@Composable
fun SecurityKeyWithoutDescriptionPreview() {
    val key = SecurityKey(0, "Key name", "Key nickname", "", "ref", "")

    HwKeyInfoTheme {
        SecurityKeyCard(SecurityKeyWithServices(securityKey = key, services = listOf()))
    }

}