package com.aza.apkkonseling.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Visual, Color & Background Guidelines
val DeepNavy = Color(0xFF1D3A5B)
val DeepNavyVariant = Color(0xFF23476E)
val DenimBlue = Color(0xFF3E6B9C)
val SteelBlue = Color(0xFF4A7CB0)
val SkyBlue = Color(0xFF6F9FD0)
val SoftSkyBlue = Color(0xFF8BB4DC)
val IceBlue = Color(0xFFC4DDF2)
val SoftIceBlue = Color(0xFFD6E7F5)

// Neutral Colors
val PureWhite = Color(0xFFFFFFFF)
val WarmCream = Color(0xFFFAF5E8)
val WarmCreamDarker = Color(0xFFEFE6D5)
val NeutralGrey = Color(0xFF6B7280)
val LightGrey = Color(0xFFF3F4F6)
val DividerGrey = Color(0xFFE5E7EB)

// Accent Colors
val SoftYellow = Color(0xFFFCE7A2)
val GoldenYellow = Color(0xFFF4B841)
val WarmYellow = Color(0xFFF4B841)
val AmberAccent = Color(0xFFE59835)

// SOS Dedicated Colors
val SosRed = Color(0xFFD9383A)
val SosRedLight = Color(0xFFFFEBEB)

// Status Colors (NO GREEN as per guidelines: use DenimBlue/Navy for confirmed/success)
val StatusSuccess = Color(0xFF3E6B9C)       // Denim Blue
val StatusSuccessLight = Color(0xFFC4DDF2)  // Ice Blue container
val StatusWarning = Color(0xFFE59835)       // Amber / Warm Yellow
val StatusWarningLight = Color(0xFFFCE7A2)  // Soft Yellow container
val StatusError = Color(0xFFD9383A)         // Sos Red
val StatusErrorLight = Color(0xFFFFEBEB)

// Smooth Vertical Gradient Background
val MainBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFFFFF), // White at top
        Color(0xFFE3EEF8), // Ice Blue
        Color(0xFF9EC0E2), // Soft Sky Blue
        Color(0xFF5B8EC5)  // Medium Blue at bottom
    )
)

val CardSurfaceGradient = Brush.verticalGradient(
    colors = listOf(
        PureWhite,
        WarmCream
    )
)
