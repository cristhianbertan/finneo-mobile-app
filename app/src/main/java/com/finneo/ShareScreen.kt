package com.finneo

import com.finneo.ShareSelectionButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.finneo.ui.theme.AlataFont
import com.finneo.ui.theme.DarkGreen
import com.finneo.ui.theme.BrightGreen
import com.finneo.ui.theme.LeagueSpartanFont

sealed class ShareOption {
    object Fiduciary : ShareOption()
    object Crypto : ShareOption()
    object Both : ShareOption()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(
    navController: NavController,
    onShareProceed: (ShareOption) -> Unit
) {
    var isFiduciarySelected by remember { mutableStateOf(false) }
    var isCryptoSelected by remember { mutableStateOf(false) }


    val currentShareOption = remember(isFiduciarySelected, isCryptoSelected) {
        when {
            isFiduciarySelected && isCryptoSelected -> ShareOption.Both
            isFiduciarySelected -> ShareOption.Fiduciary
            isCryptoSelected -> ShareOption.Crypto
            else -> null // Nenhuma seleção
        }
    }

    val textColor = MaterialTheme.colorScheme.onSurface

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Compartilhamento de Carteiras",
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(DarkGreen, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
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

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Qual carteira deseja compartilhar?",
                style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .height(40.dp)
                    .background(DarkGreen, RoundedCornerShape(25))
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Opção Fiduciário
                ShareSelectionButton(
                    text = "Fiduciário",
                    isSelected = isFiduciarySelected,
                    onClick = { isFiduciarySelected = !isFiduciarySelected }
                )

                // Opção Criptomoedas
                ShareSelectionButton(
                    text = "Criptomoedas",
                    isSelected = isCryptoSelected,
                    onClick = { isCryptoSelected = !isCryptoSelected }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            InstructionItem(number = "1", text = "Selecione acima quais carteiras deseja compartilhar.")
            Spacer(modifier = Modifier.height(16.dp))
            InstructionItem(number = "2", text = "Vá em “Compartilhar Carteiras” para gerar o QR Code com suas carteiras selecionadas.")
            Spacer(modifier = Modifier.height(16.dp))
            InstructionItem(number = "3", text = "Compartilhe o QR Code para quem deseja mostrar suas carteiras de ativos.")

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f)),
                border = null
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.stat_sys_warning),
                        contentDescription = "Aviso de segurança",
                        tint = Color(0xFFFF5757),
                        modifier = Modifier.padding(end = 8.dp).size(24.dp)
                    )
                    Text(
                        text = "Por medida de segurança e proteção de dados sigilosos, somente as porcentagens da composição dos seus ativos em carteira serão compartilhados, e não os valores totais em carteira.",
                        style = TextStyle(
                            fontFamily = LeagueSpartanFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            ElevatedButton(
                onClick = {
                    currentShareOption?.let(onShareProceed)
                },
                enabled = currentShareOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = BrightGreen,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    "Compartilhar carteira",
                    style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Acesse Perguntas frequentes para mais informações.",
                style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, color = DarkGreen),
                modifier = Modifier.clickable { /* Ação de FAQ */ }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InstructionItem(number: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(BrightGreen, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.Black,
                style = TextStyle(
                    fontFamily = AlataFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = TextStyle(
                fontFamily = LeagueSpartanFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                lineHeight = 22.sp
            ),
            modifier = Modifier.weight(1f)
        )
    }
}