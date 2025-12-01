package com.finneo

import AlataFont
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finneo.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSectionEmail(
    viewModel: LoginViewModel = viewModel(), // Injeta o ViewModel
    onContinue: () -> Unit = {}, // Navega para a tela de Senha
    onNotHaveAccount: () -> Unit = {},
    onLoginGoogleSuccess: () -> Unit = {} // Navega para a Home
) {
    val email by viewModel.email // Observa o email do ViewModel
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // --- GOOGLE SIGN IN ---
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // Certifique-se que este ID está no strings.xml
            .requestEmail()
            .build()
    }
    val googleClient: GoogleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(Exception::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        Log.d("LOGIN", "Login Google OK")
                        onLoginGoogleSuccess()
                    } else {
                        Log.e("LOGIN", "ERRO Firebase Google: ", authResult.exception)
                    }
                }
        } catch (e: Exception) {
            Log.e("LOGIN", "Erro no Google SignIn: $e")
        }
    }
    // ---------------------

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        contentWindowInsets = WindowInsets(0.dp)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_finneo),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Bem vindo à Finneo",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = AlataFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "E-mail",
                style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth()
            )

            // Input conectado ao ViewModel
            CustomOutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    // Validação simples antes de ir para a senha
                    if (email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        onContinue() // Vai para LoginSectionPassword
                    }
                },
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF025B2F), contentColor = Color.White)
            ) {
                Text("Continuar", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("ou", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))

            // Botão Google
            OutlinedButton(
                onClick = { launcher.launch(googleClient.signInIntent) },
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = null, modifier = Modifier.size(24.dp))
                    Text("Continue com o Google", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp), textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                }
            }

            // ... (Botão Passkey e Criar conta mantidos)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray),
                contentPadding = PaddingValues(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.passkey_icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "Continue com a Passkey",
                        style = TextStyle(
                            fontFamily = AlataFont,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNotHaveAccount,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    "Criar uma Conta Finneo",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}