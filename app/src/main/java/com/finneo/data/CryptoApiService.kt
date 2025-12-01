package com.finneo.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoApiService {

    @GET("v1/{chain_id}/address/{wallet_address}/balances_v2/")
    suspend fun getWalletBalances(
        @Path("chain_id") chainId: String,
        @Path("wallet_address") walletAddress: String,
        @Query("key") apiKey: String
    ): CovalentResponse

    @GET("api/v3/simple/price")
    suspend fun getPriceChanges(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String = "usd",
        @Query("include_24hr_change") include24hChange: Boolean = true,
    ): Map<String, Map<String, Double>>


    companion object {
        private const val COVALENT_BASE_URL = "https://api.covalenthq.com/"
        private const val GECKO_BASE_URL = "https://api.coingecko.com/"

        fun createCovalent(): CryptoApiService {
            return Retrofit.Builder()
                .baseUrl(COVALENT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CryptoApiService::class.java)
        }

        fun createGecko(): CryptoApiService {
            return Retrofit.Builder()
                .baseUrl(GECKO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CryptoApiService::class.java)
        }
    }
}