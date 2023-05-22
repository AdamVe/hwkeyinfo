package com.adamve.hwkeyinfo.ui.widgets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CustomTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    showExisting: Boolean = false
) {
    OutlinedTextField(
        value = if (value.isBlank() && showExisting) " " else value,
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        label = { Text(title) },
        leadingIcon = leadingIcon,
        modifier = modifier
    )
}