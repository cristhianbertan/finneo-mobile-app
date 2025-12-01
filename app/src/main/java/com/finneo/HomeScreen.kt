package com.finneo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.finneo.ui.theme.AlataFont
import com.finneo.ui.theme.BrightGreen
import com.finneo.ui.theme.DarkGreen
import com.finneo.ui.theme.LeagueSpartanFont
import java.text.NumberFormat
import java.util.Locale

// --- FUNÇÕES AUXILIARES DE FORMATAÇÃO ---
fun String.parseCurrency(): Double {
    return this.replace("R$", "")
        .trim()
        .replace(".", "")
        .replace(",", ".")
        .toDoubleOrNull() ?: 0.0
}

fun Double.formatCurrency(): String {
    val ptBr = Locale("pt", "BR")
    // Formata usando vírgula para decimal e ponto para milhar
    return NumberFormat.getNumberInstance(ptBr).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(this)
}

fun Double.formatPercent(): String {
    return "%.2f".format(this).replace(".", ",") + "%"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var isValueVisible by remember { mutableStateOf(false) }

    // --- DADOS DOS ATIVOS (FIDUCIÁRIO) ---
    val fiduciaryAssets = remember {
        // Função Helper para calcular o total automaticamente
        fun createAsset(
            ticker: String,
            dailyVar: String,
            yearlyVar: String,
            type: String,
            rawPrice: Double, // Preço numérico (ex: 9.49)
            quantity: Int,    // Quantidade numérica (ex: 50)
            color: Color
        ): Asset {
            // CÁLCULO AUTOMÁTICO DO TOTAL
            val total = rawPrice * quantity

            return Asset(
                ticker = ticker,
                valuationLastDay = dailyVar,
                titlepercentProfitPerYear = "Rend. último ano",
                percentProfitPerYear = yearlyVar,
                titleType = "Setor",
                type = type,
                titlePrice = "Preço BRL",
                price = "R$ ${rawPrice.formatCurrency()}", // Formata para exibição
                titleWalletValue = "Valor em carteira",
                walletValue = total.formatCurrency(),      // Total calculado e formatado
                titleNumberOfShares = "Quant. de cotas",
                numberOfShares = quantity.toString(),
                trendColor = color
            )
        }

        listOf(
            // Agora passamos os valores numéricos e o cálculo é feito sozinho
            createAsset("MXRF11", "0,42%", "12,05%", "Papel", 9.49, 50, Color(0xFF43A047)),
            createAsset("CPTS11", "0,41%", "12,95%", "Papel", 7.30, 40, Color(0xFF43A047)),
            createAsset("GARE11", "-0,42%", "12,05%", "Papel", 9.49, 30, Color(0xFF43A047)),
            createAsset("MXRF11", "0,42%", "12,05%", "Papel", 9.49, 20, Color(0xFF43A047))
        )
    }

    // --- DADOS DOS ATIVOS (CRIPTO) ---
    // Mantemos manual ou podemos criar uma lógica similar se desejar
    val cryptoAssets = remember {
        listOf(
            Asset("BTC", "+1,20%", "Var. 24h", "+1,20%", "Rede", "Bitcoin", "Preço BRL", "R$380.000", "Valor em carteira", "45.000,00", "", "", Color(0xFF43A047)),
            Asset("ETH", "-0,50%", "Var. 24h", "-0,50%", "Rede", "Ethereum", "Preço BRL", "R$15.200", "Valor em carteira", "20.000,00", "", "", Color(0xFFFF5757)),
            Asset("SOL", "+5,40%", "Var. 24h", "+5,40%", "Rede", "Solana", "Preço BRL", "R$850,00", "Valor em carteira", "5.000,00", "", "", Color(0xFF43A047)),
            Asset("USDT", "0,01%", "Var. 24h", "0,01%", "Rede", "Tron", "Preço BRL", "R$5,10", "Valor em carteira", "10.000,00", "", "", Color(0xFF43A047))
        )
    }

    // --- CÁLCULO DINÂMICO DOS DETALHES ---
    val fiduciaryDetails = remember(fiduciaryAssets) {
        val totalValue = fiduciaryAssets.sumOf { it.walletValue.parseCurrency() }

        val fiisSum = fiduciaryAssets.filter { it.type == "Papel" || it.type == "Tijolo" }.sumOf { it.walletValue.parseCurrency() }
        val acoesSum = fiduciaryAssets.filter { it.type == "Ação" }.sumOf { it.walletValue.parseCurrency() }
        val poupancaSum = fiduciaryAssets.filter { it.type == "Poupança" }.sumOf { it.walletValue.parseCurrency() }

        listOf(
            PortfolioDetail(
                percentage = if (totalValue > 0) ((fiisSum / totalValue) * 100).formatPercent() else "0,00%",
                name = "Fundos Imob.",
                value = fiisSum.formatCurrency(),
                color = Color(0xFF304D8C)
            ),
            PortfolioDetail(
                percentage = if (totalValue > 0) ((acoesSum / totalValue) * 100).formatPercent() else "0,00%",
                name = "Ações",
                value = acoesSum.formatCurrency(),
                color = Color(0xFF8D5353)
            ),
            PortfolioDetail(
                percentage = if (totalValue > 0) ((poupancaSum / totalValue) * 100).formatPercent() else "0,00%",
                name = "Poupança",
                value = poupancaSum.formatCurrency(),
                color = Color(0xFF43A047).copy(red = 0.2f, green = 0.45f, blue = 0.2f)
            ),
        )
    }

    val cryptoDetails = listOf(
        PortfolioDetail("50,00%", "Bitcoin", "********", Color(0xFFF7931A)),
        PortfolioDetail("30,00%", "Altcoins", "********", Color(0xFF627EEA)),
        PortfolioDetail("20,00%", "Stablecoins", "********", Color(0xFF26A17B)),
    )

    val currentAssets = if (selectedTab == 0) fiduciaryAssets else cryptoAssets
    val currentDetails = if (selectedTab == 0) fiduciaryDetails else cryptoDetails

    val currentTotalNumeric = remember(currentAssets) {
        currentAssets.sumOf { it.walletValue.parseCurrency() }
    }
    val currentTotal = currentTotalNumeric.formatCurrency()
    val currentTitle = if (selectedTab == 0) "Resumo dos investimentos fiduciários" else "Resumo dos criptoativos"

    val pieChartInputs = remember(currentDetails) {
        currentDetails.map { detail ->
            val value = detail.percentage
                .removeSuffix("%")
                .replace(",", ".")
                .toDoubleOrNull()
                ?.toInt() ?: 0

            PieChartInput(
                color = detail.color,
                value = value,
                description = detail.name
            )
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {}) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(DarkGreen, RoundedCornerShape(25)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Menu, "Menu", tint = Color.White)
                        }
                    }

                    // Toggle Switch
                    Row(
                        modifier = Modifier
                            .height(30.dp)
                            .background(DarkGreen, RoundedCornerShape(25))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(25))
                                .background(if (selectedTab == 0) BrightGreen else Color.Transparent)
                                .clickable { selectedTab = 0 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Fiduciário",
                                color = if (selectedTab == 0) Color.Black else Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 16.sp),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selectedTab == 1) BrightGreen else Color.Transparent)
                                .clickable { selectedTab = 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Criptomoedas",
                                color = if (selectedTab == 1) Color.Black else Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 16.sp),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Pesquisar serviços") },
                    trailingIcon = { Icon(Icons.Filled.Search, null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    "Valor total acumulado",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontFamily = AlataFont,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("R$ ", fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = AlataFont)
                    Text(
                        if (isValueVisible) currentTotal else "*****",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontFamily = AlataFont
                    )
                    IconButton(onClick = { isValueVisible = !isValueVisible }) {
                        Icon(
                            if (isValueVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            "Valor",
                            tint = Color.Black
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                ElevatedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .padding(horizontal = 10.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = DarkGreen,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Personalize conforme o seu interesse!",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = LeagueSpartanFont
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(BrightGreen, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                androidx.compose.material.icons.Icons.Default.ArrowForward,
                                "Avançar",
                                tint = DarkGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(currentAssets.chunked(2).size) { rowIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currentAssets.chunked(2)[rowIndex].forEach { asset ->
                        AssetCard(
                            asset = asset,
                            isValueVisible = isValueVisible,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (currentAssets.chunked(2)[rowIndex].size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // --- GRÁFICO ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = currentTitle,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    PieChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        input = pieChartInputs,
                        centerText = if (isValueVisible) "Total\nR$ $currentTotal" else "",
                        radius = 400f,
                        innerRadius = 200f
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    "Detalhamento",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                currentDetails.forEach { detail ->
                    // Filtra ativos conforme o grupo (Papel/Tijolo -> Fundos Imob)
                    val assetsForDetail = if(selectedTab == 0) {
                        if(detail.name == "Fundos Imob.") {
                            currentAssets.filter { it.type == "Papel" || it.type == "Tijolo" }
                        } else {
                            currentAssets.filter { it.type == detail.name }
                        }
                    } else {
                        emptyList()
                    }

                    DetailCard(
                        detail = detail,
                        subAssets = assetsForDetail,
                        isValueVisible = isValueVisible
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}