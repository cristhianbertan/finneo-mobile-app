package com.finneo

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

// --- CORES E FONTES GLOBAIS ---
val DarkGreen = Color(0xFF025B2F)
val BrightGreen = Color(0xFF00C749)
val LightBackground = Color(0xFFEEEEEE)
val AlataFont = FontFamily.SansSerif
val LeagueSpartanFont = FontFamily(Font(R.font.league_spartan))

// --- DATA CLASSES COMPARTILHADAS ---
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

// --- COMPONENTES FINANCEIROS (MOVIDOS PARA CÁ) ---

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
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = asset.titleType, fontSize = 8.sp, color = Color.Gray)
                Text(text = asset.type, fontSize = 14.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Column {
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

// --- SEUS COMPONENTES DE LOGIN/CADASTRO ORIGINAIS ---

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
    AlataFont: androidx.compose.ui.text.font.FontFamily
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                "Senha",
                style = TextStyle(
                    fontFamily = AlataFont,
                    fontSize = 16.sp
                )
            )
        },
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