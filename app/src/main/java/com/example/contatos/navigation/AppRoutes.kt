package com.example.contatos.navigation

sealed class AppRoutes(val route: String) {
    data object ContactList : AppRoutes("contact_list")
    data object ContactCreate : AppRoutes("contact_create")
    data object ContactEdit : AppRoutes("contact_edit/{contactId}") {
        fun createRoute(contactId: String) = "contact_edit/$contactId"
    }
}

