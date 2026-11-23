package com.aza.apkkonseling.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.theme.*

/**
 * Standard selectable chip for mobile screens:
 * - selected = true: container = #3E6B9C (DenimBlue), text & icon = White
 * - selected = false: container = White, border = 1.dp #9BBCE0, text & icon = #1D3A5B (DeepNavy)
 * - Slim dimensions: height = 36.dp, shape = RoundedCornerShape(12.dp), horizontal padding = 14.dp, maxLines = 1
 */
@Composable
fun BkSelectableChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingEmoji: String? = null
) {
    val containerBg = if (selected) DenimBlue else PureWhite
    val contentColor = if (selected) PureWhite else DeepNavy
    val borderStroke = if (selected) null else BorderStroke(1.dp, Color(0xFF9BBCE0))

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = containerBg,
        border = borderStroke,
        shadowElevation = if (selected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                color = contentColor,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            if (trailingEmoji != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = trailingEmoji,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * Reusable Status Badge:
 * - Uses Modifier.wrapContentWidth()
 * - Padding: horizontal = 10.dp, vertical = 4.dp, RoundedCornerShape(50)
 * - Text: 11.sp SemiBold, maxLines = 1, softWrap = false
 * - Menunggu: bg #FCE7A2, text #8A5300
 * - Disetujui / Selesai: bg #D6E7F5, text #1D3A5B (STRICTLY NO GREEN)
 * - Ditolak: bg #FDECEC, text #D9383A
 */
@Composable
fun BkStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "menunggu", "menunggu konfirmasi", "perlu tindakan", "pending", "proses", "dalam proses" -> {
            Color(0xFFFCE7A2) to Color(0xFF8A5300)
        }
        "disetujui", "selesai", "dikonfirmasi", "sedang ditangani", "aktif", "approved" -> {
            Color(0xFFD6E7F5) to Color(0xFF1D3A5B)
        }
        "ditolak", "batal", "darurat", "rejected" -> {
            Color(0xFFFDECEC) to Color(0xFFD9383A)
        }
        else -> {
            Color(0xFFD6E7F5) to Color(0xFF1D3A5B)
        }
    }

    Box(
        modifier = modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            maxLines = 1,
            softWrap = false
        )
    }
}

/**
 * Reusable Floating Pill Card Snackbar Host:
 * - White container (#FFFFFF), 1.dp border #8BB4DC, 6.dp shadow, rounded corners 16.dp
 * - Text: #1D3A5B (13.sp, clear & readable) with #3E6B9C check/info icon on the left
 */
@Composable
fun BkSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) { data ->
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = PureWhite,
            border = BorderStroke(1.dp, Color(0xFF8BB4DC)),
            shadowElevation = 6.dp,
            modifier = Modifier.wrapContentWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = DenimBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = data.visuals.message,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepNavy
                )
                if (data.visuals.actionLabel != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { data.performAction() }) {
                        Text(
                            text = data.visuals.actionLabel!!,
                            color = DenimBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Komponen BK", showBackground = true)
@Composable
fun BkComponentsPreview() {
    BkCounselingTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BkSelectableChip(selected = true, onClick = {}, label = "Aktif")
                BkSelectableChip(selected = false, onClick = {}, label = "Nonaktif")
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BkStatusBadge(status = "Menunggu")
                BkStatusBadge(status = "Disetujui")
                BkStatusBadge(status = "Ditolak")
            }
        }
    }
}
