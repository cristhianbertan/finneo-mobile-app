package com.finneo

import AlataFont
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility
import java.util.Calendar

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
    // Este parâmetro garante que a fonte AlataFont seja conhecida
    AlataFont: androidx.compose.ui.text.font.FontFamily
) {
    // Gerenciamento do estado interno para mostrar/ocultar a senha
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        // 🚨 1. Label
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

        // 🚨 2. Estilo e Altura
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp), // Altura padronizada
        shape = RoundedCornerShape(8.dp),

        // 🚨 3. Lógica de Ocultar/Mostrar
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),

        // 🚨 4. Ícone de Ação (Olho)
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

        // 🚨 5. Estilo do Texto de Entrada (para manter a consistência da fonte)
        textStyle = TextStyle(
            fontFamily = AlataFont,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),

        // 🚨 6. Teclado (para garantir que o teclado não seja numérico por padrão)
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

                return original.coerceIn(
                    0,
                    trimmed.length
                )
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
        modifier = modifier
            .fillMaxWidth(),
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
    val genderOptions = listOf(
        "Feminino",
        "Masculino",
        "Não Binário",
        "Prefiro não informar"
    )

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
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
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