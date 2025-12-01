package com.finneo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DarkGreen = Color(0xFF025B2F)
val BrightGreen = Color(0xFF00C749)
val LightBackground = Color(0xFFEEEEEE)
val AlataFont = androidx.compose.ui.text.font.FontFamily.SansSerif

val LeagueSpartanFont = FontFamily(Font(R.font.league_spartan))

data class Asset(
    val ticker: String,
    val valuationLastDay: String,
    val titlepercentProfitPerYear: String,
    val percentProfitPerYear: String,
    val titleType: String,
    val type: String,
    val titlePrice: String,
    val price: String,
    val titleWalletValue: String,
    val walletValue: String,
    val trendColor: Color,
)

data class PortfolioDetail(
    val percentage: String,
    val name: String,
    val value: String,
    val color: Color
)

@Composable
fun AssetCard(asset: Asset) {
    val valuationColor = if (asset.valuationLastDay.contains("-")) {
        Color(0xFFFF5757)
    } else {
        BrightGreen
    }

    val cornerShape = RoundedCornerShape(8.dp)
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(150.dp)
            .padding(8.dp)
            .border(1.dp, Color.LightGray, cornerShape),
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = asset.ticker,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = asset.valuationLastDay,
                    fontSize = 14.sp,
                    fontFamily = LeagueSpartanFont,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .background(valuationColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = asset.titlepercentProfitPerYear, fontSize = 8.sp, color = Color.Gray)
                    Text(text = asset.percentProfitPerYear, fontSize = 14.sp, color = Color.Black)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = asset.titleWalletValue, fontSize = 8.sp, color = Color.Gray)
                    Text(text = "R$ ${asset.walletValue}", fontSize = 12.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ){
                Text(text = asset.titleType, fontSize = 8.sp, color = Color.Gray)
                Text(text = asset.type, fontSize = 14.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Column(){
                Text(text = asset.titlePrice, fontSize = 8.sp, color = Color.Gray)
                Text(text = asset.price, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun SimulatedPieChart(details: List<PortfolioDetail>, modifier: Modifier = Modifier) {
    val totalPercentage = details.sumOf { it.percentage.removeSuffix("%").toDoubleOrNull() ?: 0.0 }
    val colors = details.map { it.color }
    val proportions = details.map { (it.percentage.removeSuffix("%").toDoubleOrNull() ?: 0.0) / totalPercentage }

    Canvas(modifier = modifier.size(200.dp)) {
        var startAngle = 0f
        val chartSize = size.minDimension / 2f
        val radius = chartSize * 0.8f

        proportions.forEachIndexed { index, proportion ->
            val sweepAngle = (proportion * 360f).toFloat()
            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset((size.width - chartSize) / 2, (size.height - chartSize) / 2),
                size = Size(chartSize, chartSize),
                style = Stroke(width = radius * 0.5f)
            )
            startAngle += sweepAngle
        }
        drawCircle(color = Color.White, radius = radius * 0.35f, center = center)
    }
}

@Composable
fun DetailCard(detail: PortfolioDetail) {
    val cornerShape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(vertical = 4.dp)
            .background(detail.color, cornerShape)
            .clickable { /* Ação de clique */ }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = detail.percentage,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = detail.name,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "R$ *****",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PortfolioSummaryScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    // Dadods mocados
    val assetData = listOf(
        Asset("MXRF11",
            "0,42%",
            "Rend. último ano",
            "12,05%",
            "Setor",
            "Papel",
            "Preço BRL",
            "R$9,49",
            "Valor em carteira",
            "25.000,00",
            Color(0xFF43A047)
        ),
        Asset("CPTS11",
            "0,41%",
            "Rend. último ano",
            "12,95%",
            "Setor",
            "Papel",
            "Preço BRL",
            "R$7,30",
            "Valor em carteira",
            "25.000,00",
            Color(0xFF43A047)
        ),
        Asset("GARE11",
            "-0,42%",
            "Rend. último ano",
            "12,05%",
            "Setor",
            "Papel",
            "Preço BRL",
            "R$9,49",
            "Valor em carteira",
            "25.000,00",
            Color(0xFF43A047)
        ),
        Asset("MXRF11",
            "0,42%",
            "Rend. último ano",
            "12,05%",
            "Setor",
            "Papel",
            "Preço BRL",
            "R$9,49",
            "Valor em carteira",
            "25.000,00",
            Color(0xFF43A047)
        )
    )

    val detailColors = listOf(
        Color(0xFF43A047).copy(red = 0.2f, green = 0.45f, blue = 0.2f),
        Color(0xFF304D8C),
        Color(0xFF8D5353),
    )
    val portfolioDetails = listOf(
        PortfolioDetail("33,28%", "Fundos Imobiliários", "R$ ********", detailColors[1]),
        PortfolioDetail("33,36%", "Ações", "R$ ********", detailColors[2]),
        PortfolioDetail("33,36%", "Poupança", "R$ ********", detailColors[0]),
    )
    val chartDetails = listOf(
        PortfolioDetail("33,36%", "Ações", "R$ ********", detailColors[2]),
        PortfolioDetail("33,28%", "Fundos Imobiliários", "R$ ********", detailColors[1]),
        PortfolioDetail("33,36%", "Poupança", "R$ ********", detailColors[0]),
    )

    var isValueVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {}) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    color = DarkGreen,
                                    shape = RoundedCornerShape(25)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .height(30.dp)
                            .background(color = DarkGreen, shape = RoundedCornerShape(25))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botão Fiduciário
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(25))
                                .background(if (selectedTab == 0) BrightGreen else Color.Transparent)
                                .clickable { selectedTab = 0 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Fiduciário",
                                color = if (selectedTab == 0) Color.Black else Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(fontFamily = LeagueSpartanFont),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        // Botão Criptomoedas
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selectedTab == 1) LightBackground else Color.Transparent)
                                .clickable { selectedTab = 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Criptomoedas",
                                color = if (selectedTab == 1) DarkGreen else Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(fontFamily = LeagueSpartanFont),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        },
        content = { innerPadding ->
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
                            .padding(horizontal = 16.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray,
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    ElevatedButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
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
                                text = "Personalize conforme o seu interesse!",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = LeagueSpartanFont
                            )

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(color = BrightGreen, shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowForward,
                                    contentDescription = "Avançar",
                                    tint = DarkGreen, // Cor do ícone
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier
                        .height(30.dp))
                    Text(
                        text = "Valor total acumulado",
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
                        Text(if (isValueVisible) "25.000,00" else "*****", fontWeight = FontWeight.Bold, fontSize = 24.sp, fontFamily = AlataFont)
                        IconButton(onClick = { isValueVisible = !isValueVisible }) {
                            Icon(if (isValueVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, "Valor", tint = Color.Black)
                        }
                    }
                }

                // Cards de Ativos
                items(assetData.chunked(2).size) { rowIndex ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        assetData.chunked(2)[rowIndex].forEach { asset ->
                            AssetCard(asset = asset)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Resumo dos investimentos fiduciários",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = AlataFont, fontWeight = FontWeight.SemiBold),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center // Linha adicionada
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    SimulatedPieChart(details = chartDetails)
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    Text(
                        text = "Detalhamento",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = AlataFont, fontWeight = FontWeight.SemiBold)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    portfolioDetails.forEach { detail ->
                        DetailCard(detail = detail)
                    }
                }
            }
        }
    )
}
