package com.aza.apkkonseling.ui.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.aza.apkkonseling.data.ChatMessage
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.ConfirmDeleteDialog
import com.aza.apkkonseling.ui.components.EmptyStateView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminChatListScreen(
    onNavigateBack: () -> Unit,
    onChatSelected: (roomId: String) -> Unit
) {
    val chatRooms = MockRepository.getChatRooms()

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Pesan Masuk Siswa",
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
            ) {
                Text(
                    text = "Daftar Percakapan Siswa:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Setiap ruang obrolan terisolasi khusus antara siswa dan guru BK tujuan",
                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (chatRooms.isEmpty()) {
                    EmptyStateView(
                        icon = Icons.Default.ChatBubbleOutline,
                        title = "Belum Ada Obrolan",
                        description = "Belum ada pesan masuk dari siswa."
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(chatRooms) { room ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onChatSelected(room.roomId) },
                                shape = RoundedCornerShape(16.dp),
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
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(if (room.isAnonymous) WarmCreamDarker else IceBlue),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (room.isAnonymous) Icons.Default.VisibilityOff else Icons.Default.Person,
                                            contentDescription = null,
                                            tint = if (room.isAnonymous) AmberAccent else DenimBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = if (room.isAnonymous) "Siswa Anonim (Rahasia)" else "${room.studentName} (${room.studentClass})",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepNavy
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = room.lastTimestamp,
                                                fontSize = 11.sp,
                                                color = NeutralGrey
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = "Tujuan: ${room.teacherName}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = SteelBlue,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = room.lastMessage,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = 12.sp,
                                                color = if (room.isAnonymous) AmberAccent else DeepNavyVariant
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SoftSkyBlue)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminChatRoomScreen(
    roomId: String,
    onNavigateBack: () -> Unit
) {
    val roomMessages = MockRepository.chatMessages.filter { it.roomId == roomId && !it.isDeleted }
    val firstMsg = MockRepository.chatMessages.firstOrNull { it.roomId == roomId }
    val isAnonymous = firstMsg?.isAnonymous ?: roomId.endsWith("_anon")
    val student = MockRepository.students.find { it.id == firstMsg?.studentId }
    val teacher = MockRepository.teachers.find { it.id == firstMsg?.teacherId } ?: MockRepository.teachers.first()

    val studentDisplayName = if (isAnonymous) "Siswa Anonim" else (student?.name ?: firstMsg?.senderName ?: "Siswa")
    val studentClass = if (isAnonymous) "Identitas Rahasia" else (student?.className ?: "XII IPA 1")

    var inputText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var selectedMessageForOption by remember { mutableStateOf<ChatMessage?>(null) }
    var editingMessageId by remember { mutableStateOf<String?>(null) }
    var messageToDelete by remember { mutableStateOf<ChatMessage?>(null) }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(if (isAnonymous) WarmCreamDarker else IceBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isAnonymous) Icons.Default.VisibilityOff else Icons.Default.Person,
                                        contentDescription = null,
                                        tint = if (isAnonymous) AmberAccent else DenimBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "$studentDisplayName ($studentClass)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = DeepNavy,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Guru BK: ${teacher.name}",
                                        fontSize = 11.sp,
                                        color = SteelBlue,
                                        maxLines = 1
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
                    )

                    // Identity Banner at Top of Chat
                    Surface(
                        color = if (isAnonymous) WarmCream else SoftIceBlue,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isAnonymous) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = if (isAnonymous) AmberAccent else DenimBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAnonymous)
                                    "Mode Anonim: Identitas siswa dirahasiakan untuk keterbukaan curhat"
                                else
                                    "Identitas Asli: Siswa terverifikasi sistem BK",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepNavy
                                )
                            )
                        }
                    }
                }
            },
            bottomBar = {
                Surface(
                    color = PureWhite,
                    shadowElevation = 8.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Inline Edit Mode Indicator
                        AnimatedVisibility(visible = editingMessageId != null) {
                            Surface(
                                color = SoftYellow.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Edit, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Mengedit tanggapan guru BK...",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            editingMessageId = null
                                            inputText = ""
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Batal", tint = NeutralGrey, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("Balas pesan siswa...") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = LightGrey,
                                    focusedContainerColor = PureWhite
                                ),
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (inputText.isNotBlank()) {
                                        val activeEditId = editingMessageId
                                        if (activeEditId != null) {
                                            MockRepository.editChatMessage(activeEditId, inputText)
                                            editingMessageId = null
                                            inputText = ""
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Pesan berhasil diperbarui")
                                            }
                                        } else {
                                            val studentIdTarget = firstMsg?.studentId ?: "STU-1"
                                            MockRepository.sendChatMessage(
                                                roomId = roomId,
                                                studentId = studentIdTarget,
                                                teacherId = teacher.id,
                                                senderRole = "ADMIN",
                                                senderName = teacher.name,
                                                isAnonymous = isAnonymous,
                                                text = inputText
                                            )
                                            inputText = ""
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Balasan guru berhasil terkirim")
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(DenimBlue)
                            ) {
                                Icon(
                                    imageVector = if (editingMessageId != null) Icons.Default.Check else Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Kirim",
                                    tint = PureWhite
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            if (roomMessages.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Default.ChatBubbleOutline,
                    title = "Belum Ada Pesan",
                    description = "Belum ada pesan di dalam ruang obrolan ini.",
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    items(roomMessages) { message ->
                        val isTeacher = message.senderRole == "ADMIN"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalAlignment = if (isTeacher) Alignment.End else Alignment.Start
                        ) {
                            Card(
                                modifier = Modifier
                                    .widthIn(max = 290.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isTeacher) 16.dp else 4.dp,
                                            bottomEnd = if (isTeacher) 4.dp else 16.dp
                                        )
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isTeacher) DenimBlue else PureWhite
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = message.senderName,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isTeacher) SoftYellow else SteelBlue
                                            )
                                        )
                                        if (isTeacher) {
                                            IconButton(
                                                onClick = { selectedMessageForOption = message },
                                                modifier = Modifier.size(20.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.MoreVert,
                                                    contentDescription = "Opsi",
                                                    tint = PureWhite.copy(alpha = 0.85f),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = message.text,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (isTeacher) PureWhite else DeepNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        if (message.isEdited) {
                                            Text(
                                                text = "(diedit) ",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 10.sp,
                                                    color = if (isTeacher) PureWhite.copy(alpha = 0.7f) else NeutralGrey
                                                )
                                            )
                                        }
                                        Text(
                                            text = message.timestamp,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 10.sp,
                                                color = if (isTeacher) PureWhite.copy(alpha = 0.7f) else NeutralGrey
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

    // Teacher Message Options Modal Bottom Sheet
    if (selectedMessageForOption != null) {
        val msg = selectedMessageForOption!!
        ModalBottomSheet(
            onDismissRequest = { selectedMessageForOption = null },
            containerColor = PureWhite,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Opsi Pesan Guru BK",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                )
                Spacer(modifier = Modifier.height(14.dp))
                ListItem(
                    headlineContent = { Text("Sunting / Edit Pesan", color = DeepNavy) },
                    leadingContent = { Icon(Icons.Default.Edit, contentDescription = null, tint = DenimBlue) },
                    modifier = Modifier.clickable {
                        editingMessageId = msg.id
                        inputText = msg.text
                        selectedMessageForOption = null
                    }
                )
                HorizontalDivider(color = DividerGrey)
                ListItem(
                    headlineContent = { Text("Hapus Pesan", color = SosRed) },
                    leadingContent = { Icon(Icons.Default.Delete, contentDescription = null, tint = SosRed) },
                    modifier = Modifier.clickable {
                        messageToDelete = msg
                        selectedMessageForOption = null
                    }
                )
            }
        }
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        visible = messageToDelete != null,
        title = "Hapus Pesan",
        message = "Apakah Anda yakin ingin menghapus pesan ini dari percakapan siswa?",
        onConfirm = {
            messageToDelete?.let {
                MockRepository.deleteChatMessage(it.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Pesan telah dihapus")
                }
            }
            messageToDelete = null
        },
        onDismiss = { messageToDelete = null }
    )
}

@Preview(name = "Admin - Daftar Chat Siswa", showBackground = true, showSystemUi = true)
@Composable
fun AdminChatListScreenPreview() {
    BkCounselingTheme {
        AdminChatListScreen(
            onNavigateBack = {},
            onChatSelected = {}
        )
    }
}

@Preview(name = "Admin - Ruang Chat Siswa", showBackground = true, showSystemUi = true)
@Composable
fun AdminChatRoomScreenPreview() {
    BkCounselingTheme {
        AdminChatRoomScreen(
            roomId = MockRepository.getChatRooms().firstOrNull()?.roomId ?: "s1_t1_real",
            onNavigateBack = {}
        )
    }
}
