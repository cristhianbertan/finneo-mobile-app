// Em ShareGenerationScreen.kt (ou no mesmo arquivo que contém o ShareScreen)

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.graphics.Bitmap
import android.graphics.Color as GColor
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.util.Log
import androidx.compose.ui.text.style.TextAlign

// (Reutilize a função generateQRCodeBitmap que forneci anteriormente)
fun generateQRCodeBitmap(text: String): Bitmap? {
    try {
        val size = 512
        val bits = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)

        // CORREÇÃO: Especificar Bitmap.Config.ARGB_8888 no createBitmap
        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    // CORREÇÃO: setPixel é um método que funciona diretamente na instância do Bitmap
                    setPixel(x, y, if (bits[x, y]) GColor.BLACK else GColor.WHITE)
                }
            }
        }
    } catch (e: Exception) {
        Log.e("QRCode", "Erro ao gerar QR Code: ${e.message}")
        return null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareGenerationScreen(
    navController: NavController,
    shareDataJson: String // Dados JSON decodificados da navegação
) {
    val uniqueLink = remember {
        if (shareDataJson.length < 5) {
            "Erro: Dados inválidos para QR Code"
        } else {
            "https://finneo.com/share/data?id=${shareDataJson.hashCode()}"
        }
    }

    // 2. Geração do QR Code
    val qrCodeBitmap by remember(uniqueLink) {
        mutableStateOf(generateQRCodeBitmap(uniqueLink))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerar Código de Compartilhamento") },
                navigationIcon = { /* Botão Voltar */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            if (qrCodeBitmap != null) {
                Image(
                    bitmap = qrCodeBitmap!!.asImageBitmap(),
                    contentDescription = "QR Code para Compartilhamento",
                    modifier = Modifier.size(300.dp)
                )
            } else {
                Text("Não foi possível gerar o QR Code.")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Texto informativo sobre o QR Code
            Text(
                "Este QR Code contém as proporções dos seus ativos. Ele pode ser lido por qualquer scanner.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botão nativo para salvar/compartilhar
            Button(onClick = {  }) {
                Text("Compartilhar Imagem")
            }
        }
    }
}