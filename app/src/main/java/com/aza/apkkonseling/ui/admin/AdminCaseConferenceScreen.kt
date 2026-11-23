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
import com.aza.apkkonseling.data.CaseConference
import com.aza.apkkonseling.data.CaseConferenceAttendee
import com.aza.apkkonseling.data.CaseConferenceDoc
import com.aza.apkkonseling.data.MockRepository
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
fun AdminCaseConferenceScreen(
    onNavigateBack: () -> Unit
) {
    var selectedConference by remember { mutableStateOf<CaseConference?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("Semua") }

    // Dialog States
    var showAddConferenceDialog by remember { mutableStateOf(false) }
    var conferenceToDelete by remember { mutableStateOf<CaseConference?>(null) }

    // Input States for New Case Conference
    var inputTitle by remember { mutableStateOf("") }
    var inputDate by remember { mutableStateOf("26 Nov 2026") }
    var inputTime by remember { mutableStateOf("09:00 - 11:30 WIB") }
    var inputLocation by remember { mutableStateOf("Ruang Konferensi Kasus BK") }
    var inputChairPerson by remember { mutableStateOf(MockRepository.currentTeacherName) }
    var inputSummary by remember { mutableStateOf("") }

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
                            text = if (selectedConference == null) "Gelar Kasus (Case Conference)" else "Detail Gelar Kasus",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DeepNavy
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (selectedConference != null) selectedConference = null else onNavigateBack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            if (selectedConference == null) {
                // ==========================================
                // 1. LIST VIEW OF CASE CONFERENCES
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Manajemen Sidang Kasus & Kolaborasi Penanganan",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Fasilitasi pertemuan lintas pihak (wali kelas, guru BK, orang tua, pimpinan) untuk pemecahan masalah siswa secara komprehensif.",
                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 12.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search & Add Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Cari judul, pimpinan, lokasi...", fontSize = 13.sp) },
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
                            onClick = {
                                inputTitle = ""
                                inputSummary = ""
                                showAddConferenceDialog = true
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = bkButtonColors(DenimBlue),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tambah", color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Status Filter Chips
                    val statusFilters = listOf("Semua", "Dalam Proses", "Selesai", "Menunggu Pelaksanaan")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(statusFilters) { st ->
                            BkSelectableChip(
                                selected = selectedStatusFilter == st,
                                onClick = { selectedStatusFilter = st },
                                label = st
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val filteredList = MockRepository.caseConferences.filter { conf ->
                        (selectedStatusFilter == "Semua" || conf.status == selectedStatusFilter) &&
                                (conf.title.contains(searchQuery, ignoreCase = true) ||
                                        conf.chairPerson.contains(searchQuery, ignoreCase = true) ||
                                        conf.location.contains(searchQuery, ignoreCase = true))
                    }

                    if (filteredList.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.Groups,
                            title = "Tidak Ada Gelar Kasus",
                            description = "Belum ada agenda konferensi kasus yang sesuai dengan kriteria filter.",
                            actionText = "Buat Agenda Baru",
                            onActionClick = { showAddConferenceDialog = true }
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredList) { conf ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { selectedConference = conf },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = conf.title,
                                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Pimpinan: ${conf.chairPerson}",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.SemiBold)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            BkStatusBadge(status = conf.status)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        HorizontalDivider(color = SoftIceBlue)
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Event, contentDescription = null, tint = NeutralGrey, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${conf.date} • ${conf.time}",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                                )
                                            }

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Group, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${conf.attendees.size} Peserta",
                                                    style = MaterialTheme.typography.bodySmall.copy(color = DenimBlue, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Place, contentDescription = null, tint = NeutralGrey, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = conf.location,
                                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // ==========================================
                // 2. DETAIL VIEW OF CASE CONFERENCE
                // ==========================================
                val conf = selectedConference!!
                // Re-fetch from repository to ensure live state
                val liveConf = MockRepository.caseConferences.firstOrNull { it.id == conf.id } ?: conf
                CaseConferenceDetailContent(
                    conference = liveConf,
                    paddingValues = paddingValues,
                    onStatusUpdated = { updatedConf ->
                        MockRepository.updateCaseConference(updatedConf)
                        selectedConference = updatedConf
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Data Gelar Kasus berhasil diperbarui!")
                        }
                    },
                    onDeleteRequested = {
                        conferenceToDelete = liveConf
                    }
                )
            }
        }
    }

    // Add Conference Dialog
    if (showAddConferenceDialog) {
        AlertDialog(
            onDismissRequest = { showAddConferenceDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = PureWhite,
            title = {
                Text("Agenda Gelar Kasus Baru", fontWeight = FontWeight.Bold, color = DeepNavy)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Judul / Topik Konferensi Kasus:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputTitle,
                        onValueChange = { inputTitle = it },
                        placeholder = { Text("Contoh: Konferensi Penanganan Kasus X", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Tanggal Pelaksanaan:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputDate,
                        onValueChange = { inputDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Waktu:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputTime,
                        onValueChange = { inputTime = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Lokasi Ruangan:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputLocation,
                        onValueChange = { inputLocation = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Pimpinan Rapat:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputChairPerson,
                        onValueChange = { inputChairPerson = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Ringkasan Kasus / Latar Belakang:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputSummary,
                        onValueChange = { inputSummary = it },
                        placeholder = { Text("Latar belakang masalah yang akan dibahas...", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputTitle.isNotBlank()) {
                            val newConf = CaseConference(
                                id = "CC-${System.currentTimeMillis() % 10000}",
                                title = inputTitle,
                                date = inputDate,
                                time = inputTime,
                                location = inputLocation,
                                chairPerson = inputChairPerson,
                                status = "Dalam Proses",
                                discussionSummary = inputSummary,
                                jointAgreement = "",
                                followUpRecommendation = "",
                                attendees = listOf(
                                    CaseConferenceAttendee("ATT-1", inputChairPerson, "Pimpinan Rapat", "Hadir"),
                                    CaseConferenceAttendee("ATT-2", "Wali Kelas Terkait", "Wali Kelas", "Hadir")
                                ),
                                documentations = emptyList()
                            )
                            MockRepository.addCaseConference(newConf)
                            showAddConferenceDialog = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Agenda Gelar Kasus berhasil ditambahkan!")
                            }
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan Agenda", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddConferenceDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        visible = conferenceToDelete != null,
        title = "Hapus Gelar Kasus?",
        message = "Apakah Anda yakin ingin menghapus agenda gelar kasus \"${conferenceToDelete?.title}\"? Data yang dihapus tidak dapat dipulihkan.",
        onConfirm = {
            conferenceToDelete?.let { conf ->
                MockRepository.deleteCaseConference(conf.id)
                conferenceToDelete = null
                selectedConference = null
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Gelar Kasus berhasil dihapus.")
                }
            }
        },
        onDismiss = { conferenceToDelete = null }
    )
}

@Composable
private fun CaseConferenceDetailContent(
    conference: CaseConference,
    paddingValues: PaddingValues,
    onStatusUpdated: (CaseConference) -> Unit,
    onDeleteRequested: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Daftar Hadir, 1: Dokumentasi, 2: Laporan

    // Dialog states for sub-items
    var showAddAttendeeDialog by remember { mutableStateOf(false) }
    var newAttendeeName by remember { mutableStateOf("") }
    var newAttendeeRole by remember { mutableStateOf("Wali Kelas") }

    var showAddDocDialog by remember { mutableStateOf(false) }
    var newDocTitle by remember { mutableStateOf("") }
    var newDocDesc by remember { mutableStateOf("") }

    // Report edit fields
    var discussionSummary by remember { mutableStateOf(conference.discussionSummary) }
    var jointAgreement by remember { mutableStateOf(conference.jointAgreement) }
    var followUpRecommendation by remember { mutableStateOf(conference.followUpRecommendation) }
    var selectedStatus by remember { mutableStateOf(conference.status) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Main Header Card
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
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = conference.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pimpinan: ${conference.chairPerson}",
                            style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    BkStatusBadge(status = conference.status)
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SoftIceBlue)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Jadwal: ${conference.date}, ${conference.time}", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 12.sp))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("Lokasi: ${conference.location}", style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 12.sp))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Navigation: 3 Distinct Sections
        val tabs = listOf("Daftar Hadir", "Dokumentasi", "Laporan")
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = PureWhite,
            contentColor = DenimBlue,
            modifier = Modifier.clip(RoundedCornerShape(14.dp))
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = tabTitle,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) DenimBlue else NeutralGrey,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> {
                // ==========================================
                // SECTION 1: DAFTAR HADIR PESERTA (ATTENDEES)
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
                                text = "Daftar Hadir Peserta",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    newAttendeeName = ""
                                    newAttendeeRole = "Wali Kelas"
                                    showAddAttendeeDialog = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = bkButtonColors(DenimBlue),
                                modifier = Modifier.height(36.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tambah", fontSize = 12.sp, color = PureWhite, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (conference.attendees.isEmpty()) {
                            Text("Belum ada daftar hadir peserta.", color = NeutralGrey, fontSize = 13.sp)
                        } else {
                            conference.attendees.forEach { att ->
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
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = att.name,
                                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                                )
                                                Text(
                                                    text = att.role,
                                                    style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue)
                                                )
                                            }

                                            // Interactive Attendance Toggles (STRICTLY NO GREEN: Hadir uses IceBlue / DenimBlue)
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                listOf("Hadir", "Izin", "Tidak Hadir").forEach { statusOption ->
                                                    val isSelected = att.status == statusOption
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = if (isSelected) {
                                                            when (statusOption) {
                                                                "Hadir" -> DenimBlue // Denim blue, NO GREEN!
                                                                "Izin" -> WarmYellow
                                                                else -> SosRed
                                                            }
                                                        } else PureWhite,
                                                        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else SoftSkyBlue.copy(alpha = 0.5f)),
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .clickable {
                                                                MockRepository.updateAttendeeStatus(conference.id, att.id, statusOption)
                                                                val updatedAttendees = conference.attendees.map {
                                                                    if (it.id == att.id) it.copy(status = statusOption) else it
                                                                }
                                                                onStatusUpdated(conference.copy(attendees = updatedAttendees))
                                                            }
                                                    ) {
                                                        Text(
                                                            text = statusOption,
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                            style = MaterialTheme.typography.bodySmall.copy(
                                                                fontSize = 11.sp,
                                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                                color = if (isSelected) PureWhite else DeepNavy
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
                    }
                }
            }

            1 -> {
                // ==========================================
                // SECTION 2: DOKUMENTASI BUKTI ACARA
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
                                text = "Dokumentasi & Bukti Acara",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    newDocTitle = ""
                                    newDocDesc = ""
                                    showAddDocDialog = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = bkButtonColors(DenimBlue),
                                modifier = Modifier.height(36.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tambah", fontSize = 12.sp, color = PureWhite, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (conference.documentations.isEmpty()) {
                            Text("Belum ada bukti dokumentasi atau notula yang diunggah.", color = NeutralGrey, fontSize = 13.sp)
                        } else {
                            conference.documentations.forEach { doc ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = SoftIceBlue),
                                    border = BorderStroke(0.5.dp, SoftSkyBlue.copy(alpha = 0.4f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(IceBlue),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Description, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(28.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = doc.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                            )
                                            Text(
                                                text = doc.timestamp,
                                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = doc.description,
                                                style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant, fontSize = 12.sp)
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
                // SECTION 3: LAPORAN HASIL RAPAT
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Laporan & Rekomendasi Hasil Rapat",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Status Konferensi Kasus:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Dalam Proses", "Selesai", "Menunggu Pelaksanaan").forEach { st ->
                                BkSelectableChip(
                                    selected = selectedStatus == st,
                                    onClick = { selectedStatus = st },
                                    label = st
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Ringkasan Pembahasan Kasus:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = discussionSummary,
                            onValueChange = { discussionSummary = it },
                            placeholder = { Text("Poin-poin penting pembahasan...", fontSize = 13.sp) },
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Kesepakatan Bersama Pihak Terkait:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = jointAgreement,
                            onValueChange = { jointAgreement = it },
                            placeholder = { Text("Kesepakatan antara sekolah, orang tua, dan siswa...", fontSize = 13.sp) },
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Rekomendasi Tindak Lanjut Guru BK:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = followUpRecommendation,
                            onValueChange = { followUpRecommendation = it },
                            placeholder = { Text("Rencana pendampingan, home visit, evaluasi lanjutan...", fontSize = 13.sp) },
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val updatedConf = conference.copy(
                                    status = selectedStatus,
                                    discussionSummary = discussionSummary,
                                    jointAgreement = jointAgreement,
                                    followUpRecommendation = followUpRecommendation
                                )
                                onStatusUpdated(updatedConf)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = bkButtonColors(DenimBlue)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simpan Perubahan Laporan", fontWeight = FontWeight.Bold, color = PureWhite)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onDeleteRequested,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SosRed),
                            border = BorderStroke(1.dp, SosRed.copy(alpha = 0.6f))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = SosRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hapus Agenda Gelar Kasus", fontWeight = FontWeight.Bold, color = SosRed)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }

    // Add Attendee Dialog
    if (showAddAttendeeDialog) {
        val roles = listOf("Wali Kelas", "Orang Tua / Wali", "Guru Mata Pelajaran", "Kepala Sekolah", "Psikolog Sekolah", "Siswa Terkait")
        AlertDialog(
            onDismissRequest = { showAddAttendeeDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = PureWhite,
            title = { Text("Tambah Peserta Gelar Kasus", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = {
                Column {
                    Text("Nama Peserta:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newAttendeeName,
                        onValueChange = { newAttendeeName = it },
                        placeholder = { Text("Nama lengkap peserta", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Peran / Instansi:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(roles) { r ->
                            BkSelectableChip(
                                selected = newAttendeeRole == r,
                                onClick = { newAttendeeRole = r },
                                label = r
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newAttendeeName.isNotBlank()) {
                            val newAtt = CaseConferenceAttendee(
                                id = "ATT-${System.currentTimeMillis() % 10000}",
                                name = newAttendeeName,
                                role = newAttendeeRole,
                                status = "Hadir"
                            )
                            MockRepository.addAttendee(conference.id, newAtt)
                            val updatedAttendees = conference.attendees.toMutableList().apply { add(newAtt) }
                            onStatusUpdated(conference.copy(attendees = updatedAttendees))
                            showAddAttendeeDialog = false
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Tambahkan", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAttendeeDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Add Documentation Dialog
    if (showAddDocDialog) {
        AlertDialog(
            onDismissRequest = { showAddDocDialog = false },
            shape = RoundedCornerShape(18.dp),
            containerColor = PureWhite,
            title = { Text("Tambah Bukti Acara / Dokumentasi", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = {
                Column {
                    Text("Judul Dokumentasi / Berita Acara:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newDocTitle,
                        onValueChange = { newDocTitle = it },
                        placeholder = { Text("Contoh: Notula Diskusi Orang Tua", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Deskripsi / Ringkasan Bukti:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = DeepNavy))
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newDocDesc,
                        onValueChange = { newDocDesc = it },
                        placeholder = { Text("Penjelasan bukti pelaksanaan kegiatan...", fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newDocTitle.isNotBlank()) {
                            val newDoc = CaseConferenceDoc(
                                id = "DOC-${System.currentTimeMillis() % 10000}",
                                title = newDocTitle,
                                timestamp = "Hari ini",
                                description = newDocDesc
                            )
                            MockRepository.addDocumentation(conference.id, newDoc)
                            val updatedDocs = conference.documentations.toMutableList().apply { add(newDoc) }
                            onStatusUpdated(conference.copy(documentations = updatedDocs))
                            showAddDocDialog = false
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan Bukti", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDocDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }
}

@Preview(name = "Admin - Gelar Kasus Konferensi", showBackground = true, showSystemUi = true)
@Composable
fun AdminCaseConferenceScreenPreview() {
    BkCounselingTheme {
        AdminCaseConferenceScreen(onNavigateBack = {})
    }
}
