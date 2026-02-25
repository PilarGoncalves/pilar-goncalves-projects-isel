package tds.reversi.reversicompose.ui.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

/**
 * Composable function that creates an animated background with moving lines.
 *
 * The background consists of lines that move in a circular motion. The lines' positions are calculated
 * based on a continuously rotating angle, creating a dynamic effect that repeats infinitely.
 *
 * The `rememberInfiniteTransition` is used to animate the angle over time, while `Canvas` is used to draw
 * the moving lines on the screen.
 */
@Composable
fun AnimatedBackground() {
    val transition = rememberInfiniteTransition()

    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val radians = Math.toRadians(angle.toDouble())

    val shiftX = (cos(radians) * 50).toFloat()
    val shiftY = (sin(radians) * 50).toFloat()

    Canvas(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF28214E))
    ) {
        val spacing = 40f

        for (x in 0..size.width.toInt() step spacing.toInt()) {
            drawLine(
                Color.White.copy(alpha = 0.05f),
                Offset(x + shiftX, 0f),
                Offset(x + shiftX, size.height)
            )
        }

        for (y in 0..size.height.toInt() step spacing.toInt()) {
            drawLine(
                Color.White.copy(alpha = 0.05f),
                Offset(0f, y + shiftY),
                Offset(size.width, y + shiftY)
            )
        }
    }
}