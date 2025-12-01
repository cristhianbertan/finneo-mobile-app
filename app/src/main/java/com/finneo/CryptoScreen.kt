package com.finneo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CryptoScreen() {
    // Definimos como 1 pois é a tela Crypto
    var selectedTab by remember { mutableStateOf(1) }

    // Dados Criptomoedas
    val cryptoAssets = listOf(
        Asset("BTC", "+1,20%", "Var. 24h", "+1,20%", "Rede", "Bitcoin", "Preço BRL", "R$380.000", "Valor em carteira", "45.000,00", Color(0xFF43A047)),
        Asset("ETH", "-0,50%", "Var. 24h", "-0,50%", "Rede", "Ethereum", "Preço BRL", "R$15.200", "Valor em carteira", "20.000,00", Color(0xFFFF5757)),
        Asset("SOL", "+5,40%", "Var. 24h", "+5,40%", "Rede", "Solana", "Preço BRL", "R$850,00", "Valor em carteira", "5.000,00", Color(0xFF43A047)),
        Asset("USDT", "0,01%", "Var. 24h", "0,01%", "Rede", "Tron", "Preço BRL", "R$5,10", "Valor em carteira", "10.000,00", Color(0xFF43A047))
    )

    // Cores específicas de cripto
    val cryptoDetailColors = listOf(
        Color(0xFFF7931A), // Laranja BTC
        Color(0xFF627EEA), // Roxo/Azul ETH
        Color(0xFF26A17B), // Verde USDT
    )

    val cryptoDetails = listOf(
        PortfolioDetail("50,00%", "Bitcoin", "R$ ********", cryptoDetailColors[0]),
        PortfolioDetail("30,00%", "Altcoins", "R$ ********", cryptoDetailColors[1]),
        PortfolioDetail("20,00%", "Stablecoins", "R$ ********", cryptoDetailColors[2]),
    )

    val cryptoChartDetails = listOf(
        PortfolioDetail("50,00%", "Bitcoin", "R$ ********", cryptoDetailColors[0]),
        PortfolioDetail("30,00%", "Altcoins", "R$ ********", cryptoDetailColors[1]),
        PortfolioDetail("20,00%", "Stablecoins", "R$ ********", cryptoDetailColors[2]),
    )

    var isValueVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {}) {
                        Box(
                            modifier = Modifier.size(30.dp).background(DarkGreen, RoundedCornerShape(25)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Menu, "Menu", tint = Color.White)
                        }
                    }

                    Row(
                        modifier = Modifier.height(30.dp).background(DarkGreen, RoundedCornerShape(25)).padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.fillMaxHeight().clip(RoundedCornerShape(25))
                                .background(if (selectedTab == 0) BrightGreen else Color.Transparent)
                                .clickable { selectedTab = 0 }, // Aqui navegaria para FiatScreen
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Fiduciário", color = if (selectedTab == 0) Color.Black else Color.White, fontWeight = FontWeight.SemiBold, style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 16.sp), modifier = Modifier.padding(horizontal = 16.dp))
                        }
                        Box(
                            modifier = Modifier.fillMaxHeight().clip(RoundedCornerShape(20.dp))
                                .background(if (selectedTab == 1) BrightGreen else Color.Transparent) // BrightGreen aqui pois é a aba ativa
                                .clickable { selectedTab = 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Criptomoedas", color = if (selectedTab == 1) Color.Black else Color.White, fontWeight = FontWeight.SemiBold, style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 16.sp), modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Pesquisar serviços") }, trailingIcon = { Icon(Icons.Filled.Search, null) }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Text("Valor total acumulado", fontSize = 16.sp, color = Color.Black, fontFamily = AlataFont, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(6.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("R$ ", fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = AlataFont)
                    Text(if (isValueVisible) "80.000,00" else "*****", fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = AlataFont)
                    IconButton(onClick = { isValueVisible = !isValueVisible }) { Icon(if (isValueVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Valor", tint = Color.Black) }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ElevatedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(46.dp).padding(horizontal = 6.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = DarkGreen, contentColor = Color.White),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Personalize conforme o seu interesse!", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = LeagueSpartanFont)
                        Box(modifier = Modifier.size(36.dp).background(BrightGreen, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                            Icon(androidx.compose.material.icons.Icons.Default.ArrowForward, "Avançar", tint = DarkGreen, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(cryptoAssets.chunked(2).size) { rowIndex ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    cryptoAssets.chunked(2)[rowIndex].forEach { asset -> AssetCard(asset = asset) }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Resumo dos criptoativos", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium.copy(fontFamily = AlataFont, fontWeight = FontWeight.SemiBold), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                SimulatedPieChart(details = cryptoChartDetails)
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                Text("Detalhamento", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium.copy(fontFamily = AlataFont, fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(16.dp))
                cryptoDetails.forEach { detail -> DetailCard(detail = detail) }
            }
        }
    }
}