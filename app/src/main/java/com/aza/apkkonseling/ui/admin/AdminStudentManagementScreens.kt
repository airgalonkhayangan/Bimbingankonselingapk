package com.aza.apkkonseling.ui.admin

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
import com.aza.apkkonseling.data.AcademicSemesterRecord
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.data.NonAcademicAchievement
import com.aza.apkkonseling.data.PsychologicalTestResult
import com.aza.apkkonseling.data.StudentProfile
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
fun AdminStudentManagementScreens(
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedYearFilter by remember { mutableStateOf("Semua") }
    var selectedStudentId by remember { mutableStateOf<String?>(null) }
    var detailTab by remember { mutableIntStateOf(0) } // 0: Data Pribadi, 1: Tes & Prestasi, 2: Catatan Kasus, 3: Janji Konseling

    // Student CRUD Dialog state
    var showStudentDialog by remember { mutableStateOf(false) }
    var editingStudent by remember { mutableStateOf<StudentProfile?>(null) }
    var studentToDelete by remember { mutableStateOf<StudentProfile?>(null) }

    var inputNisn by remember { mutableStateOf("") }
    var inputName by remember { mutableStateOf("") }
    var inputClass by remember { mutableStateOf("X-A") }
    var inputYear by remember { mutableStateOf("2026/2027") }
    var inputGender by remember { mutableStateOf("Laki-laki") }
    var inputIntakeType by remember { mutableStateOf("Siswa Baru (Tahun Ini)") }
    var inputBirth by remember { mutableStateOf("Jakarta, 14 Mei 2008") }
    var inputAddress by remember { mutableStateOf("Jl. Merdeka No. 45") }
    var inputParentName by remember { mutableStateOf("H. Hendra Fauzi") }
    var inputParentPhone by remember { mutableStateOf("0812-3456-7890") }

    // Dialog states for Sub-data
    var showPsychoDialog by remember { mutableStateOf(false) }
    var inputPsychoType by remember { mutableStateOf("MBTI: ENFJ (Protagonist)") }
    var inputPsychoInterests by remember { mutableStateOf("Sains, Sosial, Komunikasi") }
    var inputPsychoAptitudes by remember { mutableStateOf("Kepemimpinan (88%), Analitis (82%)") }
    var inputPsychoDate by remember { mutableStateOf("15 Oktober 2026") }
    var inputPsychoNotes by remember { mutableStateOf("Siswa memiliki potensi kepemimpinan kuat.") }

    var showAddAcademicDialog by remember { mutableStateOf(false) }
    var inputSemesterName by remember { mutableStateOf("Semester 1 (Ganjil)") }
    var inputAcademicYear by remember { mutableStateOf("2026/2027") }
    var inputAverageScore by remember { mutableStateOf("88.50") }
    var inputRank by remember { mutableStateOf("3") }
    var inputTotalStudentsInClass by remember { mutableStateOf("36") }

    var showAddNonAcademicDialog by remember { mutableStateOf(false) }
    var inputNonAcademicTitle by remember { mutableStateOf("") }
    var inputNonAcademicCategory by remember { mutableStateOf("Sains / Riset") }
    var inputNonAcademicLevel by remember { mutableStateOf("Tingkat Kota / Kabupaten") }
    var inputNonAcademicYear by remember { mutableStateOf("2026") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun openStudentDialog(student: StudentProfile?) {
        if (student != null) {
            editingStudent = student
            inputNisn = student.nisn
            inputName = student.name
            inputClass = student.className
            inputYear = student.academicYear
            inputGender = student.gender
            inputIntakeType = student.intakeType
            inputBirth = student.birthPlaceDate
            inputAddress = student.address
            inputParentName = student.parentName
            inputParentPhone = student.parentPhone
        } else {
            editingStudent = null
            inputNisn = ""
            inputName = ""
            inputClass = "X-A"
            inputYear = "2026/2027"
            inputGender = "Laki-laki"
            inputIntakeType = "Siswa Baru (Tahun Ini)"
            inputBirth = "Jakarta, 01 Januari 2009"
            inputAddress = "Jl. Contoh Alamat No. 1"
            inputParentName = "Orang Tua Siswa"
            inputParentPhone = "0812-0000-0000"
        }
        showStudentDialog = true
    }

    val liveSelectedStudent = MockRepository.students.firstOrNull { it.id == selectedStudentId }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (liveSelectedStudent == null) "Data Siswa & Intake" else "Profil & Riwayat Siswa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DeepNavy
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (liveSelectedStudent != null) selectedStudentId = null else onNavigateBack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            if (liveSelectedStudent == null) {
                // ==========================================
                // 1. LIST OF STUDENTS
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Cari nama, NISN, kelas...", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DenimBlue) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = PureWhite,
                                focusedContainerColor = PureWhite,
                                unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                                focusedBorderColor = DenimBlue
                            ),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { openStudentDialog(null) },
                            shape = RoundedCornerShape(14.dp),
                            colors = bkButtonColors(DenimBlue),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tambah", color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Filter Angkatan / Intake:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(6.dp))
                    val years = listOf("Semua", "2026/2027", "2025/2026", "2024/2025")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(years) { yr ->
                            val isSelected = selectedYearFilter == yr
                            BkSelectableChip(
                                selected = isSelected,
                                onClick = { selectedYearFilter = yr },
                                label = yr
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val filteredStudents = MockRepository.students.filter { stu ->
                        (selectedYearFilter == "Semua" || stu.academicYear == selectedYearFilter) &&
                                (stu.name.contains(searchQuery, ignoreCase = true) ||
                                        stu.nisn.contains(searchQuery, ignoreCase = true) ||
                                        stu.className.contains(searchQuery, ignoreCase = true))
                    }

                    if (filteredStudents.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.PersonOff,
                            title = "Siswa Tidak Ditemukan",
                            description = "Tidak ada data siswa yang cocok dengan filter atau kata kunci.",
                            actionText = "Tambah Siswa Baru",
                            onActionClick = { openStudentDialog(null) }
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(filteredStudents) { student ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { selectedStudentId = student.id },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(CircleShape)
                                                .background(IceBlue),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(26.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = student.name,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                            )
                                            Text(
                                                text = "NISN: ${student.nisn} • Kelas: ${student.className}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                            )
                                            Surface(
                                                color = SoftIceBlue,
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                Text(
                                                    text = "${student.academicYear} • ${student.intakeType}",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = DenimBlue, fontWeight = FontWeight.Bold)
                                                )
                                            }
                                        }
                                        Row {
                                            IconButton(
                                                onClick = { openStudentDialog(student) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = SteelBlue, modifier = Modifier.size(18.dp))
                                            }
                                            IconButton(
                                                onClick = { studentToDelete = student },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = SosRed, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // ==========================================
                // 2. STUDENT DETAIL VIEW: 4 HORIZONTAL TABS
                // ==========================================
                val stu = liveSelectedStudent
                val studentCounselingNotes = MockRepository.counselingNotes.filter { it.nisn == stu.nisn || it.studentName == stu.name }
                val studentAppointments = MockRepository.appointments.filter { it.nisn == stu.nisn || it.studentName == stu.name }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Student Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(IceBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(28.dp))
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(stu.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy))
                                    Text("NISN: ${stu.nisn} • Kelas: ${stu.className}", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey))
                                    Text("TA: ${stu.academicYear} • ${stu.intakeType}", style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.SemiBold, fontSize = 11.sp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 Clean Horizontal Tabs
                    val detailTabs = listOf(
                        "Data Pribadi",
                        "Tes & Prestasi",
                        "Catatan Kasus (${studentCounselingNotes.size})",
                        "Janji Konseling (${studentAppointments.size})"
                    )

                    ScrollableTabRow(
                        selectedTabIndex = detailTab,
                        containerColor = PureWhite,
                        contentColor = DenimBlue,
                        edgePadding = 8.dp,
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    ) {
                        detailTabs.forEachIndexed { index, tabName ->
                            Tab(
                                selected = detailTab == index,
                                onClick = { detailTab = index },
                                text = {
                                    Text(
                                        text = tabName,
                                        fontSize = 12.sp,
                                        fontWeight = if (detailTab == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (detailTab == index) DenimBlue else NeutralGrey
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    when (detailTab) {
                        0 -> {
                            // ==========================================
                            // TAB 0: DATA PRIBADI SISWA
                            // ==========================================
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
                                            text = "Data Pribadi & Kontak Siswa",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                        )
                                        IconButton(onClick = { openStudentDialog(stu) }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = DenimBlue, modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = SoftIceBlue)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    ProfileInfoRow("Nama Lengkap", stu.name)
                                    ProfileInfoRow("NISN", stu.nisn)
                                    ProfileInfoRow("Kelas", stu.className)
                                    ProfileInfoRow("Jenis Kelamin", stu.gender)
                                    ProfileInfoRow("Tahun Ajaran / Masuk", stu.academicYear)
                                    ProfileInfoRow("Kategori Intake", stu.intakeType)
                                    ProfileInfoRow("Tempat, Tanggal Lahir", stu.birthPlaceDate)
                                    ProfileInfoRow("Alamat Domisili", stu.address)
                                    ProfileInfoRow("Nama Orang Tua / Wali", stu.parentName)
                                    ProfileInfoRow("No. Telepon Ortu / Darurat", stu.parentPhone)
                                }
                            }
                        }

                        1 -> {
                            // ==========================================
                            // TAB 1: HASIL TES PSIKOLOGI & PRESTASI
                            // ==========================================
                            // 1. Hasil Tes Psikologi / Kepribadian
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
                                            text = "Hasil Tes Psikologi / Kepribadian",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                            modifier = Modifier.weight(1f),
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Button(
                                            onClick = {
                                                val existing = stu.psychologicalTest
                                                if (existing != null) {
                                                    inputPsychoType = existing.personalityType
                                                    inputPsychoInterests = existing.mainInterests
                                                    inputPsychoAptitudes = existing.dominantAptitudes
                                                    inputPsychoDate = existing.testDate
                                                    inputPsychoNotes = existing.counselorNotes
                                                }
                                                showPsychoDialog = true
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = bkButtonColors(DenimBlue),
                                            modifier = Modifier.height(36.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (stu.psychologicalTest != null) "Edit" else "Tambah", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PureWhite, maxLines = 1, softWrap = false)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    val psycho = stu.psychologicalTest
                                    if (psycho == null) {
                                        Text("Belum ada rekam hasil tes kepribadian atau psikologi siswa.", color = NeutralGrey, fontSize = 12.sp)
                                    } else {
                                        Surface(
                                            color = SoftIceBlue,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(
                                                    text = psycho.personalityType,
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DenimBlue)
                                                )
                                                Text(
                                                    text = "Tanggal Tes: ${psycho.testDate}",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("Minat Dominan: ${psycho.mainInterests}", style = MaterialTheme.typography.bodySmall.copy(color = DeepNavy, fontWeight = FontWeight.Medium))
                                                Text("Bakat / Kemampuan: ${psycho.dominantAptitudes}", style = MaterialTheme.typography.bodySmall.copy(color = DeepNavy, fontWeight = FontWeight.Medium))
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("Catatan & Rekomendasi Guru BK:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy))
                                                Text(psycho.counselorNotes, style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant, fontSize = 12.sp))
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 2. Prestasi Akademik per Semester
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
                                            text = "Prestasi Akademik per Semester",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                            modifier = Modifier.weight(1f),
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Button(
                                            onClick = { showAddAcademicDialog = true },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = bkButtonColors(DenimBlue),
                                            modifier = Modifier.height(36.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Tambah", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PureWhite, maxLines = 1, softWrap = false)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    if (stu.academicRecords.isEmpty()) {
                                        Text("Belum ada rekam nilai atau peringkat semester.", color = NeutralGrey, fontSize = 12.sp)
                                    } else {
                                        stu.academicRecords.forEach { rec ->
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
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = "${rec.semester} • TA ${rec.academicYear}",
                                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                                        )
                                                        Text(
                                                            text = "Peringkat Kelas: ${rec.classRank} dari ${rec.totalStudentsInClass} siswa",
                                                            style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue)
                                                        )
                                                    }
                                                    Surface(
                                                        color = PureWhite,
                                                        shape = RoundedCornerShape(8.dp),
                                                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                                    ) {
                                                        Text(
                                                            text = "Rata: ${rec.averageScore}",
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = DenimBlue)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 3. Prestasi Non-Akademik
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
                                            text = "Prestasi Non-Akademik",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                            modifier = Modifier.weight(1f),
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Button(
                                            onClick = { showAddNonAcademicDialog = true },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = bkButtonColors(DenimBlue),
                                            modifier = Modifier.height(36.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Tambah", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PureWhite, maxLines = 1, softWrap = false)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    if (stu.nonAcademicAchievements.isEmpty()) {
                                        Text("Belum ada rekam prestasi non-akademik.", color = NeutralGrey, fontSize = 12.sp)
                                    } else {
                                        stu.nonAcademicAchievements.forEach { na ->
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
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = WarmYellow, modifier = Modifier.size(24.dp))
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = na.title,
                                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                                        )
                                                        Text(
                                                            text = "${na.category} • ${na.level} (${na.year})",
                                                            style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontSize = 11.sp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        2 -> {
                            // ==========================================
                            // TAB 2: CATATAN KASUS
                            // ==========================================
                            if (studentCounselingNotes.isEmpty()) {
                                EmptyStateView(
                                    icon = Icons.Default.Description,
                                    title = "Belum Ada Catatan Konseling",
                                    description = "Siswa ini belum memiliki catatan kasus konseling terdaftar."
                                )
                            } else {
                                studentCounselingNotes.forEach { note ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "${note.serviceDomain} - ${note.serviceType}",
                                                    fontWeight = FontWeight.Bold,
                                                    color = DenimBlue,
                                                    modifier = Modifier.weight(1f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                BkStatusBadge(status = note.status)
                                            }
                                            Text(note.date, style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp))
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text("Keluhan: " + note.complaint, style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavy))
                                            Text("Hasil: " + note.result, style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant))
                                        }
                                    }
                                }
                            }
                        }

                        3 -> {
                            // ==========================================
                            // TAB 3: JANJI KONSELING
                            // ==========================================
                            if (studentAppointments.isEmpty()) {
                                EmptyStateView(
                                    icon = Icons.Default.EventBusy,
                                    title = "Belum Ada Janji Konseling",
                                    description = "Siswa ini belum memiliki riwayat pengajuan janji konseling."
                                )
                            } else {
                                studentAppointments.forEach { apt ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = apt.teacherName,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepNavy,
                                                    modifier = Modifier.weight(1f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                BkStatusBadge(status = apt.status)
                                            }
                                            Text("Jadwal: ${apt.date} (${apt.timeSlot})", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp))
                                            Text("Metode: ${apt.mode}", style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontSize = 11.sp))
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("Alasan: \"${apt.reason}\"", style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavyVariant))
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

    // Add / Edit Student Profile Dialog
    if (showStudentDialog) {
        AlertDialog(
            onDismissRequest = { showStudentDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = PureWhite,
            title = {
                Text(
                    text = if (editingStudent == null) "Tambah Siswa Baru" else "Edit Profil Siswa",
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(value = inputNisn, onValueChange = { inputNisn = it }, label = { Text("NISN (10 Digit)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputName, onValueChange = { inputName = it }, label = { Text("Nama Lengkap Siswa") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputClass, onValueChange = { inputClass = it }, label = { Text("Kelas (cth: X-A, XI IPS 2)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputYear, onValueChange = { inputYear = it }, label = { Text("Tahun Ajaran (cth: 2026/2027)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputGender, onValueChange = { inputGender = it }, label = { Text("Jenis Kelamin (Laki-laki/Perempuan)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputIntakeType, onValueChange = { inputIntakeType = it }, label = { Text("Kategori Intake") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputBirth, onValueChange = { inputBirth = it }, label = { Text("Tempat, Tanggal Lahir") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputAddress, onValueChange = { inputAddress = it }, label = { Text("Alamat Domisili Siswa") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputParentName, onValueChange = { inputParentName = it }, label = { Text("Nama Orang Tua / Wali") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputParentPhone, onValueChange = { inputParentPhone = it }, label = { Text("No. HP Orang Tua / Darurat") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputNisn.isNotBlank() && inputName.isNotBlank()) {
                            if (editingStudent != null) {
                                MockRepository.updateStudent(
                                    id = editingStudent!!.id,
                                    nisn = inputNisn,
                                    name = inputName,
                                    className = inputClass,
                                    academicYear = inputYear,
                                    gender = inputGender,
                                    intakeType = inputIntakeType,
                                    birthPlaceDate = inputBirth,
                                    address = inputAddress,
                                    parentName = inputParentName,
                                    parentPhone = inputParentPhone
                                )
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Data siswa berhasil diperbarui")
                                }
                            } else {
                                MockRepository.addStudent(
                                    nisn = inputNisn,
                                    name = inputName,
                                    className = inputClass,
                                    academicYear = inputYear,
                                    gender = inputGender,
                                    intakeType = inputIntakeType,
                                    birthPlaceDate = inputBirth,
                                    address = inputAddress,
                                    parentName = inputParentName,
                                    parentPhone = inputParentPhone
                                )
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Siswa baru berhasil ditambahkan")
                                }
                            }
                            showStudentDialog = false
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStudentDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Psychological Test Dialog
    if (showPsychoDialog && liveSelectedStudent != null) {
        AlertDialog(
            onDismissRequest = { showPsychoDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = PureWhite,
            title = { Text("Input / Sunting Hasil Tes Psikologi", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Jenis Tes & Tipe Kepribadian:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = inputPsychoType, onValueChange = { inputPsychoType = it }, placeholder = { Text("cth: MBTI - ENFJ atau Holland - RIASEC") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tanggal Pelaksanaan Tes:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = inputPsychoDate, onValueChange = { inputPsychoDate = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Minat Dominan:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = inputPsychoInterests, onValueChange = { inputPsychoInterests = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Bakat & Potensi Kunci:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = inputPsychoAptitudes, onValueChange = { inputPsychoAptitudes = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Catatan & Rekomendasi Guru BK:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = inputPsychoNotes, onValueChange = { inputPsychoNotes = it }, minLines = 2, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val test = PsychologicalTestResult(
                            personalityType = inputPsychoType,
                            mainInterests = inputPsychoInterests,
                            dominantAptitudes = inputPsychoAptitudes,
                            testDate = inputPsychoDate,
                            counselorNotes = inputPsychoNotes
                        )
                        MockRepository.updateStudentPsychologicalTest(liveSelectedStudent.id, test)
                        showPsychoDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Hasil tes psikologi berhasil disimpan!")
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan Hasil Tes", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPsychoDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Add Academic Record Dialog
    if (showAddAcademicDialog && liveSelectedStudent != null) {
        AlertDialog(
            onDismissRequest = { showAddAcademicDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = PureWhite,
            title = { Text("Tambah Rekam Prestasi Akademik", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = {
                Column {
                    OutlinedTextField(value = inputSemesterName, onValueChange = { inputSemesterName = it }, label = { Text("Semester (cth: Semester 1)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputAcademicYear, onValueChange = { inputAcademicYear = it }, label = { Text("Tahun Ajaran (cth: 2026/2027)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = inputAverageScore, onValueChange = { inputAverageScore = it }, label = { Text("Nilai Rata-rata Raport") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = inputRank, onValueChange = { inputRank = it }, label = { Text("Peringkat") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp))
                        OutlinedTextField(value = inputTotalStudentsInClass, onValueChange = { inputTotalStudentsInClass = it }, label = { Text("Total Siswa") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val record = AcademicSemesterRecord(
                            id = "AC-${System.currentTimeMillis() % 10000}",
                            academicYear = inputAcademicYear,
                            semester = inputSemesterName,
                            averageScore = inputAverageScore.toDoubleOrNull() ?: 85.0,
                            rankInClass = inputRank.toIntOrNull() ?: 1,
                            totalStudentsInClass = inputTotalStudentsInClass.toIntOrNull() ?: 36
                        )
                        MockRepository.addAcademicRecord(liveSelectedStudent.id, record)
                        showAddAcademicDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Prestasi akademik semester berhasil ditambahkan!")
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAcademicDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Add Non-Academic Achievement Dialog
    if (showAddNonAcademicDialog && liveSelectedStudent != null) {
        val categories = listOf("Sains / Riset", "Olahraga", "Seni & Musik", "Organisasi", "Lainnya")
        val levels = listOf("Tingkat Sekolah", "Tingkat Kota / Kabupaten", "Tingkat Provinsi", "Tingkat Nasional", "Tingkat Internasional")

        AlertDialog(
            onDismissRequest = { showAddNonAcademicDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = PureWhite,
            title = { Text("Tambah Prestasi Non-Akademik", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Nama Kejuaraan / Prestasi:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = inputNonAcademicTitle, onValueChange = { inputNonAcademicTitle = it }, placeholder = { Text("cth: Juara 1 Turnamen Futsal Pelajar") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Kategori Bidang:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(categories) { cat ->
                            BkSelectableChip(
                                selected = inputNonAcademicCategory == cat,
                                onClick = { inputNonAcademicCategory = cat },
                                label = cat
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tingkat Kejuaraan:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(levels) { lvl ->
                            BkSelectableChip(
                                selected = inputNonAcademicLevel == lvl,
                                onClick = { inputNonAcademicLevel = lvl },
                                label = lvl
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tahun Perolehan:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = inputNonAcademicYear, onValueChange = { inputNonAcademicYear = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputNonAcademicTitle.isNotBlank()) {
                            val achievement = NonAcademicAchievement(
                                id = "NA-${System.currentTimeMillis() % 10000}",
                                achievementName = inputNonAcademicTitle,
                                category = inputNonAcademicCategory,
                                level = inputNonAcademicLevel,
                                year = inputNonAcademicYear
                            )
                            MockRepository.addNonAcademicAchievement(liveSelectedStudent.id, achievement)
                            showAddNonAcademicDialog = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Prestasi non-akademik berhasil ditambahkan!")
                            }
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNonAcademicDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        visible = studentToDelete != null,
        title = "Hapus Data Siswa",
        message = "Apakah Anda yakin ingin menghapus data siswa ${studentToDelete?.name} (${studentToDelete?.nisn})? Data yang dihapus tidak dapat dipulihkan.",
        onConfirm = {
            studentToDelete?.let {
                MockRepository.deleteStudent(it.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Data siswa telah dihapus")
                }
            }
            studentToDelete = null
        },
        onDismiss = { studentToDelete = null }
    )
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
        )
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavy, fontWeight = FontWeight.SemiBold)
        )
    }
}

@Preview(name = "Admin - Kelola Data Siswa", showBackground = true, showSystemUi = true)
@Composable
fun AdminStudentManagementScreensPreview() {
    BkCounselingTheme {
        AdminStudentManagementScreens(onNavigateBack = {})
    }
}

