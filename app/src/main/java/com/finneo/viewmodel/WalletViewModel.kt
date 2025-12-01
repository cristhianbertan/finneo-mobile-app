package com.finneo.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finneo.Asset
import com.finneo.ShareOption
import com.finneo.data.CryptoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.launch

class WalletViewModel : ViewModel() {
    private val repository = CryptoRepository()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _assets = mutableStateOf<List<Asset>>(emptyList())
    val assets: State<List<Asset>> = _assets

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _walletAddress = mutableStateOf("")
    val walletAddress: State<String> = _walletAddress

    fun onWalletAddressChange(newAddress: String) {
        _walletAddress.value = newAddress
    }

    fun saveWalletToFirebase(address: String, onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        if (address.isBlank()) return

        Log.d("WalletVM", "Iniciando salvamento da carteira: $address")
        _isLoading.value = true

        val walletData = hashMapOf(
            "address" to address,
            "createdAt" to FieldValue.serverTimestamp(),
            "type" to "EVM"
        )

        db.collection("users").document(userId).collection("wallets")
            .add(walletData)
            .addOnSuccessListener {
                Log.d("WalletVM", "Carteira salva com sucesso no Firestore. ID: ${it.id}")
                onWalletAddressChange(address) // 1. Atualiza o estado

                fetchWalletData()

                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("WalletVM", "ERRO ao salvar carteira: ${e.message}")
                _isLoading.value = false
            }
    }

    fun fetchWalletData() {
        val currentAddress = _walletAddress.value
        if (currentAddress.isBlank()) {
            Log.d("WalletVM", "Endereço da carteira está VAZIO. Não busca dados.")
            return
        }

        Log.d("WalletVM", "Buscando dados para o endereço: $currentAddress")

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.fetchAssetsForWallet(currentAddress)

                if (result.isEmpty()) {
                    Log.w("WalletVM", "A busca retornou uma lista vazia de ativos.")
                } else {
                    Log.d("WalletVM", "Ativos carregados: ${result.size}")
                }
                _assets.value = result
            } catch (e: Exception) {
                Log.e("WalletVM", "Erro ao buscar dados na API", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun generateShareableData(option: ShareOption, fiduciaryAssets: List<Asset>): String {

        val assetsToShare = when (option) {
            is ShareOption.Crypto -> assets.value
            is ShareOption.Fiduciary -> fiduciaryAssets
            is ShareOption.Both -> assets.value + fiduciaryAssets // Junta as duas listas
        }

        val shareableAssets = assetsToShare.map { asset ->
            mapOf(
                "ticker" to asset.ticker,
                "valuationLastDay" to asset.valuationLastDay,
                "percentProfitPerYear" to asset.percentProfitPerYear,
                "titleType" to asset.titleType,
                "type" to asset.type,
                "titlePrice" to asset.titlePrice,
                "price" to asset.price,
                "titleNumberOfShares" to asset.titleNumberOfShares,
                "shares" to asset.numberOfShares
            )
        }

        return try {
            Gson().toJson(shareableAssets)
        } catch (e: Exception) {
            Log.e("WalletVM", "Erro ao serializar dados para compartilhamento: ${e.message}")
            "[]"
        }
    }
}