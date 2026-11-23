package com.aza.apkkonseling.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkBottomNavBar
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.BkStatusBadge
import com.aza.apkkonseling.ui.components.BottomNavItem
import kotlinx.coroutines.launch

data class AdminMenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToMenu: (String) -> Unit,
    onLogout: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val adminMenuItems = listOf(
        AdminMenuItem(
            title = "Gelar Kasus (Case Conference)",
            description = "Sidang kasus kolaboratif lintas pihak, daftar hadir, dokumentasi & hasil",
            icon = Icons.Default.Groups,
            route = "admin_case_conferences"
        ),
        AdminMenuItem(
            title = "Rekam & Dokumen Konseling",
            description = "Catatan kasus terstandar, integrasi profil siswa & Audio-to-Text",
            icon = Icons.Default.Description,
            route = "admin_counseling_notes"
        ),
        AdminMenuItem(
            title = "Chat Masuk Siswa",
            description = "Tanggapi pesan masuk siswa (mode Nama Asli & Anonim)",
            icon = Icons.Default.ChatBubble,
            route = "admin_chat_list"
        ),
        AdminMenuItem(
            title = "Atur Jadwal & Persetujuan",
            description = "Kelola slot konseling & setujui/tolak permohonan siswa",
            icon = Icons.Default.EventAvailable,
            route = "admin_schedule_appointment"
        ),
        AdminMenuItem(
            title = "Data Siswa & Intake",
            description = "Kelola data siswa, intake, hasil tes psikologi & rekam prestasi",
            icon = Icons.Default.PersonSearch,
            route = "admin_students"
        ),
        AdminMenuItem(
            title = "Perpustakaan Konseling",
            description = "Kelola artikel edukasi, panduan bimbingan siswa & literasi BK",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            route = "admin_articles"
        ),
        AdminMenuItem(
            title = "Monitoring Mood & Diary",
            description = "Rekap suasana hati harian, grafik distribusi emosi & catatan siswa",
            icon = Icons.Default.InsertEmoticon,
            route = "admin_mood_diary_monitor"
        )
    )

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Panel Guru BK & Admin",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = DeepNavy
                            )
                            Text(
                                text = "Sistem Bimbingan & Konseling Terpadu",
                                fontSize = 12.sp,
                                color = SteelBlue
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Keluar",
                                tint = DeepNavy
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // Header Profil Guru
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(IceBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = MockRepository.currentTeacherName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Text(
                                text = "NIP: ${MockRepository.currentTeacherNip}",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Koordinator Bimbingan & Konseling",
                                style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==================================================
                // SOS PRIORITY NOTIFICATION AREA
                // ==================================================
                val activeSosReports = MockRepository.sosReports.filter { it.status != "Selesai" }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SosRed),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFFF8585))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFD9383A), Color(0xFFB71C1C))
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(PureWhite.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = PureWhite, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "NOTIFIKASI PRIORITAS SOS",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PureWhite,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                            Surface(
                                color = PureWhite,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "${activeSosReports.size} Kasus Aktif",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFB71C1C),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (activeSosReports.isEmpty()) {
                            Text(
                                "Semua laporan darurat SOS telah ditangani secara tuntas.",
                                style = MaterialTheme.typography.bodySmall.copy(color = PureWhite.copy(alpha = 0.95f))
                            )
                        } else {
                            activeSosReports.forEach { report ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            onNavigateToMenu("admin_sos_detail/${report.id}")
                                        },
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = report.studentName,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepNavy,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = " (${report.className})",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                                )
                                            }
                                            Text(
                                                text = "${report.type} • ${report.timestamp}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = SosRed, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                                            )
                                            Text(
                                                text = report.description,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant, fontSize = 11.sp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(horizontalAlignment = Alignment.End) {
                                            BkStatusBadge(status = report.status)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Surface(
                                                color = if (report.progressPercent >= 100) IceBlue else WarmYellow.copy(alpha = 0.35f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "Progres ${report.progressPercent}%",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (report.progressPercent >= 100) DenimBlue else DeepNavy
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ==================================================
                // 4-CARD 2-COLUMN STATISTICS GRID
                // ==================================================
                val totalStudents = MockRepository.getTotalStudentsCount()
                val totalCases = MockRepository.getTotalCasesCount()
                val completedCases = MockRepository.getCompletedCasesCount()
                val totalReports = MockRepository.getTotalReportsCount()
                val resolutionRate = if (totalCases > 0) (completedCases * 100 / totalCases) else 100

                Text(
                    text = "Statistik & Ringkasan Penanganan",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Siswa",
                        value = "$totalStudents",
                        subtitle = "Siswa Terdata",
                        icon = Icons.Default.Groups,
                        iconBg = IceBlue,
                        iconTint = DenimBlue
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Kasus",
                        value = "$totalCases",
                        subtitle = "Konseling & Kasus",
                        icon = Icons.Default.FolderShared,
                        iconBg = SoftSkyBlue.copy(alpha = 0.4f),
                        iconTint = SteelBlue
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Kasus Selesai",
                        value = "$completedCases",
                        subtitle = "$resolutionRate% Dituntaskan",
                        icon = Icons.Default.TaskAlt,
                        iconBg = SoftIceBlue,
                        iconTint = DenimBlue
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Laporan",
                        value = "$totalReports",
                        subtitle = "SOS & Gelar Kasus",
                        icon = Icons.Default.AssignmentLate,
                        iconBg = WarmYellow.copy(alpha = 0.25f),
                        iconTint = DeepNavy
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==================================================
                // MONTHLY COMPARISON BAR CHART (COMPOSE CANVAS)
                // STRICTLY NO GREEN: Denim Blue (Selesai) vs Amber (Dalam Proses)
                // ==================================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Rekap Kasus Selesai vs Proses",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Text(
                                text = "5 Bulan Terakhir",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Chart Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(DenimBlue)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Selesai", fontSize = 11.sp, color = DeepNavy, fontWeight = FontWeight.SemiBold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(WarmYellow)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Dalam Proses", fontSize = 11.sp, color = DeepNavy, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Compose Canvas Bar Chart
                        val stats = MockRepository.monthlyCaseStats
                        val maxStatValue = maxOf(20, stats.maxOfOrNull { maxOf(it.completed, it.inProgress) } ?: 20)

                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            val slotWidth = canvasWidth / stats.size
                            val barWidth = slotWidth * 0.28f
                            val barSpacing = 4.dp.toPx()
                            val cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())

                            // Draw horizontal light guideline
                            drawLine(
                                color = Color(0xFFE2E8F0),
                                start = Offset(0f, canvasHeight - 1.dp.toPx()),
                                end = Offset(canvasWidth, canvasHeight - 1.dp.toPx()),
                                strokeWidth = 1.dp.toPx()
                            )

                            stats.forEachIndexed { index, item ->
                                val groupCenterX = (index * slotWidth) + (slotWidth / 2)

                                // Bar 1: Kasus Selesai (Denim Blue)
                                val completedHeight = (item.completed.toFloat() / maxStatValue) * (canvasHeight - 16.dp.toPx())
                                val completedLeft = groupCenterX - barWidth - (barSpacing / 2)
                                val completedTop = canvasHeight - completedHeight

                                drawRoundRect(
                                    color = Color(0xFF3E6B9C),
                                    topLeft = Offset(completedLeft, completedTop),
                                    size = Size(barWidth, completedHeight),
                                    cornerRadius = cornerRadius
                                )

                                // Bar 2: Dalam Proses (Amber / Warm Yellow)
                                val inProgressHeight = (item.inProgress.toFloat() / maxStatValue) * (canvasHeight - 16.dp.toPx())
                                val inProgressLeft = groupCenterX + (barSpacing / 2)
                                val inProgressTop = canvasHeight - inProgressHeight

                                drawRoundRect(
                                    color = Color(0xFFF4B841),
                                    topLeft = Offset(inProgressLeft, inProgressTop),
                                    size = Size(barWidth, inProgressHeight),
                                    cornerRadius = cornerRadius
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Month Labels aligned below each group
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            stats.forEach { item ->
                                Text(
                                    text = item.month,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DeepNavy,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Menu Manajemen & Layanan Bimbingan",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Admin Menu Cards
                adminMenuItems.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigateToMenu(item.route) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(IceBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(item.icon, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                )
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SoftSkyBlue)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = DeepNavy, fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontSize = 10.sp, fontWeight = FontWeight.Medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(name = "Admin - Dashboard Utama", showBackground = true, showSystemUi = true)
@Composable
fun AdminDashboardScreenPreview() {
    BkCounselingTheme {
        AdminDashboardScreen(
            onNavigateToMenu = {},
            onLogout = {}
        )
    }
}
