package com.finneo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.finneo.ui.theme.FinneoTheme

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

    NavHost(
        navController = navController,
        startDestination = "email"
    ) {
        composable("email") {
            LoginSectionEmail (
                onContinue = {
                    navController.navigate("password")
                },
                onNotHaveAccount = {
                    navController.navigate("register")
                }
            )
        }

        composable("password") {
            LoginSectionPassword (
                onContinue = {
                    // Login Sucesso -> Vai para Home e remove telas de login da pilha
                    navController.navigate("home") {
                        popUpTo("email") { inclusive = true }
                    }
                },
                onForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }

        composable("register") {
            RegisterScreen (
                onContinue = {
                    // Cadastro Sucesso -> Vai para Home
                    navController.navigate("home") {
                        popUpTo("email") { inclusive = true }
                    }
                }
            )
        }

        //Home com Toggle Switch
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("forgot_password") {
            // Você pode criar uma tela ForgotPasswordScreen depois
        }
    }
}