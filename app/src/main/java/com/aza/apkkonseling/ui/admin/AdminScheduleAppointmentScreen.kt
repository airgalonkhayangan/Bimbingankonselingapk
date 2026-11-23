package com.aza.apkkonseling.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.data.TeacherScheduleSlot
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSelectableChip
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.BkStatusBadge
import com.aza.apkkonseling.ui.components.ConfirmDeleteDialog
import com.aza.apkkonseling.ui.components.EmptyStateView
import com.aza.apkkonseling.ui.components.bkButtonColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScheduleAppointmentScreen(
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Pengajuan Siswa, 1 = Kelola Slot Jadwal Tersedia
    var showAddSlotDialog by remember { mutableStateOf(false) }
    var slotToDelete by remember { mutableStateOf<TeacherScheduleSlot?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var newSlotDay by remember { mutableStateOf("Senin") }
    var newSlotTime by remember { mutableStateOf("08:00 - 09:00") }
    var newSlotMode by remember { mutableStateOf("Offline") }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Jadwal & Persetujuan Konseling", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                    .padding(horizontal = 20.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = PureWhite,
                    contentColor = DenimBlue,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Pengajuan Masuk",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 0) DeepNavy else SteelBlue
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Atur Jadwal Tersedia",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == 1) DeepNavy else SteelBlue
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTab == 0) {
                    // Tab 0: Student Appointment Requests (Approve / Reject Action)
                    Text(
                        text = "Permohonan Janji Konseling Siswa:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (MockRepository.appointments.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.EventAvailable,
                            title = "Tidak Ada Permohonan",
                            description = "Belum ada pengajuan jadwal konseling yang masuk dari siswa."
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(MockRepository.appointments) { apt ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${apt.studentName} (${apt.nisn})",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                                modifier = Modifier.weight(1f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            BkStatusBadge(status = apt.status)
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Jadwal: ${apt.date} • ${apt.timeSlot} (${apt.mode})", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey))
                                        Text("Guru BK: ${apt.teacherName}", style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.Medium))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Surface(
                                            color = LightGrey,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Topik: \"${apt.reason}\"",
                                                style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavy),
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }

                                        if (apt.status == "Menunggu Konfirmasi") {
                                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = DividerGrey)
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                OutlinedButton(
                                                    onClick = {
                                                        MockRepository.updateAppointmentStatus(apt.id, "Ditolak")
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Pengajuan jadwal ditolak")
                                                        }
                                                    },
                                                    shape = RoundedCornerShape(12.dp),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(42.dp),
                                                    border = BorderStroke(1.dp, SosRed),
                                                    colors = ButtonDefaults.outlinedButtonColors(
                                                        containerColor = Color(0xFFFFF5F5),
                                                        contentColor = SosRed
                                                    )
                                                ) {
                                                    Text("Tolak", color = SosRed, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                                }

                                                Button(
                                                    onClick = {
                                                        MockRepository.updateAppointmentStatus(apt.id, "Disetujui")
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Pengajuan jadwal berhasil disetujui")
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                                                    shape = RoundedCornerShape(12.dp),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(42.dp)
                                                ) {
                                                    Text("Setujui", color = PureWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Tab 1: Available Schedule Slots Settings (CRUD)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Slot Konseling BK Tersedia:",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                        )
                        Button(
                            onClick = { showAddSlotDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DenimBlue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tambah Slot", color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (MockRepository.teacherScheduleSlots.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.EventBusy,
                            title = "Belum Ada Slot",
                            description = "Tambahkan slot jadwal konseling agar siswa dapat memilih waktu konsultasi.",
                            actionText = "Tambah Slot Sekarang",
                            onActionClick = { showAddSlotDialog = true }
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(MockRepository.teacherScheduleSlots) { slot ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "${slot.dayOfWeek}, ${slot.timeSlot}",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                            )
                                            Text(
                                                text = "Metode: ${slot.mode}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.SemiBold)
                                            )
                                        }
                                        IconButton(
                                            onClick = { slotToDelete = slot }
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Hapus Slot", tint = SosRed)
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

    // Add Slot Dialog
    if (showAddSlotDialog) {
        val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")
        val modes = listOf("Offline", "Online")

        AlertDialog(
            onDismissRequest = { showAddSlotDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = PureWhite,
            title = { Text("Tambah Slot Konseling BK", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = {
                Column {
                    Text("Pilih Hari:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, color = DeepNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(days) { day ->
                            val isSelected = newSlotDay == day
                            BkSelectableChip(
                                selected = isSelected,
                                onClick = { newSlotDay = day },
                                label = day
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = newSlotTime,
                        onValueChange = { newSlotTime = it },
                        label = { Text("Rentang Waktu (cth: 08:00 - 09:00)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Metode Konseling:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, color = DeepNavy)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        modes.forEach { md ->
                            val isSelected = newSlotMode == md
                            BkSelectableChip(
                                selected = isSelected,
                                onClick = { newSlotMode = md },
                                label = md
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSlotTime.isNotBlank()) {
                            MockRepository.addScheduleSlot(newSlotDay, newSlotTime, newSlotMode)
                            showAddSlotDialog = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Slot jadwal konseling berhasil ditambahkan")
                            }
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Simpan Slot", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSlotDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Delete Slot Confirmation Dialog
    ConfirmDeleteDialog(
        visible = slotToDelete != null,
        title = "Hapus Slot Konseling",
        message = "Apakah Anda yakin ingin menghapus slot jadwal ${slotToDelete?.dayOfWeek} (${slotToDelete?.timeSlot})?",
        onConfirm = {
            slotToDelete?.let {
                MockRepository.deleteScheduleSlot(it.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Slot jadwal telah dihapus")
                }
            }
            slotToDelete = null
        },
        onDismiss = { slotToDelete = null }
    )
}

@Preview(name = "Admin - Jadwal & Persetujuan Konseling", showBackground = true, showSystemUi = true)
@Composable
fun AdminScheduleAppointmentScreenPreview() {
    BkCounselingTheme {
        AdminScheduleAppointmentScreen(onNavigateBack = {})
    }
}
