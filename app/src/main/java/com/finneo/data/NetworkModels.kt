package com.finneo.data

import com.google.gson.annotations.SerializedName

data class CovalentResponse(
    @SerializedName("data") val data: CovalentData,
    @SerializedName("error") val error: Boolean
)

data class CovalentData(
    @SerializedName("address") val address: String,
    @SerializedName("items") val items: List<TokenItem>
)

data class TokenItem(
    @SerializedName("contract_name") val contractName: String,
    @SerializedName("contract_ticker_symbol") val contractTickerSymbol: String,
    @SerializedName("logo_url") val logoUrl: String,
    @SerializedName("quote") val quote: Double?,
    @SerializedName("quote_rate") val quoteRate: Double?,
    @SerializedName("balance") val balance: String,
    @SerializedName("contract_decimals") val contractDecimals: Int
)