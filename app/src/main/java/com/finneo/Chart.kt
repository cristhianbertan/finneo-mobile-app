package com.finneo

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finneo.ui.theme.WhiteColor
import kotlin.math.PI
import kotlin.math.atan2

data class PieChartInput(
    val color: Color,
    val value: Int,
    val description: String,
    val isTapped: Boolean = false
)

class AdvancedChartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Carteira Fiduciária",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhiteColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 30.dp)
                )
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    PieChart(
                        modifier = Modifier.size(400.dp),
                        input = listOf(
                            PieChartInput(
                                color = Color(0xFF304D8C),
                                value = 33,
                                description = "Ações"
                            ),
                            PieChartInput(
                                color = Color(0xFF8D5353),
                                value = 33,
                                description = "FIIs"
                            ),
                            PieChartInput(
                                color = Color(0xFF43A047),
                                value = 33,
                                description = "Poupança"
                            )
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    radius: Float = 500f,
    innerRadius: Float = 250f,
    transparentWidth: Float = 70f,
    input: List<PieChartInput>,
    centerText: String = ""
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var inputList by remember {
        mutableStateOf(input)
    }
    var isCenterTapped by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { offset ->
                            val tapAngleInDegrees = (-atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat() - 90f).mod(360f)

                            val centerClicked = if (tapAngleInDegrees < 90) {
                                offset.x < circleCenter.x + innerRadius && offset.y < circleCenter.y + innerRadius
                            } else if (tapAngleInDegrees < 180) {
                                offset.x > circleCenter.x - innerRadius && offset.y < circleCenter.y + innerRadius
                            } else if (tapAngleInDegrees < 270) {
                                offset.x > circleCenter.x - innerRadius && offset.y > circleCenter.y - innerRadius
                            } else {
                                offset.x < circleCenter.x + innerRadius && offset.y > circleCenter.y - innerRadius
                            }

                            if (centerClicked) {
                                inputList = inputList.map {
                                    it.copy(isTapped = !isCenterTapped)
                                }
                                isCenterTapped = !isCenterTapped
                            } else {
                                val anglePerValue = 360f / input.sumOf { it.value }
                                var currAngle = 0f
                                inputList.forEach { pieChartInput ->
                                    currAngle += pieChartInput.value * anglePerValue
                                    if (tapAngleInDegrees < currAngle) {
                                        val description = pieChartInput.description
                                        inputList = inputList.map {
                                            if (description == it.description) {
                                                it.copy(isTapped = !it.isTapped)
                                            } else {
                                                it.copy(isTapped = false)
                                            }
                                        }
                                        return@detectTapGestures
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            val totalValue = input.sumOf { it.value }
            val anglePerValue = 360f / totalValue
            var currentStartAngle = 0f

            inputList.forEach { pieChartInput ->
                val angleToDraw = pieChartInput.value * anglePerValue

                drawArc(
                    color = pieChartInput.color,
                    startAngle = currentStartAngle,
                    sweepAngle = angleToDraw,
                    useCenter = true,
                    size = Size(
                        width = radius * 2f,
                        height = radius * 2f
                    ),
                    topLeft = Offset(
                        (width - radius * 2f) / 2f,
                        (height - radius * 2f) / 2f
                    )
                )
                currentStartAngle += angleToDraw

                val percentage = (pieChartInput.value / totalValue.toFloat() * 100).toInt()

                var rotateAngle = currentStartAngle - angleToDraw / 2f - 90f
                var factor = 1f

                if (percentage == 100) {
                    rotateAngle = 0f
                    factor = 1f
                } else if (rotateAngle > 90f) {
                    rotateAngle = (rotateAngle + 180).mod(360f)
                    factor = -0.92f
                }

                drawContext.canvas.nativeCanvas.apply {
                    if (percentage > 3) {
                        rotate(rotateAngle) {
                            drawText(
                                "$percentage %",
                                circleCenter.x,
                                circleCenter.y + (radius - (radius - innerRadius) / 2f) * factor,
                                Paint().apply {
                                    textSize = 13.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = WhiteColor.toArgb()
                                    isFakeBoldText = true
                                }
                            )
                        }
                    }
                }
            }

            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    circleCenter.x,
                    circleCenter.y,
                    innerRadius,
                    Paint().apply {
                        color = WhiteColor.copy(alpha = 0.6f).toArgb()
                        setShadowLayer(10f, 0f, 0f, WhiteColor.toArgb())
                    }
                )
            }
        }
    }
}