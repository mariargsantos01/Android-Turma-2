package com.example.contatos.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contatos.model.Contact

@Composable
fun ContactCard(
    contact: Contact,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModernContactCard(
        contact = contact,
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick,
        modifier = modifier
    )
}

