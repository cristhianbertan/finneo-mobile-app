package com.finneo

import ShareGenerationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.finneo.ui.theme.FinneoTheme
import com.finneo.viewmodel.LoginViewModel
import com.finneo.viewmodel.WalletViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FinneoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNav()
                }
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()

    val walletViewModel: WalletViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    val fiduciaryAssets = remember {
        listOf(
            Asset("MXRF11", "0,42%", "Rend. último ano", "12,05%", "Setor", "Papel", "Preço BRL", "R$ 9,49", "Valor em carteira", "474,50", "Quant. de cotas", "50", Color(0xFF43A047)),
            Asset("CPTS11", "0,41%", "Rend. último ano", "12,95%", "Setor", "Papel", "Preço BRL", "R$ 7,30", "Valor em carteira", "292,00", "Quant. de cotas", "40", Color(0xFF43A047))
        )
    }

    NavHost(
        navController = navController,
        startDestination = "email"
    ) {

        composable("email") {
            LoginSectionEmail (
                viewModel = loginViewModel,
                onContinue = { navController.navigate("password") },
                onLoginGoogleSuccess = {
                    navController.navigate("home?startTab=0") { popUpTo("email") { inclusive = true } }
                },
                onNotHaveAccount = { navController.navigate("register") }
            )
        }

        composable("password") {
            LoginSectionPassword (
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("home?startTab=0") { popUpTo("email") { inclusive = true } }
                },
                onBack = { navController.popBackStack() },
                onForgotPassword = { navController.navigate("forgot_password") }
            )
        }

        composable("register") {
            RegisterScreen (
                onContinue = {
                    navController.navigate("home?startTab=0") { popUpTo("email") { inclusive = true } }
                }
            )
        }

        composable(
            route = "home?startTab={startTab}",
            arguments = listOf(navArgument("startTab") {
                defaultValue = 0 // Padrão: Fiduciário
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val startTab = backStackEntry.arguments?.getInt("startTab") ?: 0
            HomeScreen(
                navController = navController,
                viewModel = walletViewModel,
                startTab = startTab
            )
        }

        composable("add_wallet") {
            AddWalletScreen(
                navController = navController,
                onAddWalletClick = { address ->
                    walletViewModel.saveWalletToFirebase(address) {
                        navController.navigate("home?startTab=1") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("share_screen") {
            ShareScreen(
                navController = navController,
                onShareProceed = { selectedOption ->
                    val jsonToShare = walletViewModel.generateShareableData(selectedOption, fiduciaryAssets)

                    val encodedJson = java.net.URLEncoder.encode(jsonToShare, "UTF-8")
                    navController.navigate("share_generation/$encodedJson")
                }
            )
        }

        composable("share_generation/{shareDataJson}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("shareDataJson") ?: ""

            val decodedJson = java.net.URLDecoder.decode(json, "UTF-8")

            ShareGenerationScreen(
                navController = navController,
                shareDataJson = decodedJson
            )
        }

        composable("forgot_password") {
        }
    }
}