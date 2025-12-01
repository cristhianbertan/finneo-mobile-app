package com.finneo.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun registerUser(
        email: String,
        pass: String,
        name: String,
        surname: String,
        cpf: String,
        gender: String,
        birthDate: String,
        phone: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || pass.isBlank() || name.isBlank()) {
            _errorMessage.value = "Preencha os campos obrigatórios."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        // 1. Cria o usuário no Authentication
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: return@addOnSuccessListener

                // 2. Prepara os dados para o Firestore
                val userMap = hashMapOf(
                    "uid" to userId,
                    "email" to email,
                    "name" to name,
                    "surname" to surname,
                    "cpf" to cpf,
                    "gender" to gender,
                    "birthDate" to birthDate,
                    "phone" to phone,
                    "createdAt" to com.google.firebase.Timestamp.now()
                )

                // 3. Salva os dados extras no Firestore
                db.collection("users").document(userId)
                    .set(userMap)
                    .addOnSuccessListener {
                        _isLoading.value = false
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        _isLoading.value = false
                        _errorMessage.value = "Erro ao salvar dados: ${e.message}"
                    }
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                _errorMessage.value = "Erro ao criar conta: ${e.message}"
            }
    }
}