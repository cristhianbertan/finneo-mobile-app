package com.finneo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.finneo.data.WalletItem
import com.finneo.ui.theme.AlataFont
import com.finneo.ui.theme.BrightGreen
import com.finneo.ui.theme.DarkGreen
import com.finneo.ui.theme.LeagueSpartanFont
import com.finneo.viewmodel.WalletViewModel
import java.text.NumberFormat
import java.util.Locale

fun String.parseCurrency(): Double {
    return this.replace("R$", "")
        .replace("$", "")
        .trim()
        .replace(".", "")
        .replace(",", ".")
        .toDoubleOrNull() ?: 0.0
}

fun String.parseShares(): Double {
    return this.replace(".", "")
        .replace(",", ".")
        .toDoubleOrNull() ?: 0.0
}

fun Double.formatCurrency(): String {
    val ptBr = Locale("pt", "BR")
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
fun HomeScreen(
    navController: NavController,
    viewModel: WalletViewModel = viewModel(),
    startTab: Int = 0
) {
    val apiAssets by viewModel.assets
    val isLoading by viewModel.isLoading
    val walletAddress by viewModel.walletAddress

    var selectedTab by remember { mutableIntStateOf(startTab) }
    var isValueVisible by remember { mutableStateOf(false) }

    var showSharePopup by remember { mutableStateOf(false) }

    val fiduciaryAssets = remember {
        listOf(
            Asset("MXRF11", "0,42%", "Rend. último ano", "12,05%", "Setor", "Papel", "Preço BRL", "R$ 9,49", "Valor em carteira", "474,50", "Quant. de cotas", "50", Color(0xFF43A047)),
            Asset("CPTS11", "0,41%", "Rend. último ano", "12,95%", "Setor", "Papel", "Preço BRL", "R$ 7,30", "Valor em carteira", "292,00", "Quant. de cotas", "40", Color(0xFF43A047))
        )
    }

    val currentAssets = if (selectedTab == 0) fiduciaryAssets else apiAssets

    val currentDetails = remember(currentAssets) {
        if (currentAssets.isEmpty()) {
            emptyList()
        } else {
            val totalValue = currentAssets.sumOf { it.walletValue.parseCurrency() }

            currentAssets.groupBy { it.ticker }
                .map { (ticker, assets) ->
                    object {
                        val ticker = ticker
                        val assets = assets
                        val groupValue = assets.sumOf { it.walletValue.parseCurrency() }
                        val totalShares = assets.sumOf { it.numberOfShares.parseShares() }
                    }
                }
                .sortedByDescending { it.totalShares }
                .take(5)
                .map { groupedAsset ->
                    val perc = if (totalValue > 0) (groupedAsset.groupValue / totalValue) * 100 else 0.0

                    PortfolioDetail(
                        percentage = perc.formatPercent(),
                        name = groupedAsset.ticker,
                        value = groupedAsset.groupValue.formatCurrency(),
                        color = getColorForTicker(groupedAsset.ticker)
                    )
                }
        }
    }

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

                    Row(
                        modifier = Modifier
                            .height(30.dp)
                            .background(DarkGreen, RoundedCornerShape(25))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Opção Fiduciário
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

                        // Opção Criptomoedas
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(if (selectedTab == 1) BrightGreen else Color.Transparent)
                                .clickable {
                                    if (walletAddress.isBlank()) {
                                        navController.navigate("add_wallet")
                                    } else {
                                        selectedTab = 1
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Criptomoedas",
                                color = if (selectedTab == 1) Color.Black else Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 16.sp),
                                modifier = Modifier.padding(horizontal = 16.dp)
                                    .clip(RoundedCornerShape(25))
                            )
                        }
                    }

                    // Botão +
                    IconButton(onClick = { navController.navigate("add_wallet") }) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_input_add),
                            contentDescription = "Adicionar",
                            tint = DarkGreen
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        if (showSharePopup) {
            ShareOptionsPopup(
                initialSelection = selectedTab,
                onDismiss = { showSharePopup = false },
                onProceed = {
                    navController.navigate("share_screen") // Navega para a tela de seleção
                    showSharePopup = false
                }
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Barra de pesquisa
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Pesquisar serviços") },
                        trailingIcon = { Icon(Icons.Filled.Search, null) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                // Valor total
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        "Valor total acumulado",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth().padding(6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(6.dp),
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
                        modifier = Modifier.fillMaxWidth().height(46.dp).padding(horizontal = 10.dp),
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
                                fontWeight = FontWeight.Bold,
                                fontFamily = LeagueSpartanFont
                            )
                            Box(
                                modifier = Modifier.size(36.dp).background(BrightGreen, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ArrowForward,
                                    "Avançar",
                                    tint = DarkGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Cards dos ativos
                items(currentAssets.chunked(2).size) { rowIndex ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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

                if (currentAssets.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "Nenhum ativo encontrado.\nAdicione uma carteira.",
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }

                if (currentAssets.isNotEmpty()) {
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
                                modifier = Modifier.fillMaxWidth().height(350.dp),
                                input = pieChartInputs,
                                centerText = if (isValueVisible) "Total\nR$ $currentTotal" else "",
                                radius = 400f,
                                innerRadius = 200f
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }

                if (currentAssets.isNotEmpty()) {
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
                            val subAssets = currentAssets.filter { it.ticker == detail.name }
                            DetailCard(
                                detail = detail,
                                subAssets = subAssets,
                                isValueVisible = isValueVisible
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    ElevatedButton(
                        onClick = { navController.navigate("share_screen") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .padding(horizontal = 10.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BrightGreen,
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
                    ) {
                        Text(
                            "Compartilhar Carteira",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = LeagueSpartanFont
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun WalletItemRow(
    wallet: WalletItem,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = wallet.type,
                style = TextStyle(fontSize = 12.sp, color = DarkGreen, fontFamily = LeagueSpartanFont)
            )
            val shortAddress = wallet.address.take(6) + "..." + wallet.address.takeLast(4)
            Text(
                text = shortAddress,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = AlataFont)
            )
        }

        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Remover carteira",
                tint = Color.Red
            )
        }
    }
}