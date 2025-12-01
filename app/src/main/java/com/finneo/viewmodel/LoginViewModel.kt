package com.finneo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // Armazena o email digitado na primeira tela
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    // Estados de UI
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    // Função para realizar o login com Email e Senha
    fun loginWithEmailPassword(password: String, onSuccess: () -> Unit) {
        if (_email.value.isBlank() || password.isBlank()) {
            _loginError.value = "Preencha todos os campos."
            return
        }

        _isLoading.value = true
        _loginError.value = null

        auth.signInWithEmailAndPassword(_email.value, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    // Tratamento de erros comuns
                    val error = when (task.exception?.message) {
                        null -> "Erro desconhecido."
                        else -> "Falha no login. Verifique email e senha."
                    }
                    _loginError.value = error
                }
            }
    }

    // Função para resetar erros ao trocar de tela
    fun clearError() {
        _loginError.value = null
    }
}