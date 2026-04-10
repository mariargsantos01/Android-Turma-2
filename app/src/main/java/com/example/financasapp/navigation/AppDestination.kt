package com.example.financasapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : AppDestination("home", "Home", Icons.Filled.Home)
    data object Ganhos : AppDestination("ganhos", "Ganhos", Icons.Filled.AccountBalanceWallet)
    data object Gastos : AppDestination("gastos", "Gastos", Icons.AutoMirrored.Filled.TrendingDown)
    data object Sonhos : AppDestination("sonhos", "Sonhos", Icons.Filled.Savings)
}

val bottomDestinations = listOf(
    AppDestination.Home,
    AppDestination.Ganhos,
    AppDestination.Gastos,
    AppDestination.Sonhos
)

