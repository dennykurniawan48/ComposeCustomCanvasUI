package com.signaltekno

import android.graphics.Paint
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
    indicatorStrokeWidth: Float = 100f,
    forgroundIndicatorColor: Color = MaterialTheme.colors.primary,
    forgroundStrokeWidth: Float = 50f
) {

    val applyValue = if(indicatorValue < maxIndicatorValue) indicatorValue else maxIndicatorValue

    var animatableIndicator by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = indicatorValue){
        animatableIndicator = applyValue.toFloat()
    }

    val percentage = (animatableIndicator / maxIndicatorValue) * 100

    val sweepAngle: Float by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000)
    )

    val colorAnim by animateColorAsState(
        targetValue = if (applyValue == 0) Color.Black.copy(alpha = 0.3f) else Color.Black,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = indicatorStrokeWidth
                )
                forgroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = forgroundIndicatorColor,
                    indicatorStrokeWidth = forgroundStrokeWidth
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Remaining", fontSize = MaterialTheme.typography.h5.fontSize, color = Color.Black.copy(alpha = 0.5f))
        Text(text = "$indicatorValue GB", fontSize = MaterialTheme.typography.h3.fontSize, color = colorAnim)
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
){
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(x= (size.width - componentSize.width) / 2f, y = (size.height - componentSize.height) / 2f)
    )
}

fun DrawScope.forgroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
){
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(x= (size.width - componentSize.width) / 2f, y = (size.height - componentSize.height) / 2f)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewIndicator() {
    CustomComponent()
}