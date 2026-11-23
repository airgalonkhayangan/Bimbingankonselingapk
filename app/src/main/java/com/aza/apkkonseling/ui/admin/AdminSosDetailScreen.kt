package com.aza.apkkonseling.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSelectableChip
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.BkStatusBadge
import com.aza.apkkonseling.ui.components.bkButtonColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSosDetailScreen(
    sosId: String,
    onNavigateBack: () -> Unit
) {
    val report = MockRepository.sosReports.firstOrNull { it.id == sosId }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    if (report == null) {
        BkGradientBackground {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = { Text("Detail Kasus SOS", fontWeight = FontWeight.Bold, color = DeepNavy) },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Laporan SOS tidak ditemukan.", color = DeepNavy, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        return
    }

    var progressPercent by remember { mutableFloatStateOf(report.progressPercent.toFloat()) }
    var progressNotes by remember { mutableStateOf(report.progressNotes) }

    // Final result fields
    var actionsTaken by remember { mutableStateOf(report.actionsTaken) }
    var partiesInvolved by remember { mutableStateOf(report.partiesInvolved) }
    var finalResolution by remember { mutableStateOf(report.finalResolution) }
    var monitoringRecommendation by remember { mutableStateOf(report.monitoringRecommendation) }
    var completedDate by remember { mutableStateOf(if (report.completedDate.isNotBlank()) report.completedDate else "22 Nov 2026") }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Penanganan & Hasil Kasus SOS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DeepNavy
                            )
                            Text(
                                text = "ID: ${report.id} • ${report.type}",
                                fontSize = 12.sp,
                                color = SteelBlue
                            )
                        }
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
                Spacer(modifier = Modifier.height(8.dp))

                // Card 1: Identitas & Isi Laporan SOS
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
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(SosRedLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = SosRed, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = report.studentName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                    )
                                    Text(
                                        text = "NISN: ${report.nisn} • Kelas: ${report.className}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                    )
                                }
                            }
                            BkStatusBadge(status = report.status)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = IceBlue.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Kategori: ${report.type}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SosRed)
                        )
                        Text(
                            text = "Waktu Laporan: ${report.timestamp}",
                            style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Keterangan / Laporan Siswa:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = SoftIceBlue,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = report.description,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavy)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card 2: Update Progres Penyelesaian (0 - 100%)
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
                                text = "Progres Penanganan Kasus",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                            )
                            Surface(
                                color = if (progressPercent.toInt() >= 100) IceBlue else WarmYellow.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "${progressPercent.toInt()}% Selesai",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (progressPercent.toInt() >= 100) DenimBlue else DeepNavy,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stylized Progress Bar
                        LinearProgressIndicator(
                            progress = { progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = DenimBlue,
                            trackColor = SoftIceBlue,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Pilih Preset Progres Cepat:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(25, 50, 75, 100).forEach { preset ->
                                val isSelected = progressPercent.toInt() == preset
                                BkSelectableChip(
                                    selected = isSelected,
                                    onClick = { progressPercent = preset.toFloat() },
                                    label = "$preset%"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Custom Slider
                        Slider(
                            value = progressPercent,
                            onValueChange = { progressPercent = it },
                            valueRange = 0f..100f,
                            steps = 19,
                            colors = SliderDefaults.colors(
                                thumbColor = DenimBlue,
                                activeTrackColor = DenimBlue,
                                inactiveTrackColor = SoftIceBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Catatan Tindak Lanjut / Progres Lapangan:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = progressNotes,
                            onValueChange = { progressNotes = it },
                            placeholder = { Text("Contoh: Telah memanggil orang tua siswa dan melakukan konseling awal...", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = PureWhite,
                                focusedContainerColor = PureWhite,
                                unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                                focusedBorderColor = DenimBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                MockRepository.updateSosProgress(report.id, progressPercent.toInt(), progressNotes)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Progres penanganan berhasil diperbarui (${progressPercent.toInt()}%)")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = bkButtonColors(DenimBlue)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simpan Progres Penanganan", fontWeight = FontWeight.Bold, color = PureWhite)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card 3: Hasil Akhir Kasus SOS (Active when 100% or Selesai, or toggleable)
                val isCaseCompleted = progressPercent.toInt() >= 100 || report.status == "Selesai"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, if (isCaseCompleted) DenimBlue.copy(alpha = 0.5f) else SoftSkyBlue.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = if (isCaseCompleted) DenimBlue else NeutralGrey,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Laporan Hasil Akhir Kasus SOS",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCaseCompleted) DeepNavy else NeutralGrey
                                )
                            )
                        }

                        if (!isCaseCompleted) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Bagian ini dapat diisi lengkap ketika progres mencapai 100% atau status kasus dinyatakan selesai.",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 12.sp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tindakan yang Telah Diambil
                        Text(
                            text = "Tindakan yang Telah Diambil:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = actionsTaken,
                            onValueChange = { actionsTaken = it },
                            placeholder = { Text("Contoh: Mediasi langsung, pendampingan psikologis, surat komitmen...", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = PureWhite,
                                focusedContainerColor = PureWhite,
                                unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                                focusedBorderColor = DenimBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Pihak yang Dilibatkan
                        Text(
                            text = "Pihak yang Dilibatkan:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = partiesInvolved,
                            onValueChange = { partiesInvolved = it },
                            placeholder = { Text("Contoh: Guru BK, Wali Kelas, Orang Tua Siswa, Kepala Sekolah", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = PureWhite,
                                focusedContainerColor = PureWhite,
                                unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                                focusedBorderColor = DenimBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Kesimpulan / Hasil Akhir Kasus
                        Text(
                            text = "Kesimpulan / Hasil Akhir Kasus:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = finalResolution,
                            onValueChange = { finalResolution = it },
                            placeholder = { Text("Contoh: Kedua pihak saling memaafkan dan menandatangani pakta damai...", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = PureWhite,
                                focusedContainerColor = PureWhite,
                                unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                                focusedBorderColor = DenimBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Rekomendasi Pemantauan
                        Text(
                            text = "Rekomendasi Pemantauan Pasca-Kasus:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = monitoringRecommendation,
                            onValueChange = { monitoringRecommendation = it },
                            placeholder = { Text("Contoh: Pemantauan mingguan oleh wali kelas selama 1 bulan...", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = PureWhite,
                                focusedContainerColor = PureWhite,
                                unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                                focusedBorderColor = DenimBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tanggal Penyelesaian
                        Text(
                            text = "Tanggal Penyelesaian Kasus:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = completedDate,
                            onValueChange = { completedDate = it },
                            placeholder = { Text("Contoh: 22 Nov 2026", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = PureWhite,
                                focusedContainerColor = PureWhite,
                                unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                                focusedBorderColor = DenimBlue
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                MockRepository.saveSosFinalReport(
                                    id = report.id,
                                    actionsTaken = actionsTaken,
                                    partiesInvolved = partiesInvolved,
                                    finalResolution = finalResolution,
                                    monitoringRecommendation = monitoringRecommendation,
                                    completedDate = completedDate
                                )
                                progressPercent = 100f
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Laporan hasil akhir kasus SOS berhasil disimpan!")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = bkButtonColors(DenimBlue)
                        ) {
                            Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simpan Laporan Hasil Akhir", fontWeight = FontWeight.Bold, color = PureWhite)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Preview(name = "Admin - Detail Kasus SOS", showBackground = true, showSystemUi = true)
@Composable
fun AdminSosDetailScreenPreview() {
    BkCounselingTheme {
        AdminSosDetailScreen(
            sosId = MockRepository.sosReports.firstOrNull()?.id ?: "sos_1",
            onNavigateBack = {}
        )
    }
}
