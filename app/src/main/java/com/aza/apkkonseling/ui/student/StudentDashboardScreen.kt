package com.aza.apkkonseling.ui.student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkBottomNavBar
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.BottomNavItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onNavigateToMenu: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showSosSheet by remember { mutableStateOf(false) }
    var sosType by remember { mutableStateOf("Perundungan (Bullying)") }
    var sosDescription by remember { mutableStateOf("") }
    var selectedQuickMood by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val initials = MockRepository.currentStudentName
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")
        .ifEmpty { "BK" }

    val firstName = MockRepository.currentStudentName.split(" ").firstOrNull() ?: "Siswa"

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // ==================================================
                // 1. COMPACT STUDENT GREETING HEADER (Above the fold)
                // ==================================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1D3A5B),
                                    Color(0xFF28507D),
                                    Color(0xFF3E6B9C)
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 18.dp)
                        .padding(top = 16.dp, bottom = 20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(IceBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = initials,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = DeepNavy
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Hai, $firstName",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = PureWhite
                                    )
                                    Text(
                                        text = "XII IPA 1 • RuangCerita",
                                        fontSize = 12.sp,
                                        color = SoftIceBlue
                                    )
                                }
                            }

                            // Pill Keluar button
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onLogout() },
                                color = PureWhite.copy(alpha = 0.22f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "Keluar",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Apa kabar perasaanmu hari ini?",
                            fontSize = 13.sp,
                            color = PureWhite.copy(alpha = 0.95f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ==================================================
                // 2. PROMINENT RED SOS EMERGENCY BANNER (ABOVE THE FOLD)
                // ==================================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showSosSheet = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SosRed),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF8585))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFD9383A),
                                        Color(0xFFB71C1C)
                                    )
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(PureWhite.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Darurat",
                                        tint = PureWhite,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "DARURAT / LAPOR SOS",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = PureWhite,
                                        letterSpacing = 0.5.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Bullying atau butuh bantuan cepat? Tekan di sini",
                                        fontSize = 11.sp,
                                        color = PureWhite.copy(alpha = 0.95f),
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // High-contrast white pill button with bold red text
                            Surface(
                                color = PureWhite,
                                shape = RoundedCornerShape(14.dp),
                                shadowElevation = 2.dp
                            ) {
                                Text(
                                    text = "LAPOR",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB71C1C)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ==================================================
                // 3. QUICK MOOD CHECK-IN WIDGET
                // ==================================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Catat mood hari ini",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onNavigateToMenu("student_mood_diary") },
                                color = IceBlue,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Rekap Bulanan ›",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val quickMoods = listOf(
                            Triple("😊", "Senang", "Senang"),
                            Triple("😐", "Biasa", "Biasa"),
                            Triple("😢", "Sedih", "Sedih"),
                            Triple("😡", "Kesal", "Marah")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            quickMoods.forEach { (emoji, label, repoLabel) ->
                                val isSelected = selectedQuickMood == label
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSelected) DenimBlue else Color(0xFFF1F5F9))
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) DeepNavyVariant else SoftSkyBlue.copy(alpha = 0.6f),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            selectedQuickMood = label
                                            MockRepository.addMoodEntry(emoji, repoLabel, "Mood dicatat cepat dari Beranda")
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Mood hari ini tersimpan: $label $emoji")
                                            }
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = emoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) PureWhite else DeepNavy
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ==================================================
                // 4. MAIN RUANG KONSELING & BENTO CARDS
                // ==================================================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Ruang Konseling Siswa",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // BENTO CARD 1: Featured Card Chat BK
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onNavigateToMenu("student_chat_teachers") },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DenimBlue),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = PureWhite.copy(alpha = 0.22f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Mode Anonim Tersedia",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SoftYellow
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Mulai Curhat dengan Guru BK",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = "Cerita secara rahasia atau terbuka kapan saja.",
                                    fontSize = 12.sp,
                                    color = PureWhite.copy(alpha = 0.92f),
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(PureWhite),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Chat,
                                    contentDescription = "Chat",
                                    tint = DenimBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BENTO ROW 2: Janji Temu & Minat Bakat (2 Columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Janji Temu Card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(138.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .clickable { onNavigateToMenu("student_appointment") },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(SoftYellow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = AmberAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Janji Temu",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DeepNavy
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Jadwal tatap muka",
                                        fontSize = 11.sp,
                                        color = NeutralGrey
                                    )
                                }
                            }
                        }

                        // Minat dan Bakat Card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(138.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .clickable { onNavigateToMenu("student_quiz") },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(IceBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = DenimBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Minat & Bakat",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DeepNavy
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Eksplorasi jurusan",
                                        fontSize = 11.sp,
                                        color = NeutralGrey
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BENTO CARD 3: Pojok Baca dan Self-Care
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onNavigateToMenu("student_articles") },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SoftIceBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Article,
                                    contentDescription = null,
                                    tint = DenimBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Perpustakaan Konseling",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DeepNavy
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Tips belajar, kesehatan mental & materi edukasi BK",
                                    fontSize = 11.sp,
                                    color = NeutralGrey
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = SoftSkyBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Generous bottom clearance above navigation bar
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }
    }

    // ==================================================
    // REDESIGNED EMERGENCY RED SOS MODAL BOTTOM SHEET
    // ==================================================
    if (showSosSheet) {
        val sosOptions = listOf(
            "Perundungan (Bullying)",
            "Krisis Mental/Emosional",
            "Kekerasan Fisik",
            "Darurat Lainnya"
        )

        ModalBottomSheet(
            onDismissRequest = { showSosSheet = false },
            containerColor = PureWhite,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with clear red warning icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SosRedLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = SosRed, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Laporan Darurat (SOS)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = SosRed
                            )
                        )
                        Text(
                            text = "Laporan langsung diprioritaskan untuk Guru BK",
                            style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pilih Kategori Masalah:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Problem categories with RED selection highlight (NO BLUE!)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sosOptions.forEach { option ->
                        val isSelected = (sosType == option)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { sosType = option },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFFDECEC) else PureWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) SosRed else DividerGrey
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { sosType = option },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = SosRed,
                                        unselectedColor = NeutralGrey
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) SosRed else DeepNavy
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Optional description field
                OutlinedTextField(
                    value = sosDescription,
                    onValueChange = { sosDescription = it },
                    label = { Text("Keterangan singkat / lokasi kejadian (Opsional)") },
                    placeholder = { Text("Contoh: Di area kantin belakang sekolah...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SosRed,
                        focusedLabelColor = SosRed
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Immediately active button whenever a category is chosen
                Button(
                    onClick = {
                        val desc = sosDescription.ifBlank { "Laporan darurat cepat (kategori: $sosType)" }
                        MockRepository.addSosReport(sosType, desc)
                        showSosSheet = false
                        sosDescription = ""
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Laporan SOS berhasil dikirim ke Guru BK!")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SosRed,
                        contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    enabled = true
                ) {
                    Text(
                        text = "KIRIM LAPORAN SOS SEKARANG",
                        color = PureWhite,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showSosSheet = false },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepNavy)
                ) {
                    Text("Batal", color = DeepNavy, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(name = "Siswa - Beranda Utama", showBackground = true, showSystemUi = true)
@Composable
fun StudentDashboardScreenPreview() {
    BkCounselingTheme {
        StudentDashboardScreen(
            onNavigateToMenu = {},
            onLogout = {}
        )
    }
}
