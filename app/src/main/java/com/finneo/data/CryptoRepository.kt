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
        "BNB" to "binancecoin",
        "AVAX" to "avalanche-2",
        "MATIC" to "matic-network",
        "DAI" to "dai",
        "DOT" to "polkadot",
        "LINK" to "chainlink",
        "WETH" to "weth",
        "SOL" to "solana",
        "ADA" to "cardano",
        "DOGE" to "dogecoin",
        "SHIB" to "shiba-inu",
        "TRX" to "tron"
    )

    private val API_KEY = "cqt_rQ63TfmQBfHyJPpmcg9yhQqYYXdV"
    private val TARGET_CHAINS = listOf("1",     // Ethereum Mainnet
        "56",    // BNB Smart Chain
        "137",   // Polygon Mainnet
        "43114", // Avalanche C-Chain
        "10",    // Optimism
        "42161", // Arbitrum
        "250",   // Fantom
        "100",   // Gnosis (xDai)
        "42220"  // Celo)
    )

    private fun getChainName(chainId: String): String {
        return when (chainId) {
            "1" -> "Ethereum"
            "56" -> "BNB Smart Chain"
            "137" -> "Polygon"
            "43114" -> "Avalanche"
            "10" -> "Optimism"
            "42161" -> "Arbitrum"
            "250" -> "Fantom"
            "100" -> "Gnosis (xDai)"
            "42220" -> "Celo"
            "128" -> "Huobi ECO Chain (HECO)"
            "66" -> "OKC (OKXChain)"
            "1088" -> "Metis"
            "1666600000" -> "Harmony"
            "2000" -> "Dogechain"
            "8217" -> "Klaytn"
            else -> "Outra Rede"
        }
    }


    suspend fun fetchAssetsForWallet(walletAddress: String): List<Asset> {
        val allRawAssets = mutableListOf<TokenItem>()

        for (chainId in TARGET_CHAINS) {
            try {
                val response = covalentApi.getWalletBalances(
                    chainId = chainId,
                    walletAddress = walletAddress,
                    apiKey = API_KEY
                )

                if (!response.error && response.data.items.isNotEmpty()) {
                    val assetsInChain = response.data.items
                        .filter { (it.quote ?: 0.0) > 0.0 }
                        .onEach { it.networkName = getChainName(chainId) }

                    allRawAssets.addAll(assetsInChain)
                }
            } catch (e: Exception) {
                Log.e("CryptoRepo", "Erro ao buscar Chain ID $chainId: ${e.message}")
            }
        }

        if (allRawAssets.isEmpty()) {
            Log.w("CryptoRepo", "Nenhum ativo encontrado em todas as redes configuradas.")
            return emptyList()
        }

        val geckoIds = allRawAssets
            .mapNotNull { tickerToGeckoId[it.contractTickerSymbol] }
            .distinct()
            .joinToString(",")

        val priceChanges = if (geckoIds.isNotBlank()) {
            geckoApi.getPriceChanges(ids = geckoIds, vsCurrencies = "usd")
        } else {
            emptyMap()
        }

        return allRawAssets.map { token ->
            mapToAsset(token, priceChanges)
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

        val geckoId = tickerToGeckoId[token.contractTickerSymbol]

        val change24h = geckoId?.let { id ->
            priceChanges[id]?.get("usd_24h_change")
        } ?: 0.0

        val profitPerYear = when (token.contractTickerSymbol) {
            "BTC" -> "+120.00%"
            "ETH" -> "+85.50%"
            "USDT", "USDC" -> "+3.00%"
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

            valuationLastDay = finalValuationLastDay.replace(".", ","),
            titlepercentProfitPerYear = "Rend. último ano",
            percentProfitPerYear = finalProfitPerYear,

            titleType = "Rede",
            type = token.networkName ?: "Não Informada",

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