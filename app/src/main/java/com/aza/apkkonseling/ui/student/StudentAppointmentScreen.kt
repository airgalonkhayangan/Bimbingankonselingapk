package com.aza.apkkonseling.ui.student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.BkStatusBadge
import com.aza.apkkonseling.ui.components.EmptyStateView
import com.aza.apkkonseling.ui.components.bkButtonColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentAppointmentScreen(
    onNavigateBack: () -> Unit
) {
    var selectedTeacherName by remember { mutableStateOf(MockRepository.teachers.first().name) }
    var selectedMode by remember { mutableStateOf("Offline (Ruang BK)") }
    var selectedDate by remember { mutableStateOf("2026-11-25") }
    var selectedTimeSlot by remember { mutableStateOf("08:00 - 09:00") }
    var reasonInput by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val availableDates = listOf(
        Pair("2026-11-25", "Sen, 25 Nov"),
        Pair("2026-11-26", "Sel, 26 Nov"),
        Pair("2026-11-27", "Rab, 27 Nov"),
        Pair("2026-11-28", "Kam, 28 Nov")
    )
    val availableSlots = listOf("08:00 - 09:00", "09:30 - 10:30", "11:00 - 12:00", "13:30 - 14:30")

    var selectedTabIndex by remember { mutableStateOf(0) } // 0 = Ajukan Jadwal, 1 = Riwayat Pengajuan

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Janji Konseling BK", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DeepNavy) },
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
            ) {
                // Segmented Tab Row
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = PureWhite,
                    contentColor = DenimBlue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, SoftSkyBlue.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Text(
                                "Ajukan Jadwal",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (selectedTabIndex == 0) DeepNavy else SteelBlue
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Text(
                                "Riwayat Pengajuan",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (selectedTabIndex == 1) DeepNavy else SteelBlue
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTabIndex == 0) {
                    // ==========================================
                    // FORM: SEPARATE SECTION CARDS (Mobile-friendly)
                    // ==========================================
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // SECTION 1: Pilih Guru Pembimbing BK
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "1. Pilih Guru Pembimbing BK:",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                MockRepository.teachers.forEach { teacher ->
                                    val isSelected = (selectedTeacherName == teacher.name)
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedTeacherName = teacher.name },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) Color(0xFFEEF5FC) else Color(0xFFF8FAFC)
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) DenimBlue else DividerGrey
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { selectedTeacherName = teacher.name },
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = DenimBlue,
                                                    unselectedColor = NeutralGrey
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = teacher.name,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                    color = DeepNavy,
                                                    fontSize = 13.sp
                                                )
                                                Text(
                                                    text = teacher.specialization,
                                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                                )
                                            }
                                            Surface(
                                                color = if (teacher.status.contains("Online")) SoftIceBlue else WarmCream,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = teacher.status,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (teacher.status.contains("Online")) DenimBlue else AmberAccent
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // SECTION 2: Metode Konseling (HIGH CONTRAST BUTTONS)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "2. Metode Konseling:",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val isOffline = selectedMode == "Offline (Ruang BK)"
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedMode = "Offline (Ruang BK)" },
                                        color = if (isOffline) DenimBlue else PureWhite,
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isOffline) DenimBlue else SoftSkyBlue
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize().padding(horizontal = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Place,
                                                contentDescription = null,
                                                tint = if (isOffline) PureWhite else DenimBlue,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Tatap Muka (Offline)",
                                                fontSize = 11.sp,
                                                fontWeight = if (isOffline) FontWeight.Bold else FontWeight.SemiBold,
                                                color = if (isOffline) PureWhite else DeepNavy,
                                                maxLines = 1
                                            )
                                        }
                                    }

                                    val isOnline = selectedMode == "Online (Chat BK)"
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedMode = "Online (Chat BK)" },
                                        color = if (isOnline) DenimBlue else PureWhite,
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isOnline) DenimBlue else SoftSkyBlue
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize().padding(horizontal = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Videocam,
                                                contentDescription = null,
                                                tint = if (isOnline) PureWhite else DenimBlue,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Daring (Online)",
                                                fontSize = 11.sp,
                                                fontWeight = if (isOnline) FontWeight.Bold else FontWeight.SemiBold,
                                                color = if (isOnline) PureWhite else DeepNavy,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // SECTION 3: Tanggal & Waktu Sesi (HIGH CONTRAST CHIPS)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "3. Pilih Tanggal & Waktu Sesi:",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Pilih Hari:", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp))
                                Spacer(modifier = Modifier.height(6.dp))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(availableDates) { (dateVal, dateLabel) ->
                                        val isSelected = selectedDate == dateVal
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { selectedDate = dateVal },
                                            color = if (isSelected) DenimBlue else PureWhite,
                                            shape = RoundedCornerShape(12.dp),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (isSelected) DenimBlue else SoftSkyBlue
                                            )
                                        ) {
                                            Text(
                                                text = dateLabel,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                color = if (isSelected) PureWhite else DeepNavy
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Pilih Jam Konseling:", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp))
                                Spacer(modifier = Modifier.height(6.dp))

                                // 2-Column Grid for Time Slots
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    for (i in availableSlots.indices step 2) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            val slot1 = availableSlots[i]
                                            val isSel1 = selectedTimeSlot == slot1
                                            Surface(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(42.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .clickable { selectedTimeSlot = slot1 },
                                                color = if (isSel1) DenimBlue else PureWhite,
                                                shape = RoundedCornerShape(10.dp),
                                                border = androidx.compose.foundation.BorderStroke(
                                                    1.dp,
                                                    if (isSel1) DenimBlue else SoftSkyBlue
                                                )
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = slot1,
                                                        fontSize = 12.sp,
                                                        fontWeight = if (isSel1) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (isSel1) PureWhite else DeepNavy
                                                    )
                                                }
                                            }

                                            if (i + 1 < availableSlots.size) {
                                                val slot2 = availableSlots[i + 1]
                                                val isSel2 = selectedTimeSlot == slot2
                                                Surface(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                    .height(42.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .clickable { selectedTimeSlot = slot2 },
                                                    color = if (isSel2) DenimBlue else PureWhite,
                                                    shape = RoundedCornerShape(10.dp),
                                                    border = androidx.compose.foundation.BorderStroke(
                                                        1.dp,
                                                        if (isSel2) DenimBlue else SoftSkyBlue
                                                    )
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Text(
                                                            text = slot2,
                                                            fontSize = 12.sp,
                                                            fontWeight = if (isSel2) FontWeight.Bold else FontWeight.Medium,
                                                            color = if (isSel2) PureWhite else DeepNavy
                                                        )
                                                    }
                                                }
                                            } else {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // SECTION 4: Alasan & Tombol Ajukan
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "4. Alasan / Topik Konseling:",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = reasonInput,
                                    onValueChange = { reasonInput = it },
                                    placeholder = { Text("Ceritakan singkat apa yang ingin dikonsultasikan...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    maxLines = 5,
                                    shape = RoundedCornerShape(14.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (reasonInput.isNotBlank()) {
                                            MockRepository.requestAppointment(
                                                teacherName = selectedTeacherName,
                                                mode = selectedMode,
                                                date = selectedDate,
                                                timeSlot = selectedTimeSlot,
                                                reason = reasonInput
                                            )
                                            reasonInput = ""
                                            selectedTabIndex = 1
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Pengajuan janji konseling berhasil dikirim!")
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = bkButtonColors(DenimBlue),
                                    enabled = reasonInput.isNotBlank()
                                ) {
                                    Text("Kirim Pengajuan Konseling", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))
                    }
                } else {
                    // ==========================================
                    // TAB 1: RIWAYAT PENGAJUAN (FIXED BADGE COLLISION)
                    // ==========================================
                    if (MockRepository.appointments.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.EventBusy,
                            title = "Belum Ada Pengajuan",
                            description = "Anda belum pernah mengajukan jadwal konseling.",
                            actionText = "Ajukan Sekarang",
                            onActionClick = { selectedTabIndex = 0 }
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 28.dp)
                        ) {
                            items(MockRepository.appointments) { item ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        // Top Row: Teacher Name with weight(1f) and Status Badge without collision
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = item.teacherName,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = DeepNavy,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            BkStatusBadge(status = item.status)
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Jadwal: ${item.date} • ${item.timeSlot}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                        )
                                        Text(
                                            text = "Metode: ${item.mode}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Surface(
                                            color = Color(0xFFF8FAFC),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                                        ) {
                                            Text(
                                                text = "Topik: \"${item.reason}\"",
                                                style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavy, fontSize = 12.sp),
                                                modifier = Modifier.padding(10.dp)
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
}

@Preview(name = "Siswa - Janji Konseling", showBackground = true, showSystemUi = true)
@Composable
fun StudentAppointmentScreenPreview() {
    BkCounselingTheme {
        StudentAppointmentScreen(onNavigateBack = {})
    }
}
