package com.finneo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.finneo.ui.theme.AlataFont
import com.finneo.ui.theme.BrightGreen
import com.finneo.ui.theme.DarkGreen
import com.finneo.ui.theme.LeagueSpartanFont
import com.finneo.viewmodel.WalletViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWalletScreen(
    navController: NavController,
    onAddWalletClick: (String) -> Unit = {}
) {
    val walletViewModel: WalletViewModel = viewModel()
    val allWallets by walletViewModel.allWallets

    var searchText by remember { mutableStateOf("") }
    var walletInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        walletViewModel.fetchAllWallets()
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
                    IconButton(onClick = { /* Ação do menu */ }) {
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
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(25))
                                .background(Color.Transparent)
                                .clickable {
                                    navController.popBackStack()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Fiduciário",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 16.sp),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(25))
                                .background(BrightGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Criptomoedas",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 16.sp),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    Box(modifier = Modifier.size(48.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = {
                    Text(
                        "Pesquisar serviços",
                        color = Color.Gray,
                        fontFamily = AlataFont
                    )
                },
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Black)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(40.dp))

            InstructionItem(
                number = "1",
                text = "Adicione uma carteira de criptomoedas do padrão EVM (Ethereum Virtual Machine). Os endereços possuem prefixo “0x”."
            )

            Spacer(modifier = Modifier.height(24.dp))

            InstructionItem(
                number = "2",
                text = "Posteriormente, você pode adicionar mais de uma carteira. Porém, é necessário ao menos um endereço para que possam ser sincronizados os dados e os resultados."
            )

            Spacer(modifier = Modifier.height(24.dp))

            InstructionItem(
                number = "3",
                text = "Serão exibidos os dados e os resultados de todas as carteiras que forem cadastradas."
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Endereço da carteira",
                style = TextStyle(
                    fontFamily = LeagueSpartanFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = walletInput,
                onValueChange = { walletInput = it },
                placeholder = { Text("0x...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    onAddWalletClick(walletInput)
                    walletInput = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp), spotColor = BrightGreen),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = BrightGreen,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Adicionar carteira",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (allWallets.isNotEmpty()) {
                Text(
                    text = "Carteiras Salvas (${allWallets.size})",
                    style = TextStyle(
                        fontFamily = AlataFont,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                allWallets.forEach { wallet ->
                    WalletItemRow(
                        wallet = wallet,
                        onRemoveClick = { walletViewModel.removeWallet(wallet.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }


            val footerText = buildAnnotatedString {
                append("Acesse ")
                withStyle(style = SpanStyle(color = DarkGreen, fontWeight = FontWeight.Bold)) {
                    append("Perguntas frequentes")
                }
                append("\npara mais informações.")
            }

            Text(
                text = footerText,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = AlataFont,
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                modifier = Modifier.clickable { }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}