package com.aza.apkkonseling.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.InsertEmoticon
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSelectableChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMoodDiaryMonitorScreen(
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Rekap Umum, 1 = Per Siswa
    var periodFilter by remember { mutableStateOf("Hari Ini") } // "Hari Ini" | "Minggu Ini"

    // Tab 2 Per Siswa States
    var selectedStudentName by remember { mutableStateOf(MockRepository.currentStudentName) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredStudents = MockRepository.students.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.className.contains(searchQuery, ignoreCase = true)
    }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Monitoring Mood & Diary Siswa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DeepNavy
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
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
                Text(
                    text = "Pantau Kesejahteraan Emosional Siswa",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Lakukan pemantauan agregat kondisi psikologis maupun telaah rekam emosi personal siswa secara mendalam.",
                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 12.sp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 2 Full-Width Segmented Tabs
                val tabTitles = listOf("Rekap Umum", "Per Siswa")
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = PureWhite,
                    contentColor = DenimBlue,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) DenimBlue else NeutralGrey,
                                    fontSize = 13.sp
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTab == 0) {
                    // ==================================================
                    // TAB 1: REKAP UMUM (DEFAULT)
                    // ==================================================
                    // Period Filter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter Periode Analisis:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Hari Ini", "Minggu Ini").forEach { p ->
                                BkSelectableChip(
                                    selected = periodFilter == p,
                                    onClick = { periodFilter = p },
                                    label = p
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Breakdown Data Definition
                    val isToday = periodFilter == "Hari Ini"
                    val happyCount = if (isToday) 28 else 142
                    val neutralCount = if (isToday) 14 else 68
                    val sadCount = if (isToday) 5 else 22
                    val angryCount = if (isToday) 2 else 9
                    val tiredCount = if (isToday) 7 else 31
                    val totalEntries = happyCount + neutralCount + sadCount + angryCount + tiredCount

                    // 1. Mood Breakdown Cards
                    Text(
                        text = "Ringkasan Suasana Hati ($periodFilter)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val colorSenang = Color(0xFF23476E)
                    val colorBiasa = Color(0xFF8BB4DC)
                    val colorSedih = Color(0xFF5A7D9A)
                    val colorMarah = Color(0xFFD9383A)
                    val colorLelah = Color(0xFFF4B841)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MoodSummaryPill(modifier = Modifier.weight(1f), emoji = "😊", label = "Senang", count = happyCount, percent = (happyCount * 100 / totalEntries), accent = colorSenang)
                        MoodSummaryPill(modifier = Modifier.weight(1f), emoji = "😐", label = "Biasa", count = neutralCount, percent = (neutralCount * 100 / totalEntries), accent = colorBiasa)
                        MoodSummaryPill(modifier = Modifier.weight(1f), emoji = "😔", label = "Sedih", count = sadCount, percent = (sadCount * 100 / totalEntries), accent = colorSedih)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        MoodSummaryPill(modifier = Modifier.weight(1f), emoji = "😡", label = "Marah", count = angryCount, percent = (angryCount * 100 / totalEntries), accent = colorMarah)
                        MoodSummaryPill(modifier = Modifier.weight(1f), emoji = "😫", label = "Lelah", count = tiredCount, percent = (tiredCount * 100 / totalEntries), accent = colorLelah)
                        Spacer(modifier = Modifier.weight(0.5f))
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 2. Visual Distribution Graph (Canvas Bar)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Distribusi Proporsi Emosi Siswa",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Text(
                                text = "Total $totalEntries catatan terekam pada periode ini",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Stacked Proportion Bar via Canvas
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                val w = size.width
                                val h = size.height
                                val happyW = (happyCount.toFloat() / totalEntries) * w
                                val neutralW = (neutralCount.toFloat() / totalEntries) * w
                                val sadW = (sadCount.toFloat() / totalEntries) * w
                                val angryW = (angryCount.toFloat() / totalEntries) * w
                                val tiredW = w - (happyW + neutralW + sadW + angryW)

                                var curX = 0f
                                // Happy - DeepNavy
                                drawRect(color = colorSenang, topLeft = Offset(curX, 0f), size = Size(happyW, h))
                                curX += happyW
                                // Neutral - SkyBlue
                                drawRect(color = colorBiasa, topLeft = Offset(curX, 0f), size = Size(neutralW, h))
                                curX += neutralW
                                // Sad - Slate/SteelBlue
                                drawRect(color = colorSedih, topLeft = Offset(curX, 0f), size = Size(sadW, h))
                                curX += sadW
                                // Angry - Emergency Red
                                drawRect(color = colorMarah, topLeft = Offset(curX, 0f), size = Size(angryW, h))
                                curX += angryW
                                // Tired - Amber / WarmYellow
                                drawRect(color = colorLelah, topLeft = Offset(curX, 0f), size = Size(tiredW, h))
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Legend Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LegendItem(color = colorSenang, label = "Senang")
                                LegendItem(color = colorBiasa, label = "Biasa")
                                LegendItem(color = colorSedih, label = "Sedih")
                                LegendItem(color = colorMarah, label = "Marah")
                                LegendItem(color = colorLelah, label = "Lelah")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3. Peringatan Siswa Butuh Perhatian
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SosRed.copy(alpha = 0.35f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SosRedLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = SosRed, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Siswa Membutuhkan Perhatian Khusus",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Siswa teridentifikasi melaporkan emosi negatif berulang (Sedih / Marah / Lelah) selama 3 hari berturut-turut.",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            val alertStudents = listOf(
                                Triple("Dewi Lestari", "XI IPS 2", "Merasa sangat cemas dan kelelahan dengan beban tugas dan ujian."),
                                Triple("Budi Pratama", "X-B", "Kehilangan motivasi dan sering merasa sedih karena masalah personal.")
                            )

                            alertStudents.forEach { (name, cls, note) ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = SoftIceBlue),
                                    border = BorderStroke(0.5.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "$name ($cls)",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "\"$note\"",
                                                style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant, fontSize = 11.sp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            color = SosRedLight,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "Perlu Respon",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.bodySmall.copy(color = SosRed, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // ==================================================
                    // TAB 2: PER SISWA
                    // ==================================================
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama atau kelas siswa...", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DenimBlue) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = PureWhite,
                            focusedContainerColor = PureWhite,
                            unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                            focusedBorderColor = DenimBlue
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Pilih Siswa (disertai mood terbaru):",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filteredStudents) { stu ->
                            val isSelected = selectedStudentName == stu.name
                            val latestMood = MockRepository.moodEntries.firstOrNull { it.studentName == stu.name }?.moodEmoji ?: "😊"

                            BkSelectableChip(
                                selected = isSelected,
                                onClick = { selectedStudentName = stu.name },
                                label = "${stu.name} (${stu.className})",
                                trailingEmoji = latestMood
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Mood Overview Card for Selected Student
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Riwayat Mood: $selectedStudentName",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Text(
                                text = "Rekapitulasi catatan suasana hati harian & mingguan",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 12.sp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            val studentMoods = MockRepository.moodEntries.filter { it.studentName == selectedStudentName }

                            // Weekly summary bar
                            Surface(
                                color = SoftIceBlue,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Total Entri", fontSize = 11.sp, color = NeutralGrey)
                                        Text("${studentMoods.size} Hari", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Mood Dominan", fontSize = 11.sp, color = NeutralGrey)
                                        val dominant = studentMoods.groupBy { it.moodLabel }.maxByOrNull { it.value.size }?.key ?: "Positif"
                                        Text(dominant, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DenimBlue)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Status Emosi", fontSize = 11.sp, color = NeutralGrey)
                                        Text("Stabil", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (studentMoods.isEmpty()) {
                                Surface(
                                    color = Color(0xFFF8FAFC),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Belum ada entri catatan mood dari $selectedStudentName.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey),
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            } else {
                                studentMoods.forEach { mood ->
                                    Surface(
                                        color = Color(0xFFF8FAFC),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(mood.moodEmoji, fontSize = 28.sp)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(mood.moodLabel, fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 13.sp)
                                                    Text(mood.date, style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp))
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "Catatan: \"${mood.note}\"",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant, fontSize = 12.sp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Diary Monitoring Card for Selected Student
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Buku Harian & Catatan Refleksi",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Text(
                                text = "Tulisan diary yang dibagikan siswa untuk pendampingan konseling",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 12.sp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            val studentDiaries = MockRepository.diaryEntries.filter { it.studentName == selectedStudentName }

                            if (studentDiaries.isEmpty()) {
                                Surface(
                                    color = Color(0xFFF8FAFC),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Tidak ada catatan buku harian dari $selectedStudentName.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey),
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            } else {
                                studentDiaries.forEach { diary ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = SoftIceBlue),
                                        border = BorderStroke(0.5.dp, SoftSkyBlue.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(diary.title, fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 13.sp)
                                                Text(diary.date, style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 10.sp))
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(diary.content, style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant, fontSize = 12.sp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun MoodSummaryPill(
    modifier: Modifier = Modifier,
    emoji: String,
    label: String,
    count: Int,
    percent: Int,
    accent: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 11.sp))
            Spacer(modifier = Modifier.height(2.dp))
            Text("$count Siswa", style = MaterialTheme.typography.bodySmall.copy(color = accent, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp))
            Text("$percent%", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 10.sp))
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = DeepNavy, fontWeight = FontWeight.Medium)
    }
}

@Preview(name = "Admin - Monitoring Mood Siswa", showBackground = true, showSystemUi = true)
@Composable
fun AdminMoodDiaryMonitorScreenPreview() {
    BkCounselingTheme {
        AdminMoodDiaryMonitorScreen(onNavigateBack = {})
    }
}
