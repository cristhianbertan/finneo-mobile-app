package com.finneo

import AlataFont
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finneo.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onContinue: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel() // Injetando o ViewModel
) {
    // Estados do Formulário
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // CORREÇÃO: Variável separada
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var document by remember { mutableStateOf("") }

    // Assumindo que isValidCpf é uma função utilitária que você tem no projeto
    val isCpfInvalid = remember(document) {
        document.isNotEmpty() && document.length == 11 && !isValidCpf(document)
    }

    var gender by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Estados do ViewModel
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage

    Scaffold(
        containerColor = Color(0xFFFFFFFF)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item { Spacer(modifier = Modifier.height(60.dp)) }

            // Foto de perfil
            item {
                Image(
                    painter = painterResource(id = R.drawable.user_photo), // Certifique-se que esta imagem existe
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }

            item {
                Text(
                    text = "Insira sua foto",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(36.dp)) }

            item {
                Text(
                    text = "Credenciais de Acesso",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF025B2F)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = "Email",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                EmailField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    text = "Senha",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    text = "Confirmar Senha",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // CORREÇÃO: Usando confirmPassword e confirmPasswordVisible
            item {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }

            item {
                Text(
                    text = "Dados Pessoais",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF025B2F)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // --- CAMPOS DE DADOS PESSOAIS ---

            item { Text(text = "Nome", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(value = name, onValueChange = { name = it }, singleLine = true, modifier = Modifier.fillMaxWidth().height(54.dp)) }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { Text(text = "Sobrenome", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(value = lastName, onValueChange = { lastName = it }, singleLine = true, modifier = Modifier.fillMaxWidth()) }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { Text(text = "CPF", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth()) }
            item { CpfField(value = document, onValueChange = { document = it }, isError = isCpfInvalid, modifier = Modifier.fillMaxWidth()) }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { Text(text = "Gênero", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth()) }
            item { GenderSelectionField(value = gender, onValueChange = { gender = it }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { Text(text = "Data de Nascimento", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth()) }
            item { DateOfBirthField(value = dateOfBirth, onValueChange = { dateOfBirth = it }, modifier = Modifier.fillMaxWidth()) }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { Text(text = "Telefone", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth()) }
            item { PhoneNumberField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth()) }

            // EXIBIÇÃO DE ERRO
            if (errorMessage != null) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }

            // BOTÃO DE CADASTRO
            item {
                ElevatedButton(
                    onClick = {
                        if (password != confirmPassword) {
                            // Erro local simples se as senhas não baterem
                            // Idealmente mostraria um erro na UI, mas aqui o ViewModel trata campos vazios
                        } else {
                            viewModel.registerUser(
                                email, password, name, lastName, document, gender, dateOfBirth, phone,
                                onSuccess = onContinue
                            )
                        }
                    },
                    enabled = !isLoading, // Desabilita se estiver carregando
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFF025B2F),
                        contentColor = Color(0xFFFFFFFF)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            "Próximo",
                            style = TextStyle(
                                fontFamily = AlataFont,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(70.dp)) }
        }
    }
}