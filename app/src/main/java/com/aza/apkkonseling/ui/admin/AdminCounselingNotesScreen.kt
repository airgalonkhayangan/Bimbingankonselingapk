package com.aza.apkkonseling.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.CounselingNote
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSelectableChip
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.BkStatusBadge
import com.aza.apkkonseling.ui.components.ConfirmDeleteDialog
import com.aza.apkkonseling.ui.components.EmptyStateView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCounselingNotesScreen(
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDomainFilter by remember { mutableStateOf("Semua") }
    var showFormScreen by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<CounselingNote?>(null) }
    var noteToDelete by remember { mutableStateOf<CounselingNote?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Audio-to-Text Simulation State
    var simulatedTranscript by remember { mutableStateOf("") }
    var simulatedSummary by remember { mutableStateOf("") }
    var showAudioModal by remember { mutableStateOf(false) }

    // Form Fields
    var fStudentName by remember { mutableStateOf("") }
    var fNisn by remember { mutableStateOf("") }
    var fClassName by remember { mutableStateOf("") }
    var fAcademicYear by remember { mutableStateOf("2026/2027") }
    var fSemester by remember { mutableStateOf("Ganjil") } // "Ganjil" | "Genap"
    var fDate by remember { mutableStateOf("2026-11-22 09:00") }
    var fMode by remember { mutableStateOf("Offline") } // "Offline" | "Online"

    var fServiceDomain by remember { mutableStateOf("Pribadi") } // "Pribadi", "Sosial", "Belajar", "Karir"
    var fServiceType by remember { mutableStateOf("Konseling Individual") } // "Konseling Individual", "Konseling Kelompok", "Konsultasi"

    var fComplaint by remember { mutableStateOf("") }
    var fSymptoms by remember { mutableStateOf("") }
    var fBackgroundCause by remember { mutableStateOf("") }
    var fApproachTechnique by remember { mutableStateOf("") }
    var fSolutionOptions by remember { mutableStateOf("") }
    var fResult by remember { mutableStateOf("") }
    var fFollowUpPlan by remember { mutableStateOf("") }
    var fStatus by remember { mutableStateOf("Proses") } // "Proses", "Perlu Tindak Lanjut", "Selesai"

    fun populateForm(note: CounselingNote?) {
        if (note != null) {
            editingNote = note
            fStudentName = note.studentName
            fNisn = note.nisn
            fClassName = note.className
            fAcademicYear = note.academicYear
            fSemester = note.semester
            fDate = note.date
            fMode = note.mode
            fServiceDomain = note.serviceDomain
            fServiceType = note.serviceType
            fComplaint = note.complaint
            fSymptoms = note.symptoms
            fBackgroundCause = note.backgroundCause
            fApproachTechnique = note.approachTechnique
            fSolutionOptions = note.solutionOptions
            fResult = note.result
            fFollowUpPlan = note.followUpPlan
            fStatus = if (note.status.contains("Proses", ignoreCase = true)) "Proses" else note.status
        } else {
            editingNote = null
            val defaultStudent = MockRepository.students.first()
            fStudentName = defaultStudent.name
            fNisn = defaultStudent.nisn
            fClassName = defaultStudent.className
            fAcademicYear = "2026/2027"
            fSemester = "Ganjil"
            fDate = "2026-11-22 09:00"
            fMode = "Offline"
            fServiceDomain = "Pribadi"
            fServiceType = "Konseling Individual"
            fComplaint = ""
            fSymptoms = ""
            fBackgroundCause = ""
            fApproachTechnique = "Konseling Realitas & Wawancara Eksploratif"
            fSolutionOptions = ""
            fResult = ""
            fFollowUpPlan = ""
            fStatus = "Proses"
        }
    }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (showFormScreen) "Form Rekam Kasus BK" else "Dokumen & Rekam Konseling",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DeepNavy
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (showFormScreen) showFormScreen = false else onNavigateBack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                        }
                    },
                    actions = {
                        if (!showFormScreen) {
                            Button(
                                onClick = {
                                    populateForm(null)
                                    showFormScreen = true
                                },
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .height(36.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = PureWhite)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+ Catatan", fontSize = 13.sp, color = PureWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            if (!showFormScreen) {
                // ==========================================
                // LIST OF COUNSELING NOTES (Search & Filter)
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    // Full-width search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama siswa atau keluhan...", fontSize = 13.sp) },
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // Domain Filter Chips using BkSelectableChip
                    val domains = listOf("Semua", "Pribadi", "Sosial", "Belajar", "Karir")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(domains) { domain ->
                            val isSelected = selectedDomainFilter == domain
                            BkSelectableChip(
                                selected = isSelected,
                                onClick = { selectedDomainFilter = domain },
                                label = domain
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val filteredNotes = MockRepository.counselingNotes.filter { note ->
                        (selectedDomainFilter == "Semua" || note.serviceDomain == selectedDomainFilter) &&
                                (note.studentName.contains(searchQuery, ignoreCase = true) ||
                                        note.complaint.contains(searchQuery, ignoreCase = true))
                    }

                    if (filteredNotes.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.Description,
                            title = "Tidak Ada Dokumen",
                            description = "Belum ada dokumen konseling yang sesuai dengan kriteria pencarian.",
                            actionText = "Buat Catatan Baru",
                            onActionClick = {
                                populateForm(null)
                                showFormScreen = true
                            }
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 28.dp)
                        ) {
                            items(filteredNotes) { note ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        // Header Row: Student Name with weight(1f) and Edit/Delete buttons
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = note.studentName,
                                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "NISN: ${note.nisn} • ${note.className}",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                                )
                                            }
                                            Row {
                                                IconButton(
                                                    onClick = {
                                                        populateForm(note)
                                                        showFormScreen = true
                                                    },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = SteelBlue, modifier = Modifier.size(18.dp))
                                                }
                                                IconButton(
                                                    onClick = { noteToDelete = note },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = SosRed, modifier = Modifier.size(18.dp))
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(color = SoftIceBlue, shape = RoundedCornerShape(6.dp)) {
                                                Text(
                                                    text = "Bidang: ${note.serviceDomain}",
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.bodySmall.copy(color = DenimBlue, fontWeight = FontWeight.Bold)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(color = WarmCream, shape = RoundedCornerShape(6.dp)) {
                                                Text(
                                                    text = note.serviceType,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.bodySmall.copy(color = AmberAccent, fontWeight = FontWeight.Bold)
                                                )
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            BkStatusBadge(status = note.status)
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Surface(
                                            color = Color(0xFFF8FAFC),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Text(
                                                    text = "Keluhan: ${note.complaint}",
                                                    style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavy, fontSize = 12.sp),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "RTL: ${note.followUpPlan}",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = DenimBlue, fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Waktu: ${note.date} (${note.mode})",
                                            style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // ==========================================
                // STANDARDIZED COUNSELING FORM (Mobile First)
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Audio-to-Text & Summary Simulation Feature
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = SoftIceBlue),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, IceBlue)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Asisten Transkripsi & Resume Suara (AI)",
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Konversi rekaman percakapan konseling menjadi transkrip dan ringkasan poin inti secara otomatis.",
                                style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant, fontSize = 11.sp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        simulatedTranscript = "Guru: 'Ahmad, bagaimana perkembangan belajar menjelang ujian?' Siswa: 'Saya merasa bingung memilih antara teknik dan sains murni karena orang tua menginginkan saya masuk kedokteran.' Guru: 'Baik, mari kita petakan minat dan kemampuanmu lewat tes minat bakat.'"
                                        simulatedSummary = "Siswa mengalami dilema penjurusan karir akibat perbedaan ekspektasi dengan orang tua. Diperlukan konseling realitas dan eksplorasi minat bakat."
                                        showAudioModal = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                ) {
                                    Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp), tint = PureWhite)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Rekam Suara", fontSize = 13.sp, color = PureWhite, fontWeight = FontWeight.Bold, maxLines = 1)
                                }
                                OutlinedButton(
                                    onClick = {
                                        simulatedTranscript = "Rekaman audio konseling berhasil diunggah. Durasi: 24 menit 10 detik. Percakapan berfokus pada manajemen waktu dan penanganan stres ujian."
                                        simulatedSummary = "Masalah utama: Stres akademik karena penumpukan tugas sekolah. Solusi: Penerapan matriks Eisenhower."
                                        showAudioModal = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, DenimBlue),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = PureWhite),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                ) {
                                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp), tint = DenimBlue)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Upload Audio", fontSize = 13.sp, color = DenimBlue, fontWeight = FontWeight.Bold, maxLines = 1)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // SECTION 1: Identitas & Administrasi
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = IceBlue, shape = RoundedCornerShape(6.dp)) {
                                    Text("1", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 11.sp, color = DeepNavy, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Identitas & Info Administrasi",
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Pilih Siswa (Auto-isi NISN & Kelas):", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(MockRepository.students) { stu ->
                                    val isSelected = fStudentName == stu.name
                                    BkSelectableChip(
                                        selected = isSelected,
                                        onClick = {
                                            fStudentName = stu.name
                                            fNisn = stu.nisn
                                            fClassName = stu.className
                                        },
                                        label = "${stu.name} (${stu.className})"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fStudentName,
                                onValueChange = { fStudentName = it },
                                label = { Text("Nama Siswa", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = fNisn,
                                    onValueChange = { fNisn = it },
                                    label = { Text("NISN", fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = fClassName,
                                    onValueChange = { fClassName = it },
                                    label = { Text("Kelas", fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = fAcademicYear,
                                    onValueChange = { fAcademicYear = it },
                                    label = { Text("Tahun Ajaran", fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = fDate,
                                    onValueChange = { fDate = it },
                                    label = { Text("Tanggal & Jam", fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("Semester:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Ganjil", "Genap").forEach { sem ->
                                    BkSelectableChip(
                                        selected = fSemester == sem,
                                        onClick = { fSemester = sem },
                                        label = sem
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("Metode Konseling:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Offline", "Online").forEach { mtd ->
                                    BkSelectableChip(
                                        selected = fMode == mtd,
                                        onClick = { fMode = mtd },
                                        label = mtd
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // SECTION 2: Klasifikasi Layanan BK
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = IceBlue, shape = RoundedCornerShape(6.dp)) {
                                    Text("2", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 11.sp, color = DeepNavy, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Klasifikasi Layanan BK",
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Bidang Bimbingan:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                            Spacer(modifier = Modifier.height(6.dp))
                            val domains = listOf("Pribadi", "Sosial", "Belajar", "Karir")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(domains) { dom ->
                                    BkSelectableChip(
                                        selected = fServiceDomain == dom,
                                        onClick = { fServiceDomain = dom },
                                        label = dom
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Jenis Layanan:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                            Spacer(modifier = Modifier.height(6.dp))
                            val serviceTypes = listOf("Konseling Individual", "Konseling Kelompok", "Konsultasi")
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(serviceTypes) { st ->
                                    BkSelectableChip(
                                        selected = fServiceType == st,
                                        onClick = { fServiceType = st },
                                        label = st
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // SECTION 3: Isi Rekam Kasus & RTL
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = IceBlue, shape = RoundedCornerShape(6.dp)) {
                                    Text("3", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 11.sp, color = DeepNavy, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Isi Rekam Kasus & Tindak Lanjut (RTL)",
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fComplaint,
                                onValueChange = { fComplaint = it },
                                label = { Text("Keluhan / Masalah Siswa", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fSymptoms,
                                onValueChange = { fSymptoms = it },
                                label = { Text("Gejala / Indikasi yang Tampak", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fBackgroundCause,
                                onValueChange = { fBackgroundCause = it },
                                label = { Text("Latar Belakang / Akar Masalah", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fApproachTechnique,
                                onValueChange = { fApproachTechnique = it },
                                label = { Text("Teknik / Pendekatan Konseling", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fSolutionOptions,
                                onValueChange = { fSolutionOptions = it },
                                label = { Text("Alternatif Solusi & Kesepakatan", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fResult,
                                onValueChange = { fResult = it },
                                label = { Text("Hasil Evaluasi Sesi", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = fFollowUpPlan,
                                onValueChange = { fFollowUpPlan = it },
                                label = { Text("Rencana Tindak Lanjut (RTL)", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Status Kasus:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                            Spacer(modifier = Modifier.height(6.dp))
                            val statuses = listOf("Proses", "Perlu Tindak Lanjut", "Selesai")
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                statuses.forEach { st ->
                                    BkSelectableChip(
                                        selected = fStatus == st,
                                        onClick = { fStatus = st },
                                        label = st
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // HIGH-CONTRAST ACTION BUTTONS
                    Button(
                        onClick = {
                            val note = CounselingNote(
                                id = editingNote?.id ?: "CN-${System.currentTimeMillis() % 1000}",
                                studentName = fStudentName,
                                nisn = fNisn,
                                className = fClassName,
                                academicYear = fAcademicYear,
                                semester = fSemester,
                                date = fDate,
                                mode = fMode,
                                serviceDomain = fServiceDomain,
                                serviceType = fServiceType,
                                complaint = fComplaint,
                                symptoms = fSymptoms,
                                backgroundCause = fBackgroundCause,
                                approachTechnique = fApproachTechnique,
                                solutionOptions = fSolutionOptions,
                                result = fResult,
                                followUpPlan = fFollowUpPlan,
                                status = fStatus
                            )
                            if (editingNote != null) {
                                MockRepository.updateCounselingNote(note)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Dokumen konseling berhasil diperbarui")
                                }
                            } else {
                                MockRepository.addCounselingNote(note)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Dokumen konseling baru berhasil disimpan")
                                }
                            }
                            showFormScreen = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF23476E),
                            contentColor = PureWhite
                        ),
                        enabled = fStudentName.isNotBlank() && fComplaint.isNotBlank()
                    ) {
                        Text(
                            text = "Simpan Dokumen Konseling",
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showFormScreen = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DenimBlue),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = PureWhite,
                            contentColor = DeepNavy
                        )
                    ) {
                        Text(
                            text = "Batal",
                            color = DeepNavy,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }
    }

    // Audio Transcription & Summary Modal
    if (showAudioModal) {
        AlertDialog(
            onDismissRequest = { showAudioModal = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = PureWhite,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldenYellow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hasil Analisis Audio AI", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Surface(
                        color = IceBlue.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Audio Berhasil Diproses (Akurasi 98.4%)", fontSize = 11.sp, color = DeepNavy, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Transkrip Percakapan:", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = simulatedTranscript,
                            style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant),
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Ringkasan Inti Permasalahan:", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = SoftIceBlue,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = simulatedSummary,
                            style = MaterialTheme.typography.bodySmall.copy(color = DeepNavy, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        fComplaint = simulatedSummary
                        fBackgroundCause = "Diperoleh dari ekstraksi AI audio transkripsi otomatis."
                        showAudioModal = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Ringkasan audio berhasil dimasukkan ke formulir")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Terapkan ke Form", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAudioModal = false }) {
                    Text("Tutup", color = DeepNavy)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        visible = noteToDelete != null,
        title = "Hapus Dokumen Konseling",
        message = "Apakah Anda yakin ingin menghapus dokumen konseling atas nama ${noteToDelete?.studentName}? Tindakan ini permanen.",
        onConfirm = {
            noteToDelete?.let {
                MockRepository.deleteCounselingNote(it.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Dokumen konseling telah dihapus")
                }
            }
            noteToDelete = null
        },
        onDismiss = { noteToDelete = null }
    )
}

@Preview(name = "Admin - Rekam Catatan Konseling", showBackground = true, showSystemUi = true)
@Composable
fun AdminCounselingNotesScreenPreview() {
    BkCounselingTheme {
        AdminCounselingNotesScreen(onNavigateBack = {})
    }
}
