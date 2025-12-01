package com.finneo

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finneo.ui.theme.AlataFont
import com.finneo.ui.theme.BrightGreen
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

// --- DATA CLASSES ---
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
    val titleNumberOfShares: String,
    val numberOfShares: String,
    val trendColor: Color,
)

data class PortfolioDetail(
    val percentage: String,
    val name: String,
    val value: String,
    val color: Color
)

// Função auxiliar interna para converter string monetária em Double dentro deste arquivo
private fun String.parseMoney(): Double {
    return this.replace("R$", "")
        .trim()
        .replace(".", "")
        .replace(",", ".")
        .toDoubleOrNull() ?: 0.0
}

@Composable
fun AssetCard(
    asset: Asset,
    isValueVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val valuationColor = if (asset.valuationLastDay.contains("-")) {
        Color(0xFFFF5757)
    } else {
        BrightGreen
    }

    val cornerShape = RoundedCornerShape(12.dp)

    Card(
        modifier = modifier
            .wrapContentHeight()
            .padding(4.dp)
            .border(1.dp, Color(0xFFE0E0E0), cornerShape),
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // --- LINHA 1: Ticker e Badge ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = asset.ticker,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Text(
                    text = asset.valuationLastDay,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .background(valuationColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // --- LINHA 2: Rendimento ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Rend. último ano",
                    fontSize = 9.sp,
                    color = Color.Gray,
                    lineHeight = 9.sp
                )
                Text(
                    text = asset.percentProfitPerYear,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }

            // --- LINHA 3: Setor e Valor em Carteira ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Setor
                Column {
                    Text(
                        text = asset.titleType,
                        fontSize = 9.sp,
                        color = Color.Gray,
                        lineHeight = 9.sp
                    )
                    Text(
                        text = asset.type,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Valor em carteira",
                        fontSize = 9.sp,
                        color = Color.Gray,
                        lineHeight = 9.sp
                    )
                    val valueText = if (isValueVisible) "R$\u00A0${asset.walletValue}" else "R$\u00A0*****"

                    Text(
                        text = valueText,
                        fontSize = 13.sp,
                        textAlign = TextAlign.End,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Setor
                Column {
                    Text(
                        text = asset.titlePrice,
                        fontSize = 9.sp,
                        color = Color.Gray,
                        lineHeight = 9.sp
                    )
                    Text(
                        text = asset.price,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = asset.titleNumberOfShares,
                        fontSize = 9.sp,
                        color = Color.Gray,
                        lineHeight = 9.sp
                    )
                    val valueText = if (isValueVisible) "\u00A0${asset.numberOfShares}" else "\u00A0*****"

                    Text(
                        text = valueText,
                        fontSize = 13.sp,
                        textAlign = TextAlign.End,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    detail: PortfolioDetail,
    subAssets: List<Asset>,
    isValueVisible: Boolean
) {
    var isExpanded by remember { mutableStateOf(false) }
    val cornerShape = RoundedCornerShape(8.dp)

    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "ArrowRotation"
    )

    // Calculamos o total do grupo (ex: Total de FIIs) para usar no cálculo da %
    val groupTotalValue = remember(detail.value) { detail.value.parseMoney() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(cornerShape)
            .background(Color.LightGray.copy(alpha = 0.2f), cornerShape)
    ) {
        DetailHeader(
            detail = detail,
            isExpanded = isExpanded,
            rotation = rotation,
            isValueVisible = isValueVisible,
            onClick = { isExpanded = !isExpanded }
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Header da Tabela (Título das colunas)
                DetailSubAssetHeader()
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

                // Linhas de Ativos
                subAssets.forEach { asset ->
                    DetailSubAssetRow(
                        asset = asset,
                        groupTotalValue = groupTotalValue, // Passamos o total do grupo
                        isValueVisible = isValueVisible
                    )
                }
            }
        }
    }
}


@Composable
fun DetailHeader(
    detail: PortfolioDetail,
    isExpanded: Boolean,
    rotation: Float,
    isValueVisible: Boolean,
    onClick: () -> Unit
) {
    val cornerShape = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(detail.color, if (isExpanded) RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp) else cornerShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Percentual e Nome
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
                val valueText = if (isValueVisible) "R$ ${detail.value}" else "R$ •••••••"
                Text(
                    text = valueText,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation) // Rotação da Seta
                )
            }
        }
    }
}

@Composable
fun DetailSubAssetHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(0.7f)) {
            Text("Perc. Equiv.", fontSize = 9.sp, color = Color.Gray, modifier = Modifier.weight(0.5f))
            Text("Título", fontSize = 9.sp, color = Color.Gray, modifier = Modifier.weight(0.5f))
        }

        Row(modifier = Modifier.weight(0.8f)) {
            Text("Cotação", fontSize = 9.sp, color = Color.Gray, modifier = Modifier.weight(1f))
            Text(
                "Total equivalente",
                fontSize = 9.sp,
                color = Color.Gray,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DetailSubAssetRow(
    asset: Asset,
    groupTotalValue: Double,
    isValueVisible: Boolean
) {
    // 1. Calcular a porcentagem deste ativo em relação ao grupo
    val assetValue = asset.walletValue.parseMoney()
    val percentage = if (groupTotalValue > 0) (assetValue / groupTotalValue) * 100 else 0.0
    val percentageText = "%.2f".format(percentage).replace(".", ",") + "%"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(0.7f)) {
            // Exibe a porcentagem calculada. NÃO ocultamos com isValueVisible.
            Text(
                text = percentageText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = asset.ticker,
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(0.5f)
            )
        }

        Row(modifier = Modifier.weight(0.8f)) {
            Text(
                text = asset.price,
                fontSize = 12.sp,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )
            // Aqui ocultamos o valor monetário se a visibilidade estiver desligada
            val totalEquivalentText = if (isValueVisible) "R$ ${asset.walletValue}" else "R$ ••••••"
            Text(
                text = totalEquivalentText,
                fontSize = 12.sp,
                color = Color.Black,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SimulatedPieChart(details: List<PortfolioDetail>, modifier: Modifier = Modifier) {
    val totalPercentage = details.sumOf {
        it.percentage.removeSuffix("%").replace(",", ".").toDoubleOrNull() ?: 0.0
    }

    val proportions = details.map {
        (it.percentage.removeSuffix("%").replace(",", ".").toDoubleOrNull() ?: 0.0) / totalPercentage
    }

    Box(modifier = modifier.size(228.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            val strokeWidth = size.minDimension * 0.25f
            val chartSize = size.minDimension - strokeWidth

            proportions.forEachIndexed { index, proportion ->
                val sweepAngle = (proportion * 360f).toFloat()

                drawArc(
                    color = details[index].color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(chartSize, chartSize),
                    style = Stroke(width = strokeWidth)
                )
                startAngle += sweepAngle
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -30f
            val strokeWidth = size.minDimension * 0.25f
            val textRadius = (size.minDimension / 2) - (strokeWidth / 2)

            val textPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 12.sp.toPx()
                typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                isAntiAlias = true
            }

            val baseVerticalOffset = (textPaint.descent() + textPaint.ascent()) / 2f

            proportions.forEachIndexed { index, proportion ->
                val sweepAngle = (proportion * 360f).toFloat()
                val midAngle = startAngle + sweepAngle / 2
                val angleInRadians = Math.toRadians(midAngle.toDouble())

                val x = (center.x + textRadius * cos(angleInRadians)).toFloat()
                val y = (center.y + textRadius * sin(angleInRadians)).toFloat()

                var normalizedAngle = midAngle % 360
                if (normalizedAngle < 0) normalizedAngle += 360

                val isBottomHalf = normalizedAngle > 90 && normalizedAngle < 270

                var finalRotationAngle = midAngle + 90f
                var finalYOffset = baseVerticalOffset

                if (isBottomHalf) {
                    finalRotationAngle += 180f
                    finalYOffset = -baseVerticalOffset * 0.8f
                }


                drawContext.canvas.nativeCanvas.save()

                drawContext.canvas.nativeCanvas.rotate(finalRotationAngle, x, y)

                drawContext.canvas.nativeCanvas.drawText(
                    details[index].percentage,
                    x,
                    y - finalYOffset, // Usa o offset corrigido
                    textPaint
                )

                drawContext.canvas.nativeCanvas.restore()

                startAngle += sweepAngle
            }
        }
    }
}

@Composable
fun ElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    content: @Composable () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = colors,
    ) {
        content()
    }
}

@Composable
fun SocialLoginButton(
    onClick: () -> Unit,
    text: String,
    iconId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    AlataFont: androidx.compose.ui.text.font.FontFamily
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.Gray),
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                style = TextStyle(
                    fontFamily = AlataFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
            )
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(
            fontFamily = AlataFont,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    AlataFont: androidx.compose.ui.text.font.FontFamily,
    isError: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector =
                        if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                )
            }
        },
        isError = isError,
        textStyle = TextStyle(
            fontFamily = AlataFont,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun DateOfBirthField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            onValueChange(selectedDate)
        },
        year, month, day
    )

    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        placeholder = { Text("DD/MM/AAAA") },
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clickable { datePickerDialog.show() },
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(
            fontFamily = AlataFont,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Selecionar Data"
                )
            }
        }
    )
}

class BrazilianPhoneVisualTransformation(private val maxDigits: Int = 11) : VisualTransformation {
    override fun filter(text: androidx.compose.ui.text.AnnotatedString): androidx.compose.ui.text.input.TransformedText {
        val originalText = text.text.filter { it.isDigit() }
        val trimmed = if (originalText.length > 11) originalText.substring(0, 11) else originalText

        val output = StringBuilder()
        trimmed.forEachIndexed { index, char ->
            when (index) {
                0 -> output.append("(")
                2 -> output.append(") ")
                7 -> output.append("-")
            }
            output.append(char)
        }

        val annotatedString = androidx.compose.ui.text.AnnotatedString(output.toString())
        val offsetTranslator = object : androidx.compose.ui.text.input.OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                var transformed = offset
                if (offset >= 1) transformed += 1
                if (offset >= 3) transformed += 2
                if (offset >= 8) transformed += 1
                return transformed
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return offset
                var original = offset
                if (offset >= 2) original -= 1
                if (offset >= 5) original -= 2
                if (offset >= 12) original -= 1
                return original.coerceIn(0, trimmed.length)
            }
        }
        return androidx.compose.ui.text.input.TransformedText(annotatedString, offsetTranslator)
    }
}

class CpfVisualTransformation(private val maxDigits: Int = 11) : VisualTransformation {
    override fun filter(text: androidx.compose.ui.text.AnnotatedString): androidx.compose.ui.text.input.TransformedText {
        val trimmed = if (text.text.length > maxDigits) text.text.substring(0, maxDigits) else text.text
        val output = StringBuilder()
        trimmed.forEachIndexed { index, char ->
            when (index) {
                2, 5 -> output.append(char).append(".")
                8 -> output.append(char).append("-")
                else -> output.append(char)
            }
        }
        val annotatedString = androidx.compose.ui.text.AnnotatedString(output.toString())
        val offsetTranslator = object : androidx.compose.ui.text.input.OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 8) return offset + 2
                if (offset <= 11) return offset + 3
                return 14
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset - 1
                if (offset <= 11) return offset - 2
                return offset - 3
            }
        }
        return androidx.compose.ui.text.input.TransformedText(annotatedString, offsetTranslator)
    }
}

fun isValidCpf(cpf: String): Boolean {
    val digits = cpf.filter { it.isDigit() }
    if (digits.length != 11) return false
    if (digits.all { it == digits.first() }) return false
    return true
}

@Composable
fun PhoneNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val countryCode by remember { mutableStateOf("+55") }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val digitsOnly = newValue.filter { it.isDigit() }
            val trimmedDigits =
                if (digitsOnly.length > 11) digitsOnly.substring(0, 11) else digitsOnly
            onValueChange(trimmedDigits)
        },
        label = { Text("Telefone") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = BrazilianPhoneVisualTransformation(maxDigits = 11),
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = AlataFont,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(8.dp),
        leadingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 12.dp, end = 4.dp)
                    .clickable { /* Ação para abrir o seletor de país (DropdownMenu) */ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.br_flag),
                    contentDescription = "Bandeira do País",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "($countryCode)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(8.dp))

                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    )
}

@Composable
fun CpfField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val digitsOnly = newValue.filter { it.isDigit() }
            val trimmedDigits =
                if (digitsOnly.length > 11) digitsOnly.substring(0, 11) else digitsOnly
            onValueChange(trimmedDigits)
        },
        label = { Text("CPF") },
        placeholder = { Text("___.___.___-__") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = CpfVisualTransformation(maxDigits = 11),
        singleLine = true,
        isError = isError,
        textStyle = TextStyle(
            fontFamily = AlataFont,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val genderOptions = listOf("Feminino", "Masculino", "Não Binário", "Prefiro não informar")
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = value,
            onValueChange = {},
            textStyle = TextStyle(
                fontFamily = AlataFont,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            shape = RoundedCornerShape(8.dp),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            genderOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onValueChange(selectionOption)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}