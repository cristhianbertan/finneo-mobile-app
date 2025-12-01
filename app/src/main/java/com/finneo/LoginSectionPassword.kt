package com.finneo

import AlataFont
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Importante
import com.finneo.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSectionPassword(
    viewModel: LoginViewModel = viewModel(), // Usa o mesmo ViewModel da tela anterior (se escopado corretamente no NavHost)
    onLoginSuccess: () -> Unit = {}, // Vai para a Home
    onForgotPassword: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val email by viewModel.email
    val isLoading by viewModel.isLoading
    val loginError by viewModel.loginError

    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    // Limpa erros ao entrar na tela
    LaunchedEffect(Unit) {
        viewModel.clearError()
    }

    // Máscara simples para o email (ex: cr****@gmail.com)
    val maskedEmail = remember(email) {
        if (email.contains("@")) {
            val parts = email.split("@")
            if (parts[0].length > 2) {
                "${parts[0].take(2)}****@${parts[1]}"
            } else email
        } else email
    }

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = 40.dp).padding(horizontal = 16.dp),
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(
                            modifier = Modifier.size(30.dp).background(Color(0xFF025B2F), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.logo_finneo), contentDescription = null, modifier = Modifier.size(120.dp))
            Spacer(modifier = Modifier.height(40.dp))

            Text("Insira a sua senha", style = MaterialTheme.typography.headlineSmall.copy(fontFamily = AlataFont, fontWeight = FontWeight.Bold, fontSize = 24.sp), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            // Mostra o email mascarado (vindo do ViewModel)
            Text(text = maskedEmail, style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp), modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(32.dp))

            Text("Senha", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth())

            PasswordField(
                value = password,
                onValueChange = {
                    password = it
                    isError = false
                },
                modifier = Modifier.fillMaxWidth(),
                AlataFont = AlataFont,
                isError = isError || loginError != null // Mostra erro se local ou do firebase
            )

            // Mensagem de erro do Firebase ou Validação Local
            if (isError || loginError != null) {
                Text(
                    text = loginError ?: "Por favor, insira sua senha",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = AlataFont,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    if (password.isEmpty()) {
                        isError = true
                    } else {
                        // CHAMA O LOGIN NO VIEWMODEL
                        viewModel.loginWithEmailPassword(password, onSuccess = onLoginSuccess)
                    }
                },
                enabled = !isLoading, // Desabilita botão se estiver carregando
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF025B2F), contentColor = Color(0xFFFFFFFF))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Continuar", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onForgotPassword, modifier = Modifier.align(Alignment.Start)) {
                Text("Esqueceu a senha?", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface))
            }
        }
    }
}