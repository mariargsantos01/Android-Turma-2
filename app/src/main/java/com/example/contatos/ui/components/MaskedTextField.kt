package com.example.contatos.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.contatos.utils.DigitMaskFormatter
import com.example.contatos.utils.MaskVisualTransformation

@Composable
fun MaskedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Number,
    mask: DigitMaskFormatter,
    leadingIcon: ImageVector? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false
) {
    val externalRaw = mask.digitsOnly(value).take(mask.maxLength)
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = externalRaw,
                selection = TextRange(externalRaw.length)
            )
        )
    }

    LaunchedEffect(externalRaw) {
        if (fieldValue.text != externalRaw) {
            fieldValue = TextFieldValue(
                text = externalRaw,
                selection = TextRange(externalRaw.length)
            )
        }
    }

    OutlinedTextField(
        value = fieldValue,
        onValueChange = { updatedValue ->
            val sanitized = mask.digitsOnly(updatedValue.text).take(mask.maxLength)
            val newCursor = updatedValue.selection.end.coerceIn(0, sanitized.length)

            fieldValue = TextFieldValue(
                text = sanitized,
                selection = TextRange(newCursor)
            )

            val formatted = mask.format(sanitized)
            if (formatted != value) {
                onValueChange(formatted)
            }
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        readOnly = readOnly,
        singleLine = true,
        enabled = enabled,
        isError = isError,
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        placeholder = placeholder?.let { text -> { Text(text) } },
        supportingText = {
            ErrorText(message = errorMessage)
        },
        visualTransformation = MaskVisualTransformation(mask),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorLeadingIconColor = MaterialTheme.colorScheme.error
        )
    )
}

