package com.aza.apkkonseling.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private val LightColorScheme = lightColorScheme(
    primary = DenimBlue,
    onPrimary = PureWhite,
    primaryContainer = IceBlue,
    onPrimaryContainer = DeepNavy,
    secondary = SteelBlue,
    onSecondary = PureWhite,
    secondaryContainer = SoftIceBlue,
    onSecondaryContainer = DeepNavyVariant,
    tertiary = GoldenYellow,
    onTertiary = DeepNavy,
    background = PureWhite,
    onBackground = DeepNavy,
    surface = PureWhite,
    onSurface = DeepNavy,
    surfaceVariant = WarmCream,
    onSurfaceVariant = DeepNavyVariant,
    error = SosRed,
    onError = PureWhite
)

@Composable
fun BkCounselingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Standard gradient background container applied across the counseling application.
 * Gradients from White (top) -> Ice Blue -> Soft Sky Blue -> Medium Blue (bottom).
 */
@Composable
fun BkGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = MainBackgroundGradient),
        content = content
    )
}
