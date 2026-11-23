package com.aza.apkkonseling.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.theme.*

// ========================================================
// 1. Reusable Bottom Navigation Bar (Student & Admin)
// ========================================================
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun BkBottomNavBar(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, Color(0xFFDCE8F5)),
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxSize(),
            containerColor = PureWhite,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            onItemSelected(item.route)
                        }
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount > 0) {
                                    Badge(
                                        containerColor = SosRed,
                                        contentColor = PureWhite
                                    ) {
                                        Text(text = item.badgeCount.toString(), fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PureWhite,
                        selectedTextColor = DeepNavy,
                        indicatorColor = DenimBlue,
                        unselectedIconColor = SteelBlue.copy(alpha = 0.75f),
                        unselectedTextColor = SteelBlue.copy(alpha = 0.75f)
                    )
                )
            }
        }
    }
}

// ========================================================
// 2. Standardized Accessible Chips & Button Colors
// ========================================================

/**
 * Standard filter chip colors ensuring high contrast in both selected and unselected states:
 * - Selected: DenimBlue container with PureWhite text/icon
 * - Unselected: PureWhite container with DeepNavy readable text and SteelBlue icon
 */
@Composable
fun bkFilterChipColors() = FilterChipDefaults.filterChipColors(
    containerColor = PureWhite,
    labelColor = DeepNavy,
    iconColor = SteelBlue,
    selectedContainerColor = DenimBlue,
    selectedLabelColor = PureWhite,
    selectedLeadingIconColor = PureWhite,
    disabledContainerColor = Color(0xFFF1F5F9),
    disabledLabelColor = Color(0xFF94A3B8)
)

@Composable
fun bkFilterChipBorder(isSelected: Boolean) = FilterChipDefaults.filterChipBorder(
    enabled = true,
    selected = isSelected,
    borderColor = SoftSkyBlue,
    selectedBorderColor = DenimBlue,
    borderWidth = 1.dp,
    selectedBorderWidth = 1.dp
)

/**
 * Standard button colors ensuring disabled buttons have high contrast (never white on light gray).
 */
@Composable
fun bkButtonColors(containerColor: Color = DenimBlue) = ButtonDefaults.buttonColors(
    containerColor = containerColor,
    contentColor = PureWhite,
    disabledContainerColor = Color(0xFFD6E2EE),
    disabledContentColor = Color(0xFF6B8299)
)

@Composable
fun bkEmergencyButtonColors() = ButtonDefaults.buttonColors(
    containerColor = SosRed,
    contentColor = PureWhite,
    disabledContainerColor = Color(0xFFFCDCDC),
    disabledContentColor = Color(0xFFA86666)
)

// BkSnackbarHost and BkStatusBadge are defined in BkComponents.kt

// ========================================================
// 3. Reusable Confirm Delete Dialog
// ========================================================
@Composable
fun ConfirmDeleteDialog(
    visible: Boolean,
    title: String = "Konfirmasi Hapus",
    message: String = "Apakah Anda yakin ingin menghapus item ini? Tindakan ini tidak dapat dibatalkan.",
    confirmText: String = "Hapus",
    dismissText: String = "Batal",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(20.dp),
            containerColor = PureWhite,
            titleContentColor = DeepNavy,
            textContentColor = DeepNavyVariant,
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SosRedLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = SosRed,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = SosRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = confirmText, color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepNavy)
                ) {
                    Text(text = dismissText)
                }
            }
        )
    }
}

// ========================================================
// 4. Reusable Empty State View
// ========================================================
@Composable
fun EmptyStateView(
    icon: ImageVector,
    title: String,
    description: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(IceBlue.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DenimBlue,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DeepNavy
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(
                color = NeutralGrey
            ),
            textAlign = TextAlign.Center
        )
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = actionText, color = PureWhite, fontSize = 13.sp)
            }
        }
    }
}

@Preview(name = "Komponen Bersama (Shared)", showBackground = true)
@Composable
fun SharedComponentsPreview() {
    BkCounselingTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EmptyStateView(
                icon = Icons.Default.Info,
                title = "Data Kosong",
                description = "Tidak ada catatan yang tersedia saat ini."
            )
        }
    }
}


