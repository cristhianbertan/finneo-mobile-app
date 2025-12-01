package com.finneo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.finneo.ui.theme.AlataFont
import com.finneo.ui.theme.BrightGreen
import com.finneo.ui.theme.DarkGreen
import com.finneo.ui.theme.LeagueSpartanFont
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.finneo.ShareOption


@Composable
fun ShareOptionsPopup(
    initialSelection: Int, // 0 = Fiduciário, 1 = Cripto (Mantido para saber qual aba ativou)
    onDismiss: () -> Unit,
    onProceed: (ShareOption) -> Unit
) {
    var isFiduciarySelected by remember { mutableStateOf(initialSelection == 0) }
    var isCryptoSelected by remember { mutableStateOf(initialSelection == 1) }

    val currentShareOption = remember(isFiduciarySelected, isCryptoSelected) {
        when {
            isFiduciarySelected && isCryptoSelected -> ShareOption.Both
            isFiduciarySelected -> ShareOption.Fiduciary
            isCryptoSelected -> ShareOption.Crypto
            else -> null // Nenhuma seleção
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Opções de Compartilhamento",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = AlataFont,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SelectionButton(
                        text = "Fiduciário",
                        isSelected = isFiduciarySelected,
                        onClick = { isFiduciarySelected = !isFiduciarySelected }
                    )

                    SelectionButton(
                        text = "Criptomoedas",
                        isSelected = isCryptoSelected,
                        onClick = { isCryptoSelected = !isCryptoSelected }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                InstructionText(number = "1", text = "Selecione acima quais carteiras deseja compartilhar.")
                InstructionText(number = "2", text = "Vá em “Prosseguir” para gerar os dados de porcentagem e informações dos ativos.")
                InstructionText(number = "3", text = "Compartilhe o PDF/QR Code gerado para quem deseja mostrar suas carteiras de ativos.")

                Spacer(modifier = Modifier.height(24.dp))

                ElevatedButton(
                    // Só permite Prosseguir se alguma opção estiver selecionada
                    onClick = { currentShareOption?.let(onProceed) },
                    enabled = currentShareOption != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = DarkGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text("Prosseguir", style = TextStyle(fontFamily = AlataFont, fontSize = 16.sp, ))
                }
            }
        }
    }
}

@Composable
fun RowScope.SelectionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) BrightGreen else Color.Transparent)
            .clickable(onClick = onClick)
            .weight(1f)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else DarkGreen,
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 14.sp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InstructionText(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number - ",
            style = TextStyle(fontWeight = FontWeight.Bold, color = DarkGreen)
        )
        Text(
            text = text,
            style = TextStyle(fontFamily = LeagueSpartanFont, fontSize = 14.sp),
            modifier = Modifier.weight(1f)
        )
    }
}
