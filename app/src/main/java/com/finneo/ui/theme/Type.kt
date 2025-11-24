import com.finneo.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AlataFont = FontFamily(
    Font(R.font.alata_regular)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = AlataFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = AlataFont
    ),
    headlineSmall = TextStyle(
        fontFamily = AlataFont
    ),
    labelLarge = TextStyle(
        fontFamily = AlataFont
    )
)