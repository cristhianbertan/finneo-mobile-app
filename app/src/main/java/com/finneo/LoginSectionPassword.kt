package com.finneo

import AlataFont
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun LoginSectionPassword(
    email: String = "cr****@gmail.com",
    onContinue: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var emailState by remember { mutableStateOf(email) }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Scaffold (
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(horizontal = 16.dp),
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    color = Color(0xFF025B2F),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_finneo),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Insira a sua senha",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = AlataFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Email mascarado
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = emailState,
                style = TextStyle(
                    fontFamily = AlataFont,
                    fontSize = 16.sp,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Senha",
                style = TextStyle(
                    fontFamily = AlataFont,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            PasswordField(
                value = password,
                onValueChange = {
                    password = it
                    if (isError) isError = false
                },
                modifier = Modifier.fillMaxWidth(),
                AlataFont = AlataFont,
                isError = isError
            )

            if (isError) {
                Text(
                    text = "Por favor, insira sua senha",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = AlataFont,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    if (password.isEmpty()) {
                        isError = true
                    } else {
                        isError = false
                        onContinue()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFF025B2F),
                    contentColor = Color(0xFFFFFFFF)
                )
            ) {
                Text(
                    "Continuar",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 16.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onForgotPassword,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Esqueceu a senha?",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}