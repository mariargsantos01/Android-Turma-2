package com.example.contatos.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.contatos.ui.screens.ContactFormScreen
import com.example.contatos.ui.screens.ContactListScreen
import com.example.contatos.viewmodel.ContactViewModel

@Composable
fun AppNavigation(viewModel: ContactViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.ContactList.route
    ) {
        composable(AppRoutes.ContactList.route) {
            ContactListScreen(
                viewModel = viewModel,
                onAddClick = {
                    navController.navigate(AppRoutes.ContactCreate.route)
                },
                onEditClick = { contactId ->
                    navController.navigate(AppRoutes.ContactEdit.createRoute(contactId))
                }
            )
        }

        composable(AppRoutes.ContactCreate.route) {
            ContactFormScreen(
                viewModel = viewModel,
                contactId = null,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.ContactEdit.route,
            arguments = listOf(
                navArgument("contactId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            ContactFormScreen(
                viewModel = viewModel,
                contactId = contactId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

