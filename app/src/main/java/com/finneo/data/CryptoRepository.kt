package com.finneo.data

import android.util.Log
import com.finneo.Asset
import com.finneo.getColorForTicker
import java.math.BigInteger
import java.text.NumberFormat
import java.util.Locale

class CryptoRepository {
    private val covalentApi = CryptoApiService.createCovalent()
    private val geckoApi = CryptoApiService.createGecko()

    private val tickerToGeckoId = mapOf(
        "ETH" to "ethereum",
        "BTC" to "bitcoin",
        "USDC" to "usd-coin",
        "USDT" to "tether",
        "DAI" to "dai",
        "UNI" to "uniswap"
    )

    private val API_KEY = "cqt_rQVHvQXC4qQ9qWQ4jJHD3JXDKFxb"

    suspend fun fetchAssetsForWallet(walletAddress: String): List<Asset> {
        return try {
            // 1. Busca os saldos da Covalent
            val response = covalentApi.getWalletBalances(
                chainId = "1",
                walletAddress = walletAddress,
                apiKey = API_KEY
            )

            if (response.error || response.data.items.isEmpty()) {
                return emptyList()
            }

            val rawAssets = response.data.items.filter { (it.quote ?: 0.0) > 0.0 }

            val geckoIds = rawAssets
                .mapNotNull { tickerToGeckoId[it.contractTickerSymbol] }
                .distinct()
                .joinToString(",")

            // 3. Busca as variações de preço (24h) no CoinGecko
            val priceChanges = if (geckoIds.isNotBlank()) {
                geckoApi.getPriceChanges(ids = geckoIds, vsCurrencies = "usd")
            } else {
                emptyMap()
            }

            rawAssets.map { token ->
                mapToAsset(token, priceChanges)
            }

        } catch (e: Exception) {
            Log.e("CryptoRepo", "Exceção ao buscar dados: ${e.message}")
            emptyList()
        }
    }

    private fun mapToAsset(
        token: TokenItem,
        priceChanges: Map<String, Map<String, Double>>
    ): Asset {
        val ptBr = Locale("pt", "BR")
        val formatBrl = NumberFormat.getCurrencyInstance(ptBr)
        val usdToBrl = 5.80

        val valueInBrl = (token.quote ?: 0.0) * usdToBrl
        val priceInBrl = (token.quoteRate ?: 0.0) * usdToBrl

        // Localiza o ID do token para CoinGecko
        val geckoId = tickerToGeckoId[token.contractTickerSymbol]

        // 1. Variação de 24h
        val change24h = geckoId?.let { id ->
            priceChanges[id]?.get("usd_24h_change")
        } ?: 0.0

        // 2. Rendimento Anual
        val profitPerYear = when (token.contractTickerSymbol) {
            "BTC" -> "+120.00%"
            "ETH" -> "+85.50%"
            "USDT", "USDC" -> "+3.00%" // Stablecoins
            else -> "+15.00%"
        }

        val balanceReadable = try {
            val balanceBig = BigInteger(token.balance)
            val divisor = BigInteger.TEN.pow(token.contractDecimals)
            balanceBig.toDouble() / divisor.toDouble()
        } catch (e: Exception) { 0.0 }

        val valuationLastDayText = "${"%.2f".format(change24h)}%"
        val finalProfitPerYear = profitPerYear.replace(".", ",")

        val finalValuationLastDay = if (change24h >= 0) "+$valuationLastDayText" else valuationLastDayText

        return Asset(
            ticker = token.contractTickerSymbol,

            // CAMPOS ATUALIZADOS
            valuationLastDay = finalValuationLastDay.replace(".", ","), // Rendimento 24h
            titlepercentProfitPerYear = "Rend. último ano",
            percentProfitPerYear = finalProfitPerYear,

            // Outros campos (mantidos)
            titleType = "Rede",
            type = "Ethereum",
            titlePrice = "Preço Atual",
            price = formatBrl.format(priceInBrl),
            titleWalletValue = "Valor em carteira",
            walletValue = formatBrl.format(valueInBrl),
            titleNumberOfShares = "Saldo",
            numberOfShares = "%.4f".format(balanceReadable).replace(".", ","),
            trendColor = getColorForTicker(token.contractTickerSymbol)
        )
    }
}