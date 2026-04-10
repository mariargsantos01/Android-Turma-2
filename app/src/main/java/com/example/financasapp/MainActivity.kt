package com.example.financasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financasapp.data.local.FinanceDatabase
import com.example.financasapp.data.repository.FinanceRepositoryImpl
import com.example.financasapp.navigation.FinanceApp
import com.example.financasapp.ui.theme.Atividade2Theme
import com.example.financasapp.viewmodel.FinanceViewModelFactory
import com.example.financasapp.viewmodel.GanhosViewModel
import com.example.financasapp.viewmodel.GastosViewModel
import com.example.financasapp.viewmodel.HomeViewModel
import com.example.financasapp.viewmodel.SonhosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current.applicationContext
            val repository = remember {
                FinanceRepositoryImpl(FinanceDatabase.getDatabase(context).financeDao())
            }
            val factory = remember { FinanceViewModelFactory(repository) }
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            val ganhosViewModel: GanhosViewModel = viewModel(factory = factory)
            val gastosViewModel: GastosViewModel = viewModel(factory = factory)
            val sonhosViewModel: SonhosViewModel = viewModel(factory = factory)

            Atividade2Theme {
                FinanceApp(
                    homeViewModel = homeViewModel,
                    ganhosViewModel = ganhosViewModel,
                    gastosViewModel = gastosViewModel,
                    sonhosViewModel = sonhosViewModel
                )
            }
        }
    }
}
