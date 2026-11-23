package com.aza.apkkonseling.ui.student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.DiaryEntry
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.ConfirmDeleteDialog
import com.aza.apkkonseling.ui.components.EmptyStateView
import com.aza.apkkonseling.ui.components.bkButtonColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMoodDiaryScreen(
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Mood Board, 1 = Buku Harian

    // Mood Board State
    val moodChoices = listOf(
        Pair("😊", "Senang"),
        Pair("😐", "Biasa"),
        Pair("😢", "Sedih"),
        Pair("😡", "Marah"),
        Pair("😴", "Lelah")
    )
    var selectedMood by remember { mutableStateOf(moodChoices[0]) }
    var moodNoteInput by remember { mutableStateOf("") }
    var summaryFilter by remember { mutableStateOf("Mingguan") } // "Mingguan" or "Bulanan"

    // Diary State
    var showAddDiaryDialog by remember { mutableStateOf(false) }
    var editingDiaryEntry by remember { mutableStateOf<DiaryEntry?>(null) }
    var diaryTitleInput by remember { mutableStateOf("") }
    var diaryContentInput by remember { mutableStateOf("") }
    var diaryToDelete by remember { mutableStateOf<DiaryEntry?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Mood & Buku Harian",
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
            // Removed duplicate bottom-right FAB! Keeping single clean "+ Tulis Baru" in Diary tab
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Segmented Tab Row (Mood Board vs Buku Harian)
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = PureWhite,
                    contentColor = DenimBlue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, SoftSkyBlue.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Mood Board",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (selectedTab == 0) DeepNavy else SteelBlue
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Buku Harian",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (selectedTab == 1) DeepNavy else SteelBlue
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    // ==========================================
                    // TAB 1: MOOD BOARD
                    // ==========================================
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Card 1: Input Mood Hari Ini
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Bagaimana suasana hatimu?",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Pilih emoji yang paling menggambarkan harimu:",
                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Responsive 5 Emoji Tiles with high contrast
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    moodChoices.forEach { (emoji, label) ->
                                        val isSelected = selectedMood.first == emoji
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) DenimBlue else Color(0xFFF1F5F9))
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSelected) DeepNavyVariant else SoftSkyBlue.copy(alpha = 0.6f),
                                                    shape = RoundedCornerShape(14.dp)
                                                )
                                                .clickable { selectedMood = Pair(emoji, label) }
                                                .padding(vertical = 10.dp, horizontal = 2.dp)
                                        ) {
                                            Text(emoji, fontSize = 24.sp)
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                color = if (isSelected) PureWhite else DeepNavy,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = moodNoteInput,
                                    onValueChange = { moodNoteInput = it },
                                    label = { Text("Catatan suasana hati (opsional)", fontSize = 12.sp) },
                                    placeholder = { Text("Apa yang membuatmu merasa demikian?") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    maxLines = 3
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        MockRepository.addMoodEntry(
                                            emoji = selectedMood.first,
                                            label = selectedMood.second,
                                            note = moodNoteInput.ifBlank { "Hari yang biasa." }
                                        )
                                        moodNoteInput = ""
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Mood harianmu berhasil disimpan!")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = bkButtonColors(DenimBlue)
                                ) {
                                    Text("Simpan Mood Saya Hari Ini", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Card 2: Ringkasan Emosi Siswa (FIXED "Bul / ana / n" BUG)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Title and Subtitle clearly stacked on top
                                Text(
                                    text = "Ringkasan Emosi Siswa",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Pantau kestabilan emosi dan perkembangan suasana hatimu:",
                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Full-Width Segmented Control (Mingguan | Bulanan) - NEVER WRAPS
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFEEF5FC))
                                        .border(1.dp, SoftSkyBlue.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                        .padding(3.dp)
                                ) {
                                    val isMingguan = summaryFilter == "Mingguan"
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { summaryFilter = "Mingguan" },
                                        color = if (isMingguan) DenimBlue else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "Mingguan",
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            fontSize = 12.sp,
                                            fontWeight = if (isMingguan) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isMingguan) PureWhite else DeepNavy,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }

                                    val isBulanan = summaryFilter == "Bulanan"
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { summaryFilter = "Bulanan" },
                                        color = if (isBulanan) DenimBlue else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "Bulanan",
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            fontSize = 12.sp,
                                            fontWeight = if (isBulanan) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isBulanan) PureWhite else DeepNavy,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Differentiated View: Mingguan vs Bulanan
                                if (summaryFilter == "Mingguan") {
                                    Text(
                                        text = "Catatan 7 Hari Terakhir:",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = DeepNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Weekly Mini Calendar Bar (7 days)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
                                        val weekMoods = listOf("😊", "😐", "😴", "😊", "😊", "😐", "😊")
                                        days.forEachIndexed { idx, day ->
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(SoftIceBlue)
                                                    .padding(vertical = 8.dp, horizontal = 2.dp)
                                            ) {
                                                Text(day, fontSize = 10.sp, color = DeepNavy, fontWeight = FontWeight.SemiBold)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(weekMoods[idx], fontSize = 16.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Distribusi Mood Pekan Ini:",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = DeepNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😊 60%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DenimBlue)
                                            Text("Senang", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😐 25%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SteelBlue)
                                            Text("Biasa", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😴 10%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberAccent)
                                            Text("Lelah", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😢 5%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SosRed)
                                            Text("Sedih", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                    }
                                } else {
                                    // Bulanan Differentiated View
                                    Text(
                                        text = "Progres 4 Pekan Bulan Ini:",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = DeepNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // 4-Week Progress Cards
                                    val weeks = listOf(
                                        Triple("Pekan 1", "😊 Stabil", DenimBlue),
                                        Triple("Pekan 2", "😐 Ujian", SteelBlue),
                                        Triple("Pekan 3", "😊 Ceria", DenimBlue),
                                        Triple("Pekan 4", "😴 Padat", AmberAccent)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        weeks.forEach { (weekTitle, moodLabel, accentColor) ->
                                            Surface(
                                                modifier = Modifier.weight(1f),
                                                color = Color(0xFFF1F5F9),
                                                shape = RoundedCornerShape(10.dp),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(weekTitle, fontSize = 10.sp, color = NeutralGrey, fontWeight = FontWeight.Medium)
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(moodLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor, maxLines = 1)
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = "Distribusi Mood Kumulatif Bulan Ini:",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = DeepNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😊 55%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DenimBlue)
                                            Text("Senang", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😐 20%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SteelBlue)
                                            Text("Biasa", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😴 18%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberAccent)
                                            Text("Lelah", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("😢 7%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SosRed)
                                            Text("Sedih", fontSize = 11.sp, color = NeutralGrey)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Surface(
                                        color = SoftIceBlue,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Evaluasi: Kondisi emosional bulan ini relatif stabil dengan peningkatan rasa lelah di pekan keempat menjelang persiapan try-out.",
                                            modifier = Modifier.padding(10.dp),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = DeepNavy,
                                                lineHeight = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))
                    }
                } else {
                    // ==========================================
                    // TAB 2: BUKU HARIAN
                    // ==========================================
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Catatan Harian Anda",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepNavy
                                    )
                                )
                                Text(
                                    text = "Ruang privasi mencurahkan pikiran",
                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                )
                            }
                            // Single clean + Tulis Baru button
                            Button(
                                onClick = {
                                    diaryTitleInput = ""
                                    diaryContentInput = ""
                                    editingDiaryEntry = null
                                    showAddDiaryDialog = true
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = bkButtonColors(DenimBlue),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tulis Baru", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (MockRepository.diaryEntries.isEmpty()) {
                            EmptyStateView(
                                icon = Icons.Default.Book,
                                title = "Belum Ada Catatan Harian",
                                description = "Tuliskan apa saja yang kamu rasakan hari ini secara aman.",
                                actionText = "Mulai Menulis",
                                onActionClick = {
                                    diaryTitleInput = ""
                                    diaryContentInput = ""
                                    editingDiaryEntry = null
                                    showAddDiaryDialog = true
                                }
                            )
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                contentPadding = PaddingValues(bottom = 28.dp)
                            ) {
                                items(MockRepository.diaryEntries) { entry ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = WarmCream),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmCreamDarker)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = entry.title,
                                                    style = MaterialTheme.typography.titleSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = DeepNavy
                                                    ),
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Row {
                                                    IconButton(
                                                        onClick = {
                                                            editingDiaryEntry = entry
                                                            diaryTitleInput = entry.title
                                                            diaryContentInput = entry.content
                                                            showAddDiaryDialog = true
                                                        },
                                                        modifier = Modifier.size(36.dp)
                                                    ) {
                                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = SteelBlue, modifier = Modifier.size(18.dp))
                                                    }
                                                    IconButton(
                                                        onClick = { diaryToDelete = entry },
                                                        modifier = Modifier.size(36.dp)
                                                    ) {
                                                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = SosRed, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                            Text(
                                                text = "${entry.date} • ${entry.time}",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = NeutralGrey)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = entry.content,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, color = DeepNavyVariant)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Diary Dialog with roomy minLines = 5, maxLines = 8
    if (showAddDiaryDialog) {
        AlertDialog(
            onDismissRequest = { showAddDiaryDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = PureWhite,
            title = {
                Text(
                    text = if (editingDiaryEntry == null) "Tulis Catatan Harian" else "Edit Catatan Harian",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepNavy
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = diaryTitleInput,
                        onValueChange = { diaryTitleInput = it },
                        label = { Text("Judul Catatan", fontSize = 12.sp) },
                        placeholder = { Text("Contoh: Hari yang Melelahkan tapi Berharga") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = diaryContentInput,
                        onValueChange = { diaryContentInput = it },
                        label = { Text("Isi Buku Harian (Curahan Hati)", fontSize = 12.sp) },
                        placeholder = { Text("Tuliskan perasaanmu dengan bebas di sini...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5,
                        maxLines = 8,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (diaryTitleInput.isNotBlank() && diaryContentInput.isNotBlank()) {
                            if (editingDiaryEntry == null) {
                                MockRepository.addDiaryEntry(diaryTitleInput, diaryContentInput)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Catatan harian berhasil ditambahkan")
                                }
                            } else {
                                MockRepository.updateDiaryEntry(editingDiaryEntry!!.id, diaryTitleInput, diaryContentInput)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Catatan harian berhasil diperbarui")
                                }
                            }
                            showAddDiaryDialog = false
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(12.dp),
                    enabled = diaryTitleInput.isNotBlank() && diaryContentInput.isNotBlank()
                ) {
                    Text("Simpan", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDiaryDialog = false }) {
                    Text("Batal", color = DeepNavy)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        visible = diaryToDelete != null,
        title = "Hapus Catatan Harian",
        message = "Apakah Anda yakin ingin menghapus catatan \"${diaryToDelete?.title}\"? Tindakan ini tidak dapat dibatalkan.",
        onConfirm = {
            diaryToDelete?.let {
                MockRepository.deleteDiaryEntry(it.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Catatan harian telah dihapus")
                }
            }
            diaryToDelete = null
        },
        onDismiss = { diaryToDelete = null }
    )
}

@Preview(name = "Siswa - Mood & Diary", showBackground = true, showSystemUi = true)
@Composable
fun StudentMoodDiaryScreenPreview() {
    BkCounselingTheme {
        StudentMoodDiaryScreen(onNavigateBack = {})
    }
}
