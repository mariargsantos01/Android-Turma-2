package com.example.financasapp.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.financasapp.ui.ganhos.GanhosScreen
import com.example.financasapp.ui.gastos.GastosScreen
import com.example.financasapp.ui.home.HomeScreen
import com.example.financasapp.ui.sonhos.SonhosScreen
import com.example.financasapp.viewmodel.GanhosViewModel
import com.example.financasapp.viewmodel.GastosViewModel
import com.example.financasapp.viewmodel.HomeViewModel
import com.example.financasapp.viewmodel.SonhosViewModel

@Composable
fun FinanceApp(
    homeViewModel: HomeViewModel,
    ganhosViewModel: GanhosViewModel,
    gastosViewModel: GastosViewModel,
    sonhosViewModel: SonhosViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navigateToTopLevel: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomDestinations.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navigateToTopLevel(destination.route)
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppDestination.Home.route) {
                val state by homeViewModel.uiState.collectAsStateWithLifecycle()
                HomeScreen(
                    uiState = state,
                    onEvent = homeViewModel::onEvent,
                    onNavigateGanhos = { navigateToTopLevel(AppDestination.Ganhos.route) },
                    onNavigateGastos = { navigateToTopLevel(AppDestination.Gastos.route) },
                    onNavigateSonhos = { navigateToTopLevel(AppDestination.Sonhos.route) }
                )
            }
            composable(AppDestination.Ganhos.route) {
                val state by ganhosViewModel.uiState.collectAsStateWithLifecycle()
                GanhosScreen(uiState = state, onEvent = ganhosViewModel::onEvent)
            }
            composable(AppDestination.Gastos.route) {
                val state by gastosViewModel.uiState.collectAsStateWithLifecycle()
                GastosScreen(uiState = state, onEvent = gastosViewModel::onEvent)
            }
            composable(AppDestination.Sonhos.route) {
                val state by sonhosViewModel.uiState.collectAsStateWithLifecycle()
                SonhosScreen(uiState = state, onEvent = sonhosViewModel::onEvent)
            }
        }
    }
}


